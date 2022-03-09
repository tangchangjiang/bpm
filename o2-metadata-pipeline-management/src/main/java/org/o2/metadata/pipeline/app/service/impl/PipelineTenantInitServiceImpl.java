package org.o2.metadata.pipeline.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.core.helper.JsonHelper;
import org.o2.initialize.domain.context.TenantInitContext;
import org.o2.metadata.pipeline.api.vo.PipelineCreatedResultVO;
import org.o2.metadata.pipeline.app.service.PipelineService;
import org.o2.metadata.pipeline.app.service.PipelineTenantInitCoreService;
import org.o2.metadata.pipeline.app.service.PipelineTenantInitService;
import org.o2.metadata.pipeline.domain.entity.Pipeline;
import org.o2.metadata.pipeline.domain.repository.PipelineRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 流程器租户初始化
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/18 13:07
 */
@Slf4j
@Service
public class PipelineTenantInitServiceImpl implements PipelineTenantInitService {

    private final PipelineTenantInitCoreService pipelineTenantInitCoreService;

    private final PipelineRepository pipelineRepository;

    private final PipelineService pipelineService;

    public PipelineTenantInitServiceImpl(PipelineTenantInitCoreService pipelineTenantInitCoreService,
                                         PipelineRepository pipelineRepository,
                                         PipelineService pipelineService) {
        this.pipelineTenantInitCoreService = pipelineTenantInitCoreService;
        this.pipelineRepository = pipelineRepository;
        this.pipelineService = pipelineService;
    }

    @Override
    public void tenantInitialize(TenantInitContext context) {
        if (null == context.getTargetTenantId()) {
            return;
        }
        final List<Long> tenantIds = Collections.singletonList(context.getTargetTenantId());

        for (Long tenantId : tenantIds) {
            // 1. 初始化流程器
            pipelineTenantInitCoreService.tenantInitialize(context.getSourceTenantId(), tenantId);
        }

        // 流程器缓存同步
        tenantIds.forEach(targetTenantId -> {
            final List<Pipeline> cachePipelines = pipelineRepository.selectByCondition(Condition.builder(Pipeline.class)
                    .andWhere(Sqls.custom()
                            .andEqualTo(Pipeline.FIELD_TENANT_ID, targetTenantId)
                            .andEqualTo(Pipeline.FIELD_ACTIVE_FLAG, BaseConstants.Flag.YES))
                    .build());
            final List<PipelineCreatedResultVO> pipelineCreatedResultVOList;
            try {
                pipelineCreatedResultVOList = pipelineService.batchMerge(cachePipelines);
                log.info("cache pipelines for targetTenantId[{}],results[{}]", targetTenantId, JsonHelper.objectToString(pipelineCreatedResultVOList));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });

    }
}
