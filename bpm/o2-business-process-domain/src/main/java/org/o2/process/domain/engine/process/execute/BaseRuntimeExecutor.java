package org.o2.process.domain.engine.process.execute;

import org.o2.process.domain.engine.BusinessProcessExecParam;
import org.o2.process.domain.engine.runtime.ProcessRuntimeContext;



public abstract class BaseRuntimeExecutor<T extends BusinessProcessExecParam> {

    public abstract void execute(ProcessRuntimeContext<T>  runtimeContext);

    protected abstract  boolean isCompleted(ProcessRuntimeContext<T>  runtimeContext);

    protected abstract BaseRuntimeExecutor getExecuteExecutor(ProcessRuntimeContext<T>  runtimeContext);

    public abstract String getType();
}
