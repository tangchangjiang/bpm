package org.o2.metadata.pipeline.constants;

/**
 * 流程管理器Boot常量
 *
 * @author mark.bao@hand-china.com 2019-03-22
 */
public interface PipelineConfConstants {

    interface ErrorMessage {
        String PIPELINE_CODE_NULL = "current pipeline code is NULL";
        String PIPELINE_NULL = "current pipeline[%s] is NULL";
        String PIPELINE_EXEC_PARAM_NULL = "current pipeline[%s] parameters is NULL";
        String PIPELINE_START_ACTION_NULL = "current pipeline[%s] start_action is NULL";
        String PIPELINE_NODE_OUT_RANGE = "pipeline[%s] node[%s, %s], the next_action[%s] not in current pipeline";
        String PIPELINE_NODE_CLOSED_LOOP = "pipeline[%s] node[%s, %s], the next_action[%s] is owner closed loop";
        String PIPELINE_START_NODE_NOT_MATCH = "current pipeline[%s] start_action[%s] cannot match all nodes of this pipeline";
    }

    interface Redis {
        String PIPELINE_KEY = "o2md:pipeline:%d:%s";
        String PIPELINE_NODE_INFO = "info";
    }
}
