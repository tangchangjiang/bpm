package org.o2.metadata.console.app.job;

import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.o2.initialize.domain.context.TenantInitContext;
import org.o2.initialize.infra.job.O2AbstractTenantInitializeJob;
import org.o2.metadata.console.app.service.MetadataBusinessTenantInitService;
import org.o2.metadata.console.app.service.MetadataTenantInitService;

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
    private final MetadataBusinessTenantInitService metadataBusinessTenantInitService;

    public MetadataTenantInitializeJob(MetadataTenantInitService metadataTenantInitService,
                                       MetadataBusinessTenantInitService metadataBusinessTenantInitService) {
        this.metadataTenantInitService = metadataTenantInitService;
        this.metadataBusinessTenantInitService = metadataBusinessTenantInitService;
    }

    @Override
    public void initializeBasicData(TenantInitContext context) {
        metadataTenantInitService.tenantInitialize(context.getSourceTenantId(), context.getTargetTenantId());
    }

    @Override
    public void initializeBusinessData(TenantInitContext context) {
        metadataTenantInitService.tenantInitialize(context.getSourceTenantId(), context.getTargetTenantId());
        metadataBusinessTenantInitService.tenantInitializeBusiness(context);
    }
}
