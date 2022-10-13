package org.o2.business.process;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import io.choerodon.core.convertor.ApplicationContextHelper;
import io.choerodon.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.util.ResponseUtils;
import org.o2.business.process.constants.BusinessProcessConstants;
import org.o2.business.process.data.BusinessProcessExecParam;
import org.o2.business.process.domain.BusinessProcessBO;
import org.o2.business.process.domain.BusinessProcessNodeDO;
import org.o2.business.process.exception.BusinessProcessRuntimeException;
import org.o2.business.process.exception.ProcessErrorHandler;
import org.o2.business.process.infra.BusinessProcessRemoteService;
import org.o2.business.process.node.BusinessNodeExecutor;
import org.o2.core.O2CoreConstants;
import org.o2.core.helper.JsonHelper;
import org.o2.cache.util.CollectionCacheHelper;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
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
public class BusinessProcessDriver {

    private BusinessProcessDriver(){
        // 禁止构造该类
    }

    private final static Map<String, Long> PROCESS_LAST_UPDATE_TIME = new HashMap<>();

    public static <T extends BusinessProcessExecParam> void start(final Long tenantId, final String pipelineCode, final T pipelineNodeParams) {
        final BusinessProcessBO pipeline = getPipelineDetail(tenantId, pipelineCode);
        start(pipeline, pipelineNodeParams);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BusinessProcessExecParam> void start(final BusinessProcessBO processBO, T processExecParam) {
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
            for (BusinessProcessNodeDO processNodeDO : processBO.getAllNodeAction()) {
                stopWatch.start(processNodeDO.getBeanId());
                if (StringUtils.isNotBlank(processNodeDO.getScript())) {
                    processExecParam = evaluateGroovy(processExecParam, processNodeDO.getScript());
                } else if (null != processNodeDO.getEnabledFlag() && processNodeDO.getEnabledFlag() == O2CoreConstants.BooleanFlag.ENABLE) {
                    BusinessNodeExecutor<T> nodeExecutor = applicationContext.getBean(processNodeDO.getBeanId(), BusinessNodeExecutor.class);
                    if (StringUtils.isNotBlank(processNodeDO.getArgs()) && JsonHelper.stringToMap(processNodeDO.getArgs()) != null) {
                        processExecParam.setCurrentParam(JsonHelper.stringToMap(processNodeDO.getArgs()));
                    }
                    nodeExecutor.beforeExecution(processExecParam);
                    nodeExecutor.run(processExecParam);
                    nodeExecutor.afterExecution(processExecParam);

                    if (Boolean.FALSE.equals(processExecParam.getNextFlag())) {
                        processErrorHandel(processExecParam, processCode, applicationContext);
                        break;
                    }
                    // 清空当前节点参数
                    processExecParam.getCurrentParam().clear();
                }
                stopWatch.stop();
            }
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
        Map<String, ProcessErrorHandler> processErrorHandleMap = applicationContext.getBeansOfType(ProcessErrorHandler.class)
                .values().stream().collect(Collectors.toMap(ProcessErrorHandler::getProcessCode, Function.identity(), (a, b) -> b));
        ProcessErrorHandler<T> processErrorHandler = processErrorHandleMap.getOrDefault(processCode, processErrorHandleMap.get(ProcessErrorHandler.DEFAULT));
        processErrorHandler.errorHandle(processCode, processExecParam);
    }

    private static <T extends BusinessProcessExecParam> T evaluateGroovy(T processExecParam, String script) {
        ClassLoader parent = ClassLoader.getSystemClassLoader();
        Class<?> clazz = null;
        try (GroovyClassLoader loader = new GroovyClassLoader(parent)) {
            clazz = loader.parseClass(script);
        } catch (IOException e) {
          log.error(e.getMessage());
        }
        GroovyObject clazzObj = null;
        try {
            clazzObj = (GroovyObject) clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException | NullPointerException e) {
            log.error(e.getMessage());
        }
        assert clazzObj != null;
        return (T) clazzObj.invokeMethod("run", processExecParam);
    }

    public static BusinessProcessBO getPipelineDetail(final Long tenantId, final String processCode) {
        if (StringUtils.isBlank(processCode)) {
            throw new BusinessProcessRuntimeException(BusinessProcessConstants.ErrorMessage.PIPELINE_CODE_NULL);
        }
        final BusinessProcessBO pipeline = getPipelineByRemote(processCode, tenantId);
        if (pipeline == null) {
            throw new BusinessProcessRuntimeException(BusinessProcessConstants.ErrorMessage.PIPELINE_NULL, processCode);
        }
        if (log.isDebugEnabled()) {
            log.debug("[processConfig], BusinessProcessContext:{}", JsonHelper.objectToString(pipeline));
        }
        if(CollectionUtils.isEmpty(pipeline.getAllNodeAction())){
            pipeline.setAllNodeAction(Collections.emptyList());
        }
        pipeline.setAllNodeAction(pipeline.getAllNodeAction().stream().sorted(Comparator.comparing(BusinessProcessNodeDO::getSequenceNum)).collect(Collectors.toList()));
        pipeline.setProcessCode(processCode);
        return pipeline;
    }

    private static BusinessProcessBO getPipelineByRemote(String processCode, Long tenantId){
        Map<String, BusinessProcessBO> result;
        try {
            BusinessProcessRemoteService pipelineRemoteService = ApplicationContextHelper.getContext().getBean(BusinessProcessRemoteService.class);
            final long currentLastModifiedTime = ResponseUtils.getResponse(pipelineRemoteService.getProcessLastModifiedTime(tenantId, processCode), Long.class);
            // 清空缓存
            if (!PROCESS_LAST_UPDATE_TIME.containsKey(processCode) || currentLastModifiedTime != PROCESS_LAST_UPDATE_TIME.get(processCode)) {
                updateMemoryCacheTime(processCode, tenantId, currentLastModifiedTime);
            }

            result = CollectionCacheHelper.getCache(BusinessProcessConstants.CacheParam.CACHE_NAME, tenantId.toString(),
                    Collections.singletonList(processCode), pipelines -> {
                        BusinessProcessBO pipeline = ResponseUtils.getResponse(pipelineRemoteService.getPipelineByCode(tenantId, processCode), BusinessProcessBO.class);
                        Map<String, BusinessProcessBO> pipelineMap = new HashMap<>(1);
                        pipelineMap.put(processCode, pipeline);
                        return pipelineMap;
                    });
        }catch (Exception e){
            log.error("metadata->PipelineDriver->getPipelineByRemote error:{}", e.getMessage());
            CommonException commonException = new CommonException(BusinessProcessConstants.ErrorMessage.PIPELINE_NETWORK_REQUEST_ERROR, processCode);
            commonException.setStackTrace(e.getStackTrace());
            throw commonException;
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
