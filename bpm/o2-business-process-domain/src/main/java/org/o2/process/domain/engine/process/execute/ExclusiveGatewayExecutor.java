package org.o2.process.domain.engine.process.execute;

import io.choerodon.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.o2.core.helper.JsonHelper;
import org.o2.process.domain.engine.BusinessProcessExecParam;
import org.o2.process.domain.engine.definition.BaseElement;
import org.o2.process.domain.engine.definition.flow.ConditionalFlow;
import org.o2.process.domain.engine.definition.gateway.BaseGateway;
import org.o2.process.domain.engine.process.calculator.RuleExpressCalculator;
import org.o2.process.domain.engine.runtime.ProcessRuntimeContext;
import org.o2.process.domain.infra.ProcessEngineConstants;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/9/30 11:39
 */
@Slf4j
@Service
public class ExclusiveGatewayExecutor<T extends BusinessProcessExecParam> extends BaseNodeExecutor<T> {

    private final RuleExpressCalculator ruleExpressCalculator;

    public ExclusiveGatewayExecutor(RuleExpressCalculator ruleExpressCalculator) {
        this.ruleExpressCalculator = ruleExpressCalculator;
    }

    @Override
    public String getType() {
        return ProcessEngineConstants.FlowElementType.EXCLUSIVE_GATEWAY;
    }

    protected void doExecute(ProcessRuntimeContext<T> runtimeContext) {
        BaseElement nextNode = calculateOutgoing((BaseGateway) runtimeContext.getCurrentElement(), runtimeContext);
        runtimeContext.setCurrentElement(nextNode);
    }

    protected BaseElement calculateOutgoing(BaseGateway baseGateway, ProcessRuntimeContext<T> runtimeContext) {
        BaseElement defaultElement = null;

        List<String> outgoingList = baseGateway.getOutgoing();

        List<ConditionalFlow> conditionalFlows = new ArrayList<>();

        for (String outgoingKey : outgoingList) {
            BaseElement outgoingFlow = runtimeContext.getElementMap().get(outgoingKey);
            if(ProcessEngineConstants.FlowElementType.CONDITIONAL_FLOW.equals(outgoingFlow.getType())){
                conditionalFlows.add((ConditionalFlow) outgoingFlow);
            }

            if (ProcessEngineConstants.FlowElementType.DEFAULT_FLOW.equals(outgoingFlow.getType())) {
                defaultElement = outgoingFlow;
            }
        }

        ConditionalFlow hitElement = conditionalFlows.stream()
                .sorted(Comparator.comparing(ConditionalFlow::getPriority))
                .filter(condition -> processCondition(condition, runtimeContext.getBusinessParam(), runtimeContext.getTenantId()))
                .findFirst().orElse(null);

        if(null != hitElement){
            return hitElement;
        }
        //case2 return default while it has is configured
        if (defaultElement != null) {
            log.info("calculateOutgoing: return defaultElement.||nodeKey={} ", baseGateway.getId());
            return defaultElement;
        }

        log.error("calculateOutgoing failed.||nodeId={}, processDetail:{}", baseGateway.getId(), JsonHelper.objectToString(runtimeContext.getBpmnModel()));
        throw new CommonException(ProcessEngineConstants.ErrorCode.GET_OUTGOING_FAILED);
    }

    protected boolean processCondition(ConditionalFlow conditionalFlow, T businessParam, Long tenantId) {

        boolean hitFlag = ruleExpressCalculator.calculate(conditionalFlow.getRuleCode(), businessParam.getRuleObject(), tenantId);

        if(hitFlag){
            afterHit(conditionalFlow, businessParam);
        }
        return hitFlag;
    }

    protected void afterHit(ConditionalFlow conditionalFlow, T businessParam){
        // todo 命中之后额外操作
    }
}
