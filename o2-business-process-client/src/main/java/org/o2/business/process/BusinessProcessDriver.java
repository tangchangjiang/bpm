package org.o2.business.process;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import io.choerodon.core.convertor.ApplicationContextHelper;
import io.choerodon.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.util.ResponseUtils;
import org.o2.business.process.constants.BusinessProcessConstants;
import org.o2.business.process.data.BusinessProcessExecParam;
import org.o2.business.process.domain.BusinessProcessContext;
import org.o2.business.process.domain.BusinessProcessNodeDO;
import org.o2.business.process.exception.BusinessProcessRuntimeException;
import org.o2.business.process.infra.BusinessProcessRemoteService;
import org.o2.business.process.node.BusinessNodeExecutor;
import org.o2.ehcache.util.CollectionCacheHelper;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流水线驱动
 *
 * @author mark.bao@hand-china.com 2018/12/20
 */
@Slf4j
public class BusinessProcessDriver {

    public static <T extends BusinessProcessExecParam> void start(final Long tenantId, final String pipelineCode, final T pipelineNodeParams) {
        final BusinessProcessContext pipeline = getPipelineDetail(tenantId, pipelineCode);
        start(pipeline, pipelineNodeParams);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BusinessProcessExecParam> void start(final BusinessProcessContext pipeline, T pipelineExecParam) {
        final String pipelineCode = pipeline.getPipelineCode();
        if (pipelineExecParam == null) {
            throw new BusinessProcessRuntimeException(BusinessProcessConstants.ErrorMessage.PIPELINE_EXEC_PARAM_NULL, pipelineCode);
        }
        final ApplicationContext applicationContext = ApplicationContextHelper.getContext();
        for(BusinessProcessNodeDO processNodeDO : pipeline.getAllNodeAction()){
            if (StringUtils.isNotBlank(processNodeDO.getScript())) {
                pipelineExecParam = evaluateGroovy(pipelineExecParam, processNodeDO.getScript());
            } else {
                applicationContext.getBean(pipeline.getPipelineCode(), BusinessNodeExecutor.class).run(pipelineExecParam);
            }
        }
    }

    private static <T extends BusinessProcessExecParam> T evaluateGroovy(T pipelineExecParam, String script) {
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
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage());
        }
        assert clazzObj != null;
        return (T) clazzObj.invokeMethod("run", pipelineExecParam);
    }

    public static BusinessProcessContext getPipelineDetail(final Long tenantId, final String pipelineCode) {
        if (StringUtils.isBlank(pipelineCode)) {
            throw new BusinessProcessRuntimeException(BusinessProcessConstants.ErrorMessage.PIPELINE_CODE_NULL);
        }
        final BusinessProcessContext pipeline = getPipelineByRemote(pipelineCode, tenantId);
        pipeline.setPipelineCode(pipelineCode);
        if (pipeline == null) {
            throw new BusinessProcessRuntimeException(BusinessProcessConstants.ErrorMessage.PIPELINE_NULL, pipelineCode);
        }

        final List<BusinessProcessNodeDO> allNodeAction = pipeline.getAllNodeAction();
        for (BusinessProcessNodeDO processNodeDO : allNodeAction) {
            final String currentAction = processNodeDO.getBeanId();
            if (StringUtils.isBlank(currentAction)) {
                continue;
            }
        }
        return pipeline;
    }

    private static BusinessProcessContext getPipelineByRemote(String pipelineCode, Long tenantId){
        Map<String, BusinessProcessContext> result;
        try {
            result = CollectionCacheHelper.getCache(BusinessProcessConstants.CacheParam.CACHE_NAME, tenantId.toString(),
                    Collections.singletonList(pipelineCode), pipelines -> {
                        BusinessProcessRemoteService pipelineRemoteService = ApplicationContextHelper.getContext().getBean(BusinessProcessRemoteService.class);
                        BusinessProcessContext pipeline = ResponseUtils.getResponse(pipelineRemoteService.getPipelineByCode(tenantId, pipelineCode), BusinessProcessContext.class);
                        Map<String, BusinessProcessContext> pipelineMap = new HashMap<>(1);
                        pipelineMap.put(pipelineCode, pipeline);
                        return pipelineMap;
                    });
        }catch (Exception e){
            log.error("metadata->PipelineDriver->getPipelineByRemote error:{}", e.getMessage());
            throw new CommonException(BusinessProcessConstants.ErrorMessage.PIPELINE_NETWORK_REQUEST_ERROR, pipelineCode);
        }
        return result.getOrDefault(pipelineCode, null);
    }
}
