package org.o2.business.process.constants;

/**
 * 流程管理器Boot常量
 *
 * @author mark.bao@hand-china.com 2019-03-22
 */
public interface BusinessProcessConstants {

    interface ErrorMessage {
        String PIPELINE_CODE_NULL = "current pipeline code is NULL";
        String BUSINESS_PROCESS_NULL = "current pipeline[%s] is NULL";
        String PIPELINE_EXEC_PARAM_NULL = "current pipeline[%s] parameters is NULL";
        String PIPELINE_START_ACTION_NULL = "current pipeline[%s] start_action is NULL";
        String PIPELINE_NODE_OUT_RANGE = "pipeline[%s] node[%s, %s], the next_action[%s] not in current pipeline";
        String PIPELINE_NODE_CLOSED_LOOP = "pipeline[%s] node[%s, %s], the next_action[%s] is owner closed loop";
        String PIPELINE_START_NODE_NOT_MATCH = "current pipeline[%s] start_action[%s] cannot match all nodes of this pipeline";
        String BUSINESS_PROCESS_ERROR = "current pipeline[%s] runtime exception";
        String PIPELINE_NETWORK_REQUEST_ERROR = "current pipeline[%s] request metadata error";
    }

    interface CacheParam{
        String CACHE_NAME = "O2MD_USER";
        String PROCESS_CACHE_KEY = "%d_%s";
    }

}
