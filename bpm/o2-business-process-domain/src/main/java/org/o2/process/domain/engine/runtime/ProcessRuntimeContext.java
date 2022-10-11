package org.o2.process.domain.engine.runtime;

import lombok.Data;
import org.o2.process.domain.engine.BpmnModel;
import org.o2.process.domain.engine.BusinessProcessExecParam;
import org.o2.process.domain.engine.definition.BaseElement;

import java.util.Map;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/9/29 10:38
 */
@Data
public class ProcessRuntimeContext<T extends BusinessProcessExecParam> {

    private BpmnModel bpmnModel;

    private T businessParam;

    private Map<String, BaseElement> elementMap;

    private BaseElement currentElement;
}
