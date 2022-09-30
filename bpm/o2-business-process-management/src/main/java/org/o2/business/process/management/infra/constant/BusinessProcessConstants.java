package org.o2.business.process.management.infra.constant;

/**
 * @author zhilin.ren@hand-china.com 2022/08/11 16:09
 */
public interface BusinessProcessConstants {

    interface ErrorCode{
        /**
         * 业务流程节点不能为空
         */
        String BUSINESS_PROCESS_NODE_NOT_EMPTY = "o2bpm.process_node_can_not_be_null";
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
}
