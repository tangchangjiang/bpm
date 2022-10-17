package org.o2.process.domain.engine.process.execute;

import org.o2.process.domain.engine.BusinessProcessExecParam;
import org.o2.process.domain.engine.runtime.ProcessRuntimeContext;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/9/29 17:24
 */
public abstract class BaseElementExecutor<T extends BusinessProcessExecParam> {

    public abstract void execute(ProcessRuntimeContext<T> runtimeContext);

    public abstract String getType();
}
