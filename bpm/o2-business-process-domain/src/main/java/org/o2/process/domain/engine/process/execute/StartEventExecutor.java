package org.o2.process.domain.engine.process.execute;

import org.o2.process.domain.engine.BusinessProcessExecParam;
import org.o2.process.domain.infra.ProcessEngineConstants;
import org.springframework.stereotype.Service;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/9/30 16:53
 */
@Service
public class StartEventExecutor<T extends BusinessProcessExecParam> extends BaseNodeExecutor<T> {

    @Override
    public String getType() {
        return ProcessEngineConstants.FlowElementType.START_EVENT;
    }
}
