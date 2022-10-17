package org.o2.process.domain.engine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.o2.process.domain.engine.definition.BaseElement;
import org.o2.process.domain.infra.ProcessEngineConstants;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/9/29 10:32
 */
@Data
public class BpmnModel {

    private List<BaseElement> flowElements;

    private Integer enabledFlag;

    private Long tenantId;

    private String processCode;

    private String processName;

    @JsonIgnore
    public List<BaseElement> getAllNodes() {
        if(CollectionUtils.isEmpty(flowElements)){
            return Collections.emptyList();
        }
        return flowElements.stream().filter(e -> ProcessEngineConstants.FlowElementType.NODE.contains(e.getType())).collect(Collectors.toList());
    }
}
