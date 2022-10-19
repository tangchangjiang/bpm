package org.o2.process.domain.engine.definition;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.o2.process.domain.infra.ProcessEngineConstants;

import java.util.Collections;
import java.util.Map;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/9/28 11:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class BaseNode extends BaseElement {

    @Override
    public void validate(Map<String, BaseElement> elementMap) {
        super.validate(elementMap);
        checkElement(elementMap, getIncoming(), ProcessEngineConstants.FlowElementType.FLOW, ProcessEngineConstants.ErrorCode.NODE_INCOMING_MUST_BE_FLOW_TYPE);
        checkElement(elementMap, getOutgoing(), Collections.singletonList(ProcessEngineConstants.FlowElementType.SEQUENCE_FLOW), ProcessEngineConstants.ErrorCode.NODE_OUTGOING_MUST_BE_SEQUENCE_FLOW);
    }
}
