package org.o2.process.domain.engine;

public interface ProcessEngine<T extends BusinessProcessExecParam> {

    void startProcess(BpmnModel bpmnModel, T businessParam);

}