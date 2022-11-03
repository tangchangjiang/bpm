package org.o2.process.domain.util;


import io.choerodon.core.exception.CommonException;
import org.o2.process.domain.engine.definition.Activity.ServiceTask;
import org.o2.process.domain.engine.definition.BaseElement;
import org.o2.process.domain.engine.definition.event.EndEvent;
import org.o2.process.domain.engine.definition.event.StartEvent;
import org.o2.process.domain.engine.definition.flow.ConditionalFlow;
import org.o2.process.domain.engine.definition.flow.DefaultFlow;
import org.o2.process.domain.engine.definition.flow.RuleConditionVO;
import org.o2.process.domain.engine.definition.flow.SequenceFlow;
import org.o2.process.domain.engine.definition.gateway.ExclusiveGateway;
import org.o2.process.domain.infra.ProcessEngineConstants;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/9/29 17:59
 */
public class BpmnModelUtil {

    public static final Map<String, Class<? extends BaseElement>> CLASS_MAP;

    static{
        HashMap<String, Class<? extends BaseElement>> map = new HashMap<>();
        map.put(ProcessEngineConstants.FlowElementType.START_EVENT, StartEvent.class);
        map.put(ProcessEngineConstants.FlowElementType.END_EVENT, EndEvent.class);
        map.put(ProcessEngineConstants.FlowElementType.EXCLUSIVE_GATEWAY, ExclusiveGateway.class);
        map.put(ProcessEngineConstants.FlowElementType.SERVICE_TASK, ServiceTask.class);
        map.put(ProcessEngineConstants.FlowElementType.SEQUENCE_FLOW, SequenceFlow.class);
        map.put(ProcessEngineConstants.FlowElementType.CONDITIONAL_FLOW, ConditionalFlow.class);
        map.put(ProcessEngineConstants.FlowElementType.DEFAULT_FLOW, DefaultFlow.class);
        CLASS_MAP = Collections.unmodifiableMap(map);
    }

    private BpmnModelUtil(){
        // 工具类，不需要初始化
    }

    public static BaseElement getUniqueNextNode(BaseElement currentElement, Map<String, BaseElement> elementMap) {
        List<String> outgoingKeyList = currentElement.getOutgoing();
        String nextElementKey = outgoingKeyList.stream().findFirst().orElseThrow(() -> new CommonException(ProcessEngineConstants.ErrorCode.ELEMENT_LACK_OUTGOING));
        BaseElement nextFlowElement = elementMap.get(nextElementKey);
        while (ProcessEngineConstants.FlowElementType.FLOW.contains(nextFlowElement.getType())) {
            nextFlowElement = getUniqueNextNode(nextFlowElement, elementMap);
        }
        return nextFlowElement;
    }

    // todo
    public static String buildExpress(RuleConditionVO ruleConditionVO){
        return "";
    }
}
