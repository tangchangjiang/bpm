package org.o2.business.process.management.infra.constant;

/**
 * @author zhilin.ren@hand-china.com 2022/08/11 16:09
 */
public interface BusinessProcessConstants {

    interface ErrorCode {
        /**
         * 业务流程节点不能为空
         */
        String BUSINESS_PROCESS_NODE_NOT_EMPTY = "o2bpm.process_node_can_not_be_null";

        String UNSUPPORTED_DRAWING_TYPE = "o2bpm.unsupported_drawing_type";

        String NO_CORRESPONDING_NODE_FOUND = "o2bpm.no_corresponding_node_found";

    }

    interface ErrorMessage {
        String PARAM_CODE_NOT_EXISTS = "current beanId:%s, paramCode:%s not exists paramCodes:%s";

        String BEAN_NETWORK_REQUEST_ERROR = "current beanId[%s] request metadata error";

        String PARAM_PARSING_ERROR = "current beanId:%s, paramCode:%s, paramValue:%s";
    }

    interface LovCode {

        /**
         * 一级业务类型
         */
        String BUSINESS_TYPE_CODE = "O2BPM.BUSINESS_TYPE";

        /**
         * 二级业务类型
         */
        String SUB_BUSINESS_TYPE_CODE = "O2BPM.SUB_BUSINESS_TYPE";

        /**
         * 参数格式
         */
        String PARAM_FORMAT = "O2BPM.PARAM_FORMAT";

        /**
         * 编辑类型
         */
        String PARAM_EDIT_TYPE = "O2BPM.PARAM_EDIT_TYPE";
    }

    interface CellType {
        String FLOW_EDGE = "flow-edge";
        String BRANCH_NODE = "branch-node";
        String END_NODE = "end-node";
        String FLOW_NODE = "flow-node";
        String START_NODE = "start-node";
    }

    interface CacheParam {
        String CACHE_NAME = "O2MD_USER";
        String PROCESS_CACHE_KEY = "%d_%s";
        String NODE_PARAM_DEFINITION_CACHE = "biz_param_definition_%d_%s";
    }

    interface BizParam {
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
