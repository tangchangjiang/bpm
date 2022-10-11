package org.o2.metadata.pipeline.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 流程器VO
 *
 * @author mark.bao@hand-china.com 2019-03-22
 */
@Data
public class PipelineVO {
    private String pipelineCode;
    private String startBeanId;
    private String endBeanId;
    private String startScript;
    private String endScript;
    private final Set<String> allNodeAction;
    private final Map<String, PipelineNodeVO> pipelineNodes;

    public PipelineVO() {
        this.allNodeAction = new HashSet<>();
        this.pipelineNodes = new HashMap<>();
    }
}
