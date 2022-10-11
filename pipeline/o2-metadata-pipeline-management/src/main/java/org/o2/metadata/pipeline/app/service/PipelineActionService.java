package org.o2.metadata.pipeline.app.service;


import org.o2.metadata.pipeline.domain.entity.PipelineAction;

/**
 * 流程器行为应用服务
 *
 * @author wenjun.deng01@hand-china.com 2019-12-16 10:36:04
 */
public interface PipelineActionService {

    /**
     * 存储行为动作
     *
     * @param pipelineAction 行为
     * @param organizationId 租户ID
     */
    void savePipelineAction(PipelineAction pipelineAction, Long organizationId);

    /**
     * 更新Pipeline(或新建ActionParameter)
     *
     * @param pipelineAction 行为
     * @param organizationId 租户ID
     */
    void updatePipelineAction(PipelineAction pipelineAction, Long organizationId);

    /**
     * 删除Pipeline
     *
     * @param pipelineAction 行为
     * @param organizationId 租户ID
     */
    void delete(PipelineAction pipelineAction, Long organizationId);

}
