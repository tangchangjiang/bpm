package org.o2.business.process.management.app.job;

import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.o2.business.process.management.app.service.ProcessTenantInitService;
import org.o2.initialize.domain.context.TenantInitContext;
import org.o2.initialize.infra.job.O2AbstractTenantInitializeJob;

/**
 * description
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/18 13:04
 */
@Slf4j
@JobHandler(value = "bizTenantInitializeJob")
public class BusinessProcessTenantInitializeJob extends O2AbstractTenantInitializeJob {

    private final ProcessTenantInitService processTenantInitService;

    public BusinessProcessTenantInitializeJob(ProcessTenantInitService processTenantInitService) {
        this.processTenantInitService = processTenantInitService;
    }

    @Override
    public void initializeBasicData(TenantInitContext context) {
        processTenantInitService.tenantInitialize(context.getSourceTenantId(), context.getTargetTenantId());
    }

    @Override
    public void initializeBusinessData(TenantInitContext context) {

    }
}
