package org.o2.process.domain.engine.definition.gateway;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;
import org.o2.process.domain.engine.definition.BaseElement;
import org.o2.process.domain.engine.definition.BaseNode;
import org.o2.process.domain.infra.ProcessEngineConstants;

import java.util.Map;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/9/28 15:20
 */

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class BaseGateway extends BaseNode {

    @Override
    protected void checkOutgoing(Map<String, BaseElement> elementMap) {
        if (CollectionUtils.isEmpty(getOutgoing())) {
            throwElementValidatorException(ProcessEngineConstants.ErrorCode.ELEMENT_LACK_OUTGOING);
        }
    }

    /**
     * 网关校验规则：
     * 1. incoming必须是线,数量大于0
     * 2. outgoing 必须是缺省线或者条件线, 数量大于0
     * 3. 缺省线不能大于1
     */
    @Override
    public void validate(Map<String, BaseElement> elementMap) {
        checkIncoming(elementMap);
        checkOutgoing(elementMap);
        checkElement(elementMap, getIncoming(), ProcessEngineConstants.FlowElementType.FLOW, ProcessEngineConstants.ErrorCode.NODE_INCOMING_MUST_BE_FLOW_TYPE);
        checkElement(elementMap, getOutgoing(), ProcessEngineConstants.FlowElementType.GATEWAY_FLOW, ProcessEngineConstants.ErrorCode.GATEWAY_OUTGOING_MUST_BE_GATEWAY_FLOW);
        checkDefaultFlow(elementMap);
    }

    protected void checkDefaultFlow(Map<String, BaseElement> elementMap){
        int defaultFlowQuantity = 0;
        for(String node : getOutgoing()){
            if(ProcessEngineConstants.FlowElementType.DEFAULT_FLOW.equals(elementMap.get(node).getType())){
                defaultFlowQuantity++;
            }
        }
        if(defaultFlowQuantity != 1){
            throwElementValidatorException(ProcessEngineConstants.ErrorCode.GATEWAY_OUTGOING_DEFAULT_FLOW_MASTER_BE_ONE);
        }
    }
}
