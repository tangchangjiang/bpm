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
        String PARAM_DEFINITION_NOT_EXISTS = "current beanId[%s] paramCode[%s] can not find definition";
        String PARAM_PARSING_ERROR = "current beanId:%s, paramCode:%s, paramValue:%s";
        String PARAM_TYPE_NOT_SUPPORTED = "current beanId:%s, paramCode:%s unsupported param Type";
    }

    interface CacheParam{
        String CACHE_NAME = "O2MD_USER";
        String PROCESS_CACHE_KEY = "%d_%s";
        String NODE_PARAM_DEFINITION_CACHE = "biz_param_definition_%d_%s";
    }

    interface BizParam{
        /**
         * 日期选择框
         */
        String DATE_PICKER = "DATEPICKER";

        /**
         * 日期时间选择框
         */
        String DATE_TIME_PICKER = "DATETIMEPICKER";
        /**
         * 数字框
         */
        String INPUT_NUMBER = "INPUTNUMBER";
        /**
         * 值集视图
         */
        String LOV = "LOV";
        /**
         * 下拉框
         */
        String COMBO_BOX = "COMBOBOX";

        /**
         * 文本
         */
        String INPUT = "INPUT";

    }

}
