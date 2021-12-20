package org.o2.metadata.pipeline.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.core.helper.JsonHelper;
import org.o2.metadata.pipeline.api.vo.PipelineCreatedResultVO;
import org.o2.metadata.pipeline.app.service.*;
import org.o2.metadata.pipeline.domain.entity.Pipeline;
import org.o2.metadata.pipeline.domain.repository.PipelineRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

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

    private final PipelineActionTenantInitService pipelineActionTenantInitService;

    private final PipelineNodeTenantInitService pipelineNodeTenantInitService;

    private final PipelineRepository pipelineRepository;

    private final PipelineService pipelineService;

    public PipelineTenantInitServiceImpl(PipelineTenantInitCoreService pipelineTenantInitCoreService, PipelineActionTenantInitService pipelineActionTenantInitService, PipelineNodeTenantInitService pipelineNodeTenantInitService, PipelineRepository pipelineRepository, PipelineService pipelineService) {
        this.pipelineTenantInitCoreService = pipelineTenantInitCoreService;
        this.pipelineActionTenantInitService = pipelineActionTenantInitService;
        this.pipelineNodeTenantInitService = pipelineNodeTenantInitService;
        this.pipelineRepository = pipelineRepository;
        this.pipelineService = pipelineService;
    }

    @Override
    public void tenantInitialize(long sourceTenantId, List<String> tenantList) {
        if (CollectionUtils.isEmpty(tenantList)) {
            return;
        }
        final List<Long> tenantIds = tenantList.stream().map(Long::parseLong).collect(Collectors.toList());

        for (Long tenantId : tenantIds) {
            // 1. 初始化流程器
            pipelineTenantInitCoreService.tenantInitialize(sourceTenantId, tenantId);

            // 2. 初始化行为定义
            pipelineActionTenantInitService.tenantInitialize(sourceTenantId, tenantId);

            // 3. 初始化流程器节点
            pipelineNodeTenantInitService.tenantInitialize(sourceTenantId, tenantId);
        }

        // 流程器缓存同步
        final List<Pipeline> cachePipelines = pipelineRepository.selectByCondition(Condition.builder(Pipeline.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(Pipeline.FIELD_TENANT_ID, sourceTenantId)
                        .andEqualTo(Pipeline.FIELD_ACTIVE_FLAG, BaseConstants.Flag.YES))
                .build());
        final List<PipelineCreatedResultVO> pipelineCreatedResultVOList;
        try {
            pipelineCreatedResultVOList = pipelineService.batchMerge(cachePipelines);
            log.info("cache pipelines for sourceTenantId[{}],results[{}]", sourceTenantId, JsonHelper.objectToString(pipelineCreatedResultVOList));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
