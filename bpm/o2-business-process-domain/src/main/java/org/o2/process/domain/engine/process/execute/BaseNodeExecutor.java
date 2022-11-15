package org.o2.process.domain.engine.process.execute;

import lombok.extern.slf4j.Slf4j;
import org.o2.process.domain.engine.BusinessProcessExecParam;
import org.o2.process.domain.engine.definition.BaseElement;
import org.o2.process.domain.engine.runtime.ProcessRuntimeContext;
import org.o2.process.domain.util.BpmnModelUtil;

@Slf4j
public abstract class BaseNodeExecutor<T extends BusinessProcessExecParam> extends BaseElementExecutor<T> {

    @Override
    public void execute(ProcessRuntimeContext<T> runtimeContext) {

        preExecute(runtimeContext);

        doExecute(runtimeContext);

        postExecute(runtimeContext);
    }

    /**
     * Init runtimeContext: update currentNodeInstance
     * 1.currentNodeInfo(nodeInstance & nodeKey): currentNode is this.model
     * 2.sourceNodeInfo(nodeInstance & nodeKey): sourceNode is runtimeContext.currentNodeInstance
     */
    protected void preExecute(ProcessRuntimeContext<T> runtimeContext) {
    }

    protected void doExecute(ProcessRuntimeContext<T> runtimeContext) {
        BaseElement nextNode = BpmnModelUtil.getUniqueNextNode(runtimeContext.getCurrentElement(), runtimeContext.getElementMap());
        runtimeContext.setCurrentElement(nextNode);
    }

    protected void postExecute(ProcessRuntimeContext<T> runtimeContext) {
    }
}
