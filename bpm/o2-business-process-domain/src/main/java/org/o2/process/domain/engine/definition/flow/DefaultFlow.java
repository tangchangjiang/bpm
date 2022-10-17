package org.o2.process.domain.engine.definition.flow;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.o2.process.domain.engine.definition.BaseElement;
import org.o2.process.domain.engine.definition.BaseFlow;
import org.o2.process.domain.infra.ProcessEngineConstants;

import java.util.Collections;
import java.util.Map;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/9/28 16:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultFlow extends BaseFlow {

    @Override
    public String getType() {
        return ProcessEngineConstants.FlowElementType.DEFAULT_FLOW;
    }


    @Override
    protected void checkTerminal(Map<String, BaseElement> elementMap) {
        checkElement(elementMap, getIncoming(), Collections.singletonList(ProcessEngineConstants.FlowElementType.EXCLUSIVE_GATEWAY), ProcessEngineConstants.ErrorCode.DEFAULT_FLOW_INCOMING_MUST_BE_GATEWAY_TYPE);
        super.checkTerminal(elementMap);
    }
}
