package org.o2.business.process.management.infra.convert;

import org.apache.commons.lang3.StringUtils;
import org.o2.business.process.management.api.vo.interactive.NotationEdge;
import org.o2.process.domain.engine.definition.BaseElement;
import org.o2.process.domain.engine.definition.BaseFlow;
import org.o2.process.domain.engine.definition.flow.ConditionalFlow;
import org.o2.process.domain.engine.definition.flow.DefaultFlow;
import org.o2.process.domain.engine.definition.flow.SequenceFlow;
import org.o2.process.domain.infra.ProcessEngineConstants;

import java.util.Collections;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/10/11 11:38
 */
public class BaseFlowFactory {

    private BaseFlowFactory(){

    }

    public static BaseFlow dealEdge(BaseElement source, BaseElement target, NotationEdge edge){
        source.getOutgoing().add(edge.getId());
        target.getIncoming().add(edge.getId());
        if(ProcessEngineConstants.FlowElementType.EXCLUSIVE_GATEWAY.equals(source.getType())){
            if(null == edge.getData() || StringUtils.isBlank(edge.getData().getRuleCode())){
                return buildDefaultFlow(edge);
            }
            return buildConditionalFlow(edge);
        }
        return buildSequenceFlow(edge);
    }

    protected static BaseFlow buildSequenceFlow(NotationEdge edge){
        SequenceFlow sequenceFlow = new SequenceFlow();
        sequenceFlow.setId(edge.getId());
        sequenceFlow.setIncoming(Collections.singletonList(edge.getSource().getCell()));
        sequenceFlow.setOutgoing(Collections.singletonList(edge.getTarget().getCell()));
        return sequenceFlow;
    }

    protected static BaseFlow buildDefaultFlow(NotationEdge edge){
        DefaultFlow sequenceFlow = new DefaultFlow();
        sequenceFlow.setId(edge.getId());
        sequenceFlow.setIncoming(Collections.singletonList(edge.getSource().getCell()));
        sequenceFlow.setOutgoing(Collections.singletonList(edge.getTarget().getCell()));
        return sequenceFlow;
    }

    protected static BaseFlow buildConditionalFlow(NotationEdge edge){
        ConditionalFlow conditionalFlow = new ConditionalFlow();
        conditionalFlow.setId(edge.getId());
        conditionalFlow.setIncoming(Collections.singletonList(edge.getSource().getCell()));
        conditionalFlow.setOutgoing(Collections.singletonList(edge.getTarget().getCell()));
        conditionalFlow.setRuleCode(edge.getData().getRuleCode());
        conditionalFlow.setPriority(edge.getData().getPriority());
        return conditionalFlow;
    }
}
