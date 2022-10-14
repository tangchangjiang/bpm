package org.o2.process.domain.engine.process.execute;

import org.o2.process.domain.engine.BusinessProcessExecParam;
import org.o2.process.domain.engine.runtime.ProcessRuntimeContext;
import org.o2.process.domain.infra.ProcessEngineConstants;
import org.springframework.stereotype.Service;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/9/30 16:53
 */
@Service
public class EndEventExecutor<T extends BusinessProcessExecParam> extends BaseNodeExecutor<T>{

    @Override
    protected void doExecute(ProcessRuntimeContext<T> runtimeContext) {
        runtimeContext.setCurrentElement(null);
    }

    @Override
    public String getType() {
        return ProcessEngineConstants.FlowElementType.END_EVENT;
    }
}
