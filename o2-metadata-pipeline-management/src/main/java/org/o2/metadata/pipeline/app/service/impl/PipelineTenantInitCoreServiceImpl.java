package org.o2.metadata.pipeline.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.pipeline.app.service.PipelineTenantInitCoreService;
import org.o2.metadata.pipeline.domain.entity.Pipeline;
import org.o2.metadata.pipeline.domain.repository.PipelineRepository;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * description
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/18 13:26
 */
@Slf4j
@Service
public class PipelineTenantInitCoreServiceImpl implements PipelineTenantInitCoreService {

    private final PipelineRepository pipelineRepository;

    public PipelineTenantInitCoreServiceImpl(PipelineRepository pipelineRepository) {
        this.pipelineRepository = pipelineRepository;
    }

    @Override
    public void tenantInitialize(Long tenantId) {
        log.info("initializePipeline start, tenantId[{}]", tenantId);
        // 1. 查询平台级租户
        final List<Pipeline> platformPipelines = pipelineRepository.selectByCondition(Condition.builder(Pipeline.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(Pipeline.FIELD_TENANT_ID, BaseConstants.DEFAULT_TENANT_ID)
                        .andEqualTo(Pipeline.FIELD_ACTIVE_FLAG, BaseConstants.Flag.YES))
                .build());

        if (CollectionUtils.isEmpty(platformPipelines)) {
            log.info("pipeline is empty.");
            return;
        }

        // 2. 查询目标租户是否存在数据
        final List<Pipeline> targetPipelines = pipelineRepository.selectByCondition(Condition.builder(Pipeline.class)
                .andWhere(Sqls.custom().andEqualTo(Pipeline.FIELD_TENANT_ID, tenantId))
                .build());

        if (CollectionUtils.isNotEmpty(targetPipelines)) {
            // 删除目标租户原数据
            pipelineRepository.batchDeleteByPrimaryKey(targetPipelines);
        }

        // 3. 插入目标租户
        platformPipelines.forEach(pipeline -> {
            pipeline.setId(null);
            pipeline.setTenantId(tenantId);
        });
        pipelineRepository.batchInsert(platformPipelines);
    }
}
