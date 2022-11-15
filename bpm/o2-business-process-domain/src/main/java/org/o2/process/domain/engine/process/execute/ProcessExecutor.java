package org.o2.process.domain.engine.process.execute;

import io.choerodon.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.o2.process.domain.engine.BpmnModel;
import org.o2.process.domain.engine.BusinessProcessExecParam;
import org.o2.process.domain.engine.ProcessEngine;
import org.o2.process.domain.engine.definition.BaseElement;
import org.o2.process.domain.engine.process.provider.ExecutorProvider;
import org.o2.process.domain.engine.runtime.ProcessRuntimeContext;
import org.o2.process.domain.infra.ProcessEngineConstants;
import org.o2.process.domain.util.BpmnModelUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/9/29 11:18
 */
@Slf4j
@Service
public class ProcessExecutor<T extends BusinessProcessExecParam> implements ProcessEngine<T> {

    private final ExecutorProvider<T> executorProvider;

    public ProcessExecutor(ExecutorProvider<T> executorProvider) {
        this.executorProvider = executorProvider;
    }

    @Override
    public void startProcess(BpmnModel bpmnModel, T businessParam) {
        ProcessRuntimeContext<T> runtimeContext = new ProcessRuntimeContext<>();
        preData(runtimeContext, businessParam, bpmnModel);
        doExecute(runtimeContext);
        postExecute(runtimeContext);
    }

    protected void preData(ProcessRuntimeContext<T> runtimeContext, T businessParam, BpmnModel bpmnModel) {
        Map<String, BaseElement> elementMap = new HashMap<>();
        for (BaseElement flowElement : bpmnModel.getFlowElements()) {
            elementMap.put(flowElement.getId(), BpmnModelUtil.CLASS_MAP.get(flowElement.getType()).cast(flowElement));
        }
        runtimeContext.setBpmnModel(bpmnModel);
        runtimeContext.setElementMap(elementMap);
        runtimeContext.setBusinessParam(businessParam);
        runtimeContext.setTenantId(bpmnModel.getTenantId());
        initCurrentFlowElement(runtimeContext);
    }

    protected void initCurrentFlowElement(ProcessRuntimeContext<T> runtimeContext) {
        BaseElement startElement = runtimeContext.getBpmnModel().getAllNodes()
                .stream().filter(e -> ProcessEngineConstants.FlowElementType.START_EVENT.equals(e.getType())).findFirst()
                .orElseThrow(() -> new CommonException(ProcessEngineConstants.ErrorCode.START_NODE_INVALID));
        runtimeContext.setCurrentElement(startElement);
    }

    protected void doExecute(ProcessRuntimeContext<T> runtimeContext) {
        BaseElementExecutor<T> runtimeExecutor = executorProvider.getElementExecutor(runtimeContext.getCurrentElement());
        while (runtimeExecutor != null) {
            runtimeExecutor.execute(runtimeContext);
            runtimeExecutor = executorProvider.getElementExecutor(runtimeContext.getCurrentElement());
        }
    }

    protected void postExecute(ProcessRuntimeContext<T> runtimeContext) {

    }
}
