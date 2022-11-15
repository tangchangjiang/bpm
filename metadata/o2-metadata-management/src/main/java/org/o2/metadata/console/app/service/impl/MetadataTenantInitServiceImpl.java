package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.o2.business.process.management.app.service.ProcessTenantInitService;
import org.o2.metadata.console.app.service.*;
import org.springframework.stereotype.Service;

/**
 * description
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/17 10:45
 */
@Slf4j
@Service
public class MetadataTenantInitServiceImpl implements MetadataTenantInitService {

    private final SysParamTenantInitService sysParamTenantInitService;

    private final PlatformDefineTenantInitServiceImpl platformDefineTenantInitService;

    private final WarehouseTenantInitService warehouseTenantInitService;

    private final ProcessTenantInitService processTenantInitService;

    public MetadataTenantInitServiceImpl(SysParamTenantInitService sysParamTenantInitService,
                                         PlatformDefineTenantInitServiceImpl platformDefineTenantInitService,
                                         WarehouseTenantInitService warehouseTenantInitService, ProcessTenantInitService processTenantInitService) {
        this.sysParamTenantInitService = sysParamTenantInitService;
        this.platformDefineTenantInitService = platformDefineTenantInitService;
        this.warehouseTenantInitService = warehouseTenantInitService;
        this.processTenantInitService = processTenantInitService;
    }

    @Override
    public void tenantInitialize(Long sourceTenantId, Long targetTenantId) {
        // 1. 系统参数
        sysParamTenantInitService.tenantInitialize(sourceTenantId, targetTenantId);

        // 2. 平台
        platformDefineTenantInitService.tenantInitialize(sourceTenantId, targetTenantId);

        // 3. 仓库（虚拟仓）
        warehouseTenantInitService.tenantInitialize(sourceTenantId, targetTenantId);

        // 4. 业务流程
        processTenantInitService.tenantInitialize(sourceTenantId, targetTenantId);

    }

}
