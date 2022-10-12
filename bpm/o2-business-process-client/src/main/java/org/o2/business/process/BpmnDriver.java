package org.o2.business.process;

import io.choerodon.core.convertor.ApplicationContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.util.ResponseUtils;
import org.o2.business.process.constants.BusinessProcessConstants;
import org.o2.business.process.exception.BusinessProcessRuntimeException;
import org.o2.business.process.exception.ProcessErrorHandler;
import org.o2.business.process.infra.BusinessProcessRemoteService;
import org.o2.core.O2CoreConstants;
import org.o2.core.helper.JsonHelper;
import org.o2.ehcache.util.CollectionCacheHelper;
import org.o2.process.domain.engine.BpmnModel;
import org.o2.process.domain.engine.BusinessProcessExecParam;
import org.o2.process.domain.engine.ProcessEngine;
import org.o2.process.domain.engine.process.preruntime.validator.BpmnModelValidator;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StopWatch;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 流水线驱动
 *
 * @author mark.bao@hand-china.com 2018/12/20
 */
@Slf4j
public class BpmnDriver {

    private BpmnDriver(){
        // 禁止构造该类
    }

    private final static Map<String, Long> PROCESS_LAST_UPDATE_TIME = new HashMap<>();

    public static <T extends BusinessProcessExecParam> void start(final Long tenantId, final String pipelineCode, final T pipelineNodeParams) {
        final BpmnModel bpmnModel = getPipelineDetail(tenantId, pipelineCode);
        start(bpmnModel, pipelineNodeParams);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BusinessProcessExecParam> void start(final BpmnModel processBO, T processExecParam) {
        final String processCode = processBO.getProcessCode();
        if (processExecParam == null) {
            throw new BusinessProcessRuntimeException(BusinessProcessConstants.ErrorMessage.PIPELINE_EXEC_PARAM_NULL, processCode);
        }

        if(null == processBO.getEnabledFlag() || O2CoreConstants.BooleanFlag.NOT_ENABLE == processBO.getEnabledFlag()){
            log.info("business process:{}, tenantId:{} not enabled", processCode, processBO.getTenantId());
            return;
        }

        final ApplicationContext applicationContext = ApplicationContextHelper.getContext();
        StopWatch stopWatch = new StopWatch(processCode);
        processExecParam.setCurrentParam(Collections.emptyMap());
        try {
            ProcessEngine<T> processEngine = applicationContext.getBean(ProcessEngine.class);
            processEngine.startProcess(processBO, processExecParam);
        }catch (Exception e){
            processExecParam.setException(e);
            processErrorHandel(processExecParam, processCode, applicationContext);
        }
        if (log.isDebugEnabled()) {
            log.debug("[processConfig]:Business Process :total time millis:{}, time consuming:{}", stopWatch.getTotalTimeMillis(), JsonHelper.objectToString(stopWatch.getTaskInfo()));
        }
    }

    @SuppressWarnings({
            "unchecked",
            "rawtypes"
    })
    private static <T extends BusinessProcessExecParam> void processErrorHandel(T processExecParam, String processCode, ApplicationContext applicationContext) {
        Map<String, ProcessErrorHandler> processErrorActionMap = applicationContext.getBeansOfType(ProcessErrorHandler.class)
                .values().stream().collect(Collectors.toMap(ProcessErrorHandler::getProcessCode, Function.identity(), (a, b) -> b));
        ProcessErrorHandler<T> processErrorHandler = processErrorActionMap.getOrDefault(processCode, processErrorActionMap.get(ProcessErrorHandler.DEFAULT));
        processErrorHandler.errorHandle(processCode, processExecParam);
    }

    public static BpmnModel getPipelineDetail(final Long tenantId, final String processCode) {
        if (StringUtils.isBlank(processCode)) {
            throw new BusinessProcessRuntimeException(BusinessProcessConstants.ErrorMessage.PIPELINE_CODE_NULL);
        }
        final BpmnModel bpmnModel = getPipelineByRemote(processCode, tenantId);
        if (bpmnModel == null || CollectionUtils.isEmpty(bpmnModel.getFlowElements())) {
            throw new BusinessProcessRuntimeException(BusinessProcessConstants.ErrorMessage.BUSINESS_PROCESS_NULL, processCode);
        }

        if (log.isDebugEnabled()) {
            log.debug("[processConfig], BusinessProcessContext:{}", JsonHelper.objectToString(bpmnModel));
        }

        bpmnModel.setProcessCode(processCode);
        return bpmnModel;
    }

    private static BpmnModel getPipelineByRemote(String processCode, Long tenantId){
        Map<String, BpmnModel> result;
        try {
            BusinessProcessRemoteService pipelineRemoteService = ApplicationContextHelper.getContext().getBean(BusinessProcessRemoteService.class);
            final long currentLastModifiedTime = ResponseUtils.getResponse(pipelineRemoteService.getProcessLastModifiedTime(tenantId, processCode), Long.class);

            if (!PROCESS_LAST_UPDATE_TIME.containsKey(processCode) || currentLastModifiedTime != PROCESS_LAST_UPDATE_TIME.get(processCode)) {
                updateMemoryCacheTime(processCode, tenantId, currentLastModifiedTime);
            }

            result = CollectionCacheHelper.getCache(BusinessProcessConstants.CacheParam.CACHE_NAME, tenantId.toString(),
                    Collections.singletonList(processCode), pipelines -> {
                        BpmnModel bpmnModel = ResponseUtils.getResponse(pipelineRemoteService.getPipelineByCode(tenantId, processCode), BpmnModel.class);
                        Map<String, BpmnModel> modelMap = new HashMap<>(1);
                        modelMap.put(processCode, bpmnModel);
                        // 存入内存之前先进行合法校验
                        BpmnModelValidator.validate(bpmnModel);
                        return modelMap;
                    });
        }catch (Exception e){
            log.error("metadata->PipelineDriver->getPipelineByRemote error:{}", e.getMessage());
            BusinessProcessRuntimeException processException = new BusinessProcessRuntimeException(BusinessProcessConstants.ErrorMessage.PIPELINE_NETWORK_REQUEST_ERROR, processCode);
            processException.setStackTrace(e.getStackTrace());
            throw processException;
        }
        return result.getOrDefault(processCode, null);
    }

    private static void updateMemoryCacheTime(String processCode, Long tenantId, long currentLastModifiedTime) {
        synchronized (PROCESS_LAST_UPDATE_TIME){
            // 当存在缓存更新时间且 缓存更新时间小于当前更新时间时
            if(PROCESS_LAST_UPDATE_TIME.computeIfAbsent(processCode, a -> currentLastModifiedTime).compareTo(currentLastModifiedTime) < 0){
                // 更新缓存更新时间并清空缓存
                PROCESS_LAST_UPDATE_TIME.put(processCode, currentLastModifiedTime);
                CacheManager cacheManager = ApplicationContextHelper.getContext().getBean(CacheManager.class);
                final Cache cache = cacheManager.getCache(String.format(BusinessProcessConstants.CacheParam.PROCESS_CACHE_KEY, tenantId, processCode));
                if (null != cache) {
                    cache.clear();
                }
            }
        }
    }
}
