package org.o2.metadata.pipeline;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import io.choerodon.core.convertor.ApplicationContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.util.ResponseUtils;
import org.o2.core.O2CoreConstants;
import org.o2.metadata.pipeline.constants.PipelineConfConstants;
import org.o2.metadata.pipeline.data.PipelineExecParam;
import org.o2.metadata.pipeline.exception.PipelineRuntimeException;
import org.o2.metadata.pipeline.infra.PipelineRemoteService;
import org.o2.metadata.pipeline.node.PipelineNodeExecutor;
import org.o2.metadata.pipeline.vo.PipelineNodeVO;
import org.o2.metadata.pipeline.vo.PipelineVO;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * 流水线驱动
 *
 * @author mark.bao@hand-china.com 2018/12/20
 */
@Slf4j
public class PipelineDriver {


    public static <T extends PipelineExecParam> void start(final Long tenantId, final String pipelineCode, final T pipelineNodeParams) {
        final PipelineVO pipeline = getPipelineDetail(tenantId, pipelineCode);
        start(pipeline, pipelineNodeParams);
    }

    @SuppressWarnings("unchecked")
    public static <T extends PipelineExecParam> void start(final PipelineVO pipeline, T pipelineExecParam) {
        final String pipelineCode = pipeline.getPipelineCode();
        if (pipelineExecParam == null) {
            throw new PipelineRuntimeException(PipelineConfConstants.ErrorMessage.PIPELINE_EXEC_PARAM_NULL, pipelineCode);
        }
        final ApplicationContext applicationContext = ApplicationContextHelper.getContext();
        String script = pipeline.getStartScript();
        String runningAction = pipeline.getStartBeanId();
        while (true) {
            if (StringUtils.isNotBlank(script)) {
                pipelineExecParam = evaluateGroovy(pipelineExecParam, script);
            } else {
                applicationContext.getBean(runningAction, PipelineNodeExecutor.class).run(pipelineExecParam);
            }
            if (O2CoreConstants.PipelineStrategy.END.equalsIgnoreCase(pipelineExecParam.getNextStrategy())) {
                break;
            }
            final String runningNodeId = PipelineNodeVO.uniqueKey(runningAction, pipelineExecParam.getNextStrategy());
            final PipelineNodeVO curPipelineNode = pipeline.getPipelineNodes().get(runningNodeId);
            if (curPipelineNode == null || StringUtils.isBlank(curPipelineNode.getNextBeanId())) {
                break;
            }
            runningAction = curPipelineNode.getNextBeanId();
            script = curPipelineNode.getScript();
        }
        if (StringUtils.isNotBlank(pipeline.getEndBeanId())) {
            applicationContext.getBean(pipeline.getEndBeanId(), PipelineNodeExecutor.class).run(pipelineExecParam);
        }
        if (StringUtils.isNotEmpty(pipeline.getEndScript())) {
            evaluateGroovy(pipelineExecParam, script);
        }
    }

    private static <T extends PipelineExecParam> T evaluateGroovy(T pipelineExecParam, String script) {
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

    public static PipelineVO getPipelineDetail(final Long tenantId, final String pipelineCode) {
        if (StringUtils.isBlank(pipelineCode)) {
            throw new PipelineRuntimeException(PipelineConfConstants.ErrorMessage.PIPELINE_CODE_NULL);
        }
        PipelineRemoteService pipelineRemoteService = ApplicationContextHelper.getContext().getBean(PipelineRemoteService.class);
        final PipelineVO pipeline = ResponseUtils.getResponse(pipelineRemoteService.getPipelineByCode(tenantId, pipelineCode), PipelineVO.class);
        if (pipeline == null || MapUtils.isEmpty(pipeline.getPipelineNodes())) {
            throw new PipelineRuntimeException(PipelineConfConstants.ErrorMessage.PIPELINE_NULL, pipelineCode);
        }
        if (StringUtils.isBlank(pipeline.getStartBeanId())) {
            throw new PipelineRuntimeException(PipelineConfConstants.ErrorMessage.PIPELINE_START_ACTION_NULL, pipelineCode);
        }

        PipelineNodeVO startNode = null;
        final Set<String> allNodeAction = pipeline.getAllNodeAction();
        for (Map.Entry<String, PipelineNodeVO> var : pipeline.getPipelineNodes().entrySet()) {
            final PipelineNodeVO pipelineNode = var.getValue();
            if (pipeline.getStartBeanId().equalsIgnoreCase(pipelineNode.getCurBeanId())) {
                startNode = pipelineNode;
            }

            final String nextAction = pipelineNode.getNextBeanId();
            if (StringUtils.isBlank(nextAction)) {
                continue;
            }
            if (!allNodeAction.contains(nextAction)) {
                throw new PipelineRuntimeException(PipelineConfConstants.ErrorMessage.PIPELINE_NODE_OUT_RANGE, pipelineCode, pipelineNode.getNodeId(), pipelineNode.getCurBeanId(), nextAction);
            }
            if (nextAction.equalsIgnoreCase(pipelineNode.getCurBeanId())) {
                throw new PipelineRuntimeException(PipelineConfConstants.ErrorMessage.PIPELINE_NODE_CLOSED_LOOP, pipelineCode, pipelineNode.getNodeId(), pipelineNode.getCurBeanId(), nextAction);
            }
        }
        if (startNode == null) {
            throw new PipelineRuntimeException(PipelineConfConstants.ErrorMessage.PIPELINE_START_NODE_NOT_MATCH, pipelineCode, pipeline.getStartBeanId());
        }

        return pipeline;
    }
}
