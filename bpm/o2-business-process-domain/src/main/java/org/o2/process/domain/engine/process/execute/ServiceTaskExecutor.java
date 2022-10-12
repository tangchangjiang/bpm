package org.o2.process.domain.engine.process.execute;

import io.choerodon.core.convertor.ApplicationContextHelper;
import io.choerodon.core.exception.CommonException;
import org.o2.process.domain.engine.BusinessProcessExecParam;
import org.o2.process.domain.engine.definition.Activity.ServiceTask;
import org.o2.process.domain.engine.runtime.ProcessRuntimeContext;
import org.o2.process.domain.engine.runtime.ServiceAction;
import org.o2.process.domain.infra.ProcessEngineConstants;
import org.springframework.stereotype.Service;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/9/30 16:55
 */
@Service
public class ServiceTaskExecutor<T extends BusinessProcessExecParam> extends BaseNodeExecutor<T> {

    @Override
    protected void doExecute(ProcessRuntimeContext<T> runtimeContext) {
        ServiceTask serviceTask = (ServiceTask) runtimeContext.getCurrentElement();
        ServiceAction<T> action = ApplicationContextHelper.getContext().getBean(serviceTask.getBeanId(), ServiceAction.class);

        action.beforeExecution(runtimeContext.getBusinessParam());

        action.run(runtimeContext.getBusinessParam());

        action.afterExecution(runtimeContext.getBusinessParam());

        if(Boolean.FALSE.equals(runtimeContext.getBusinessParam().getNextFlag())){
            if(runtimeContext.getBusinessParam().getException() != null){
                throw new CommonException(runtimeContext.getBusinessParam().getException());
            }
            throw new CommonException(ProcessEngineConstants.ErrorCode.BUSINESS_PROCESS_INTERRUPTED);
        }

        super.doExecute(runtimeContext);
    }

    @Override
    public String getType() {
        return ProcessEngineConstants.FlowElementType.SERVICE_TASK;
    }
}
