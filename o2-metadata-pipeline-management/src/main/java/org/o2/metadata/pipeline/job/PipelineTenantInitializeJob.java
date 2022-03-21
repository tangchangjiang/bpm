package org.o2.metadata.pipeline.job;

import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.o2.initialize.domain.context.TenantInitContext;
import org.o2.initialize.infra.job.O2AbstractTenantInitializeJob;
import org.o2.metadata.pipeline.app.service.PipelineTenantInitService;


import java.util.Collections;

/**
 * description
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/18 13:04
 */
@Slf4j
@JobHandler(value = "pipelineTenantInitializeJob")
public class PipelineTenantInitializeJob extends O2AbstractTenantInitializeJob {

    private final PipelineTenantInitService pipelineTenantInitService;

    public PipelineTenantInitializeJob(PipelineTenantInitService pipelineTenantInitService) {
        this.pipelineTenantInitService = pipelineTenantInitService;
    }

    @Override
    public void initializeBasicData(TenantInitContext context) {
        pipelineTenantInitService.tenantInitialize(context.getSourceTenantId(),context.getTargetTenantId());
    }

    @Override
    public void initializeBusinessData(TenantInitContext context) {

    }
}
