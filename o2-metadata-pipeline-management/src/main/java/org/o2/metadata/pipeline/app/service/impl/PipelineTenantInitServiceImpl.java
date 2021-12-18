package org.o2.metadata.pipeline.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.o2.metadata.pipeline.app.service.PipelineActionTenantInitService;
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


    public PipelineTenantInitServiceImpl(PipelineTenantInitCoreService pipelineTenantInitCoreService, PipelineActionTenantInitService pipelineActionTenantInitService) {
        this.pipelineTenantInitCoreService = pipelineTenantInitCoreService;
        this.pipelineActionTenantInitService = pipelineActionTenantInitService;
    }

    @Override
    public void tenantInitialize(List<String> tenantList) {
        if (CollectionUtils.isEmpty(tenantList)) {
            return;
        }
        final List<Long> tenantIds = tenantList.stream().map(Long::parseLong).collect(Collectors.toList());

        for (Long tenantId : tenantIds) {
            // 1. 初始化流程器
            pipelineTenantInitCoreService.tenantInitialize(tenantId);

            // 2. 初始化行为定义
            pipelineActionTenantInitService.tenantInitialize(tenantId);
        }
    }
}
