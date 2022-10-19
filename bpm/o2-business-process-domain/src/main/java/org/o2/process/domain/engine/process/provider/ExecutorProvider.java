package org.o2.process.domain.engine.process.provider;

import io.choerodon.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.o2.process.domain.engine.BusinessProcessExecParam;
import org.o2.process.domain.engine.definition.BaseElement;
import org.o2.process.domain.engine.process.execute.BaseElementExecutor;
import org.o2.process.domain.engine.runtime.ProcessRuntimeContext;
import org.o2.process.domain.infra.ProcessEngineConstants;
import org.o2.process.domain.util.BpmnModelUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ExecutorProvider<T extends BusinessProcessExecParam> {

    private final Map<String, BaseElementExecutor<T>> EXECUTOR_MAP = new HashMap<>();

    public ExecutorProvider(List<BaseElementExecutor<T>> baseRuntimeExecutors){
         init(baseRuntimeExecutors);
    }

    protected void init(List<BaseElementExecutor<T>> baseRuntimeExecutors){
        baseRuntimeExecutors.forEach(executor -> EXECUTOR_MAP.put(executor.getType(), executor));
    }

    public BaseElementExecutor<T> getElementExecutor(BaseElement flowElement) {
        // 结束节点执行完为空，断开循环
        if(flowElement == null){
            return null;
        }

        String elementType = flowElement.getType();
        BaseElementExecutor<T> elementExecutor = EXECUTOR_MAP.get(elementType);

        if (elementExecutor == null) {
            log.error("getElementExecutor failed: unsupported elementType.|elementType={}", elementType);
            throw new CommonException(ProcessEngineConstants.ErrorCode.UNSUPPORTED_ELEMENT_TYPE);
        }
        return elementExecutor;
    }


    public BaseElementExecutor<T> getNextNodeExecutor(ProcessRuntimeContext<T> runtimeContext) {
        if(ProcessEngineConstants.FlowElementType.END_EVENT.equals(runtimeContext.getCurrentElement().getType())){
            return null;
        }
        BaseElement flowElement = BpmnModelUtil.getUniqueNextNode(runtimeContext.getCurrentElement(), runtimeContext.getElementMap());
        runtimeContext.setCurrentElement(flowElement);
        return EXECUTOR_MAP.get(flowElement.getType());
    }
}
