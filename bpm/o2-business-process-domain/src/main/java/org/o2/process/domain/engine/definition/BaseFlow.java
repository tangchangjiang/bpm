package org.o2.process.domain.engine.definition;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.o2.process.domain.infra.ProcessEngineConstants;

import java.util.Map;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/9/28 16:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class BaseFlow extends BaseElement {

    @Override
    protected void checkIncoming(Map<String, BaseElement> elementMap) {
        super.checkIncoming(elementMap);
        if (getIncoming().size() > 1) {
            throwElementValidatorException(ProcessEngineConstants.ErrorCode.ELEMENT_TOO_MUCH_INCOMING);
        }
    }

    @Override
    public void validate(Map<String, BaseElement> elementMap) {
        this.checkIncoming(elementMap);
        super.checkOutgoing(elementMap);
        checkTerminal(elementMap);
    }

    protected void checkTerminal(Map<String, BaseElement> elementMap) {
        checkElement(elementMap, getOutgoing(), ProcessEngineConstants.FlowElementType.NODE,
                ProcessEngineConstants.ErrorCode.FLOW_OUTGOING_MUST_BE_NODE_TYPE);
    }
}
