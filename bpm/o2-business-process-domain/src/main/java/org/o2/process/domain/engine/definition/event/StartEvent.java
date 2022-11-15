package org.o2.process.domain.engine.definition.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;
import org.o2.process.domain.engine.definition.BaseElement;
import org.o2.process.domain.infra.ProcessEngineConstants;

import java.util.Map;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/9/28 11:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StartEvent extends BaseEvent {

    @Override
    protected void checkIncoming(Map<String, BaseElement> elementMap) {
        if (CollectionUtils.isNotEmpty(getIncoming())) {
            throwElementValidatorException(ProcessEngineConstants.ErrorCode.START_NODE_CANNOT_HAVE_INCOMING);
        }
    }

    @Override
    public String getType() {
        return ProcessEngineConstants.FlowElementType.START_EVENT;
    }
}
