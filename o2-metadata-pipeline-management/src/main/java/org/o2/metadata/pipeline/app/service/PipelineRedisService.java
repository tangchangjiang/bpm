package org.o2.metadata.pipeline.app.service;


/**
 * 流程器Redis操作
 *
 * @author wenjun.deng@hand-china.com 2019-3-28
 */
public interface PipelineRedisService {

    /**
     * 创建流程器配置信息
     *
     * @param tenantId   租户ID
     * @param pipelineId 流程器节点配置
     */
    void createRedisPipelineNodeConf(final Long pipelineId, Long tenantId);

    /**
     * 保存流程器配置信息 由更新流程器节点触发
     *
     * @param tenantId   租户ID
     * @param pipelineId 流程器配置
     * @return 保存结果
     */
    boolean saveRedisPipelineNodeConf(final Long pipelineId, Long tenantId);

    /**
     * 删除流程器信息
     *
     * @param pipelineId 流程器配置
     */
    void delRedisPipelineConf(final String pipelineId);

    /**
     * 查询流程器缓存信息
     *
     * @param pipelineCode 流程器配置代码
     * @return 流程器缓存信息
     */
    String getPipelineConf(final String pipelineCode);
}
