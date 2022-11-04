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
 * @date 2022/9/28 13:36
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EndEvent extends BaseEvent {

    @Override
    public void checkOutgoing(Map<String, BaseElement> elementMap) {
        if(CollectionUtils.isNotEmpty(getOutgoing())){
            throwElementValidatorException(ProcessEngineConstants.ErrorCode.END_NODE_CANNOT_HAVE_OUTGOING);
        }
    }

    @Override
    public String getType() {
        return ProcessEngineConstants.FlowElementType.END_EVENT;
    }
}
