package org.o2.process.domain.engine.definition.flow;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.o2.process.domain.engine.definition.BaseElement;
import org.o2.process.domain.engine.definition.BaseFlow;
import org.o2.process.domain.infra.ProcessEngineConstants;

import java.util.Collections;
import java.util.Map;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/9/28 16:36
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ConditionalFlow extends BaseFlow {

    private String ruleCode;

    private Integer priority;

    @Override
    public String getType() {
        return ProcessEngineConstants.FlowElementType.CONDITIONAL_FLOW;
    }

    @Override
    public void validate(Map<String, BaseElement> elementMap) {
        super.validate(elementMap);
        if (StringUtils.isBlank(ruleCode)) {
            throwElementValidatorException(ProcessEngineConstants.ErrorCode.ELEMENT_LACK_CONDITION);
        }

        if (null == priority) {
            throwElementValidatorException(ProcessEngineConstants.ErrorCode.CONDITION_FLOW_LACK_PRIORITY);
        }
    }

    @Override
    protected void checkTerminal(Map<String, BaseElement> elementMap) {
        checkElement(elementMap, getIncoming(), Collections.singletonList(ProcessEngineConstants.FlowElementType.EXCLUSIVE_GATEWAY),
                ProcessEngineConstants.ErrorCode.CONDITIONAL_FLOW_INCOMING_MUST_BE_GATEWAY_TYPE);
        super.checkTerminal(elementMap);
    }
}
