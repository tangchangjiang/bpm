package org.o2.process.domain.infra;

import com.google.common.collect.ImmutableList;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/9/29 10:10
 */
public interface ProcessEngineConstants {

    interface ErrorCode{

        String INVALID_ELEMENT_TYPE = "invalid element type";

        String MODEL_EMPTY = "Empty model";

        String ELEMENT_KEY_NOT_UNIQUE = "Element id not unique";
        /**
         * 元素缺乏来源
         */
        String ELEMENT_LACK_INCOMING = "Element lack incoming";
        /**
         * 元素缺乏目标
         */
        String ELEMENT_LACK_OUTGOING = "Element lack outgoing";

        String ELEMENT_TOO_MUCH_OUTGOING = "Too many outgoing";

        String ELEMENT_TOO_MUCH_INCOMING = "Too many incoming";

        String ELEMENT_LACK_CONDITION = "Element lack condition";

        String ELEMENT_ILLEGAL_VERIFICATION_PARAMETERS = "The element verification parameter is invalid";

        String MODEL_UNKNOWN_ELEMENT_KEY = "Unknown element key";

        String NODE_INCOMING_MUST_BE_FLOW_TYPE = "Node incoming mast be type of flow";

        String NODE_OUTGOING_MUST_BE_SEQUENCE_FLOW = "Node outgoing mast be sequenceFlow";

        String GATEWAY_OUTGOING_MUST_BE_GATEWAY_FLOW = "Gateway outgoing mast be defaultFlow or conditionalFlow";

        String GATEWAY_OUTGOING_DEFAULT_FLOW_CANNOT_BE_GREATER_THAN_ONE = "Gateway outgoing defaultFlow cannot be greater than 1";

        String SEQUENCE_FLOW_INCOMING_CANNOT_BE_GATEWAY_TYPE = "Sequence flow incoming cannot be type of gateway";

        String CONDITIONAL_FLOW_INCOMING_MUST_BE_GATEWAY_TYPE = "Conditional Flow incoming mast be type of gateway";

        String DEFAULT_FLOW_INCOMING_MUST_BE_GATEWAY_TYPE = "Default Flow incoming mast be type of gateway";

        String FLOW_OUTGOING_MUST_BE_NODE_TYPE = "Flow outgoing mast be type of node";

        String START_NODE_INVALID = "Zero or more than one start node";

        String END_NODE_INVALID = "No end node";

        String UNSUPPORTED_ELEMENT_TYPE = "Unsupported element type";

        String UNSUPPORTED_EXPRESS_TYPE = "Unsupported express type";

        String GET_OUTGOING_FAILED = "Get outgoing failed";

        String BUSINESS_PROCESS_INTERRUPTED = "business process interrupted";

    }

    interface FlowElementType{
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

    interface ExpressType{
        String QL_EXPRESS = "QL_Express";
        String GROOVY = "Groovy";
    }
}
