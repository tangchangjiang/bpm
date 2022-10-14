package org.o2.process.domain.engine.definition.flow;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.o2.process.domain.engine.definition.BaseElement;
import org.o2.process.domain.engine.definition.BaseFlow;
import org.o2.process.domain.infra.ProcessEngineConstants;

import java.util.Map;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/9/28 16:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SequenceFlow extends BaseFlow {

    @Override
    public String getType() {
        return ProcessEngineConstants.FlowElementType.SEQUENCE_FLOW;
    }


    @Override
    protected void checkTerminal(Map<String, BaseElement> elementMap) {
        checkElement(elementMap, getIncoming(), ProcessEngineConstants.FlowElementType.UN_GATEWAY_NODE, ProcessEngineConstants.ErrorCode.SEQUENCE_FLOW_INCOMING_CANNOT_BE_GATEWAY_TYPE);
        super.checkTerminal(elementMap);
    }
}
