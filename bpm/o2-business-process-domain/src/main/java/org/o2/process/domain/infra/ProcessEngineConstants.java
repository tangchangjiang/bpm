package org.o2.process.domain.infra;

import com.google.common.collect.ImmutableList;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/9/29 10:10
 */
public interface ProcessEngineConstants {

    interface ErrorCode {

        String INVALID_ELEMENT_TYPE = "o2bpm.error_invalid_element_type";

        String MODEL_EMPTY = "o2bpm.error_empty_model";

        String ELEMENT_KEY_NOT_UNIQUE = "o2bpm.error_element_id_not_unique";
        /**
         * 元素缺乏来源
         */
        String ELEMENT_LACK_INCOMING = "o2bpm.error_element_lack_incoming";
        /**
         * 元素缺乏目标
         */
        String ELEMENT_LACK_OUTGOING = "o2bpm.error_element_lack_outgoing";

        String ELEMENT_TOO_MUCH_OUTGOING = "o2bpm.error_too_many_outgoing";

        String ELEMENT_TOO_MUCH_INCOMING = "o2bpm.error_too_many_incoming";

        String ELEMENT_LACK_CONDITION = "o2bpm.error_element_lack_condition";

        String CONDITION_FLOW_LACK_PRIORITY = "o2bpm.error_condition_flow_lack_priority";

        String ELEMENT_ILLEGAL_VERIFICATION_PARAMETERS = "o2bpm.error_element_verification_parameter_invalid";

        String MODEL_UNKNOWN_ELEMENT_KEY = "o2bpm.error_unknown_element_key";

        String NODE_INCOMING_MUST_BE_FLOW_TYPE = "o2bpm.error_node_incoming_mast_be_flow";

        String NODE_OUTGOING_MUST_BE_SEQUENCE_FLOW = "o2bpm.error_node_outgoing_mast_be_sequenceFlow";

        String GATEWAY_OUTGOING_MUST_BE_GATEWAY_FLOW = "o2bpm.error_gateway_outgoing_mast_be_defaultFlow_or_conditionalFlow";

        String GATEWAY_OUTGOING_DEFAULT_FLOW_MASTER_BE_ONE = "o2bpm.error_gateway_outgoing_master_be_one_of_defaultFlow";

        String SEQUENCE_FLOW_INCOMING_CANNOT_BE_GATEWAY_TYPE = "o2bpm.error_sequence_flow_incoming_cannot_be_type_of_gateway";

        String CONDITIONAL_FLOW_INCOMING_MUST_BE_GATEWAY_TYPE = "o2bpm.error_conditional_Flow_incoming_mast_be_type_of_gateway";

        String DEFAULT_FLOW_INCOMING_MUST_BE_GATEWAY_TYPE = "o2bpm.error_default_Flow_incoming_mast_be_type_of_gateway";

        String FLOW_OUTGOING_MUST_BE_NODE_TYPE = "o2bpm.error_flow_outgoing_mast_be_type_of_node";

        String START_NODE_INVALID = "o2bpm.error_zero_or_more_than_one_start_node";

        String END_NODE_INVALID = "o2bpm.error_no_end_node";

        String END_NODE_CANNOT_HAVE_OUTGOING = "o2bpm.error_end_node_cannot_have_target_element";

        String START_NODE_CANNOT_HAVE_INCOMING = "o2bpm.error_start_node_cannot_have_source_element";

        String UNSUPPORTED_ELEMENT_TYPE = "o2bpm.error_unsupported_element_type";

        String UNSUPPORTED_EXPRESS_TYPE = "o2bpm.error_unsupported_express_type";

        String GET_OUTGOING_FAILED = "o2bpm.error_get_outgoing_failed";

        String BUSINESS_PROCESS_INTERRUPTED = "o2bpm.error_business_process_interrupted";

        String DUPLICATE_PRIORITY = "o2bpm.error_business_process_duplicate_priority";

    }

    interface FlowElementType {
        String END_EVENT = "END_EVENT";
        String EXCLUSIVE_GATEWAY = "EXCLUSIVE_GATEWAY";
        String START_EVENT = "START_EVENT";
        String SERVICE_TASK = "SERVICE_TASK";
        String SEQUENCE_FLOW = "SEQUENCE_FLOW";
        String DEFAULT_FLOW = "DEFAULT_FLOW";
        String CONDITIONAL_FLOW = "CONDITIONAL_FLOW";

        ImmutableList<String> NODE = ImmutableList.of(END_EVENT, EXCLUSIVE_GATEWAY, START_EVENT, SERVICE_TASK);
        ImmutableList<String> FLOW = ImmutableList.of(SEQUENCE_FLOW, DEFAULT_FLOW, CONDITIONAL_FLOW);
        ImmutableList<String> GATEWAY_FLOW = ImmutableList.of(DEFAULT_FLOW, CONDITIONAL_FLOW);
        ImmutableList<String> UN_GATEWAY_NODE = ImmutableList.of(END_EVENT, START_EVENT, SERVICE_TASK);
    }

    interface ExpressType {
        String QL_EXPRESS = "QL_Express";
        String GROOVY = "Groovy";
    }
}
