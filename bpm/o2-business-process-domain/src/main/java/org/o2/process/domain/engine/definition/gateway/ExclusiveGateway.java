package org.o2.process.domain.engine.definition.gateway;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.o2.process.domain.infra.ProcessEngineConstants;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/9/28 15:58
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExclusiveGateway extends BaseGateway {

    @Override
    public String getType() {
        return ProcessEngineConstants.FlowElementType.EXCLUSIVE_GATEWAY;
    }

}
