package org.o2.metadata.pipeline.api.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 流程器
 *
 * @author wenjun.deng01@hand-china.com 2019-07-22
 */
@Data
public class PipelineDTO {
    /**
     * 流程器编码
     */
    private String code;
    /**
     * 流程器描述
     */
    private String description;
    /**
     * 开始节点
     */
    private Long startAction;
    /**
     * 结束节点
     */
    private Long endAction;
    /**
     * 所有节点详情
     */
    private Map<String, PipelineNodeDTO> pipelineNodes;
    /**
     * 是否启用
     */
    private Integer activeFlag;

    /**
     * 租户id
     */
    private Long tenantId;


    public PipelineDTO() {
        this.pipelineNodes = new HashMap<>();
    }
}
