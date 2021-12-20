package org.o2.metadata.pipeline.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.o2.metadata.pipeline.app.service.PipelineActionTenantInitService;
import org.o2.metadata.pipeline.app.service.PipelineNodeTenantInitService;
import org.o2.metadata.pipeline.app.service.PipelineTenantInitCoreService;
import org.o2.metadata.pipeline.app.service.PipelineTenantInitService;
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

    public PipelineTenantInitServiceImpl(PipelineTenantInitCoreService pipelineTenantInitCoreService, PipelineActionTenantInitService pipelineActionTenantInitService, PipelineNodeTenantInitService pipelineNodeTenantInitService) {
        this.pipelineTenantInitCoreService = pipelineTenantInitCoreService;
        this.pipelineActionTenantInitService = pipelineActionTenantInitService;
        this.pipelineNodeTenantInitService = pipelineNodeTenantInitService;
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
    }
}
