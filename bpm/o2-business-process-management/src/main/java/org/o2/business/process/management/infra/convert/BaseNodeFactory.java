package org.o2.business.process.management.infra.convert;

import io.choerodon.core.exception.CommonException;
import org.o2.business.process.management.api.vo.interactive.NotationNode;
import org.o2.business.process.management.infra.constant.BusinessProcessConstants;
import org.o2.process.domain.engine.definition.Activity.ServiceTask;
import org.o2.process.domain.engine.definition.BaseNode;
import org.o2.process.domain.engine.definition.event.EndEvent;
import org.o2.process.domain.engine.definition.event.StartEvent;
import org.o2.process.domain.engine.definition.gateway.ExclusiveGateway;

import java.util.ArrayList;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/10/11 10:47
 */
public class BaseNodeFactory {

    private BaseNodeFactory(){

    }

    public static BaseNode createBaseNode(NotationNode node){
        switch (node.getShape()){
            case BusinessProcessConstants.CellType.START_NODE:
                return buildStartEvent(node);
            case BusinessProcessConstants.CellType.FLOW_NODE:
                return buildServiceTask(node);
            case BusinessProcessConstants.CellType.BRANCH_NODE:
                return buildExclusiveGateway(node);
            case BusinessProcessConstants.CellType.END_NODE:
                return buildEndEvent(node);
            default:
                throw new CommonException(BusinessProcessConstants.ErrorCode.UNSUPPORTED_DRAWING_TYPE);
        }
    }

    public static BaseNode buildStartEvent(NotationNode node){
        StartEvent startEvent = new StartEvent();
        startEvent.setId(node.getId());
        startEvent.setIncoming(new ArrayList<>());
        startEvent.setOutgoing(new ArrayList<>());
        return startEvent;
    }

    public static BaseNode buildServiceTask(NotationNode node){
        ServiceTask serviceTask = new ServiceTask();
        serviceTask.setId(node.getId());
        serviceTask.setEnabledFlag(node.getData().getEnabledFlag());
        serviceTask.setBeanId(node.getData().getBeanId());
        serviceTask.setArgs(node.getData().getArgs());
        serviceTask.setIncoming(new ArrayList<>());
        serviceTask.setOutgoing(new ArrayList<>());
        return serviceTask;
    }

    public static BaseNode buildExclusiveGateway(NotationNode node){
        ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
        exclusiveGateway.setId(node.getId());
        exclusiveGateway.setIncoming(new ArrayList<>());
        exclusiveGateway.setOutgoing(new ArrayList<>());
        return exclusiveGateway;
    }

    public static BaseNode buildEndEvent(NotationNode node){
        EndEvent endEvent = new EndEvent();
        endEvent.setId(node.getId());
        endEvent.setIncoming(new ArrayList<>());
        endEvent.setOutgoing(new ArrayList<>());
        return endEvent;
    }
}
