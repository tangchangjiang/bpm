package org.o2.metadata.console.app.job;

import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.o2.initialize.infra.job.O2AbstractTenantInitializeJob;
import org.o2.metadata.console.app.service.MetadataTenantInitService;
import org.o2.metadata.pipeline.app.service.PipelineTenantInitService;


import java.util.Collections;

/**
 * 元数据多租户初始化Job
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/17 10:26
 */
@Slf4j
@JobHandler(value = "metadataTenantInitializeJob")
public class MetadataTenantInitializeJob extends O2AbstractTenantInitializeJob {

    private final MetadataTenantInitService metadataTenantInitService;

    private final PipelineTenantInitService pipelineTenantInitService;

    public MetadataTenantInitializeJob(MetadataTenantInitService metadataTenantInitService, PipelineTenantInitService pipelineTenantInitService) {
        this.metadataTenantInitService = metadataTenantInitService;
        this.pipelineTenantInitService = pipelineTenantInitService;
    }

    @Override
    public void initializeBasicData(Long sourceTenantId, Long targetTenantId) {
        metadataTenantInitService.tenantInitialize(sourceTenantId, Collections.singletonList(String.valueOf(targetTenantId)));
        pipelineTenantInitService.tenantInitialize(sourceTenantId, Collections.singletonList(String.valueOf(targetTenantId)));
    }

    @Override
    public void initializeBusinessData(Long sourceTenantId, Long targetTenantId) {
        // do nothing
    }
}
