package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.o2.metadata.console.app.service.*;
import org.o2.metadata.pipeline.app.service.PipelineTenantInitService;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

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

    private final StaticResourceTenantInitService staticResourceTenantInitService;

    private final MallLangPromptTenantInitService mallLangPromptTenantInitService;

    private final PlatformDefineTenantInitServiceImpl platformDefineTenantInitService;

    private final WarehouseTenantInitService warehouseTenantInitService;

    private final PipelineTenantInitService pipelineTenantInitService;


    public MetadataTenantInitServiceImpl(SysParamTenantInitService sysParamTenantInitService,
                                         StaticResourceTenantInitService staticResourceTenantInitService,
                                         MallLangPromptTenantInitService mallLangPromptTenantInitService,
                                         PlatformDefineTenantInitServiceImpl platformDefineTenantInitService,
                                         WarehouseTenantInitService warehouseTenantInitService,
                                         PipelineTenantInitService pipelineTenantInitService) {
        this.sysParamTenantInitService = sysParamTenantInitService;
        this.staticResourceTenantInitService = staticResourceTenantInitService;
        this.mallLangPromptTenantInitService = mallLangPromptTenantInitService;
        this.platformDefineTenantInitService = platformDefineTenantInitService;
        this.warehouseTenantInitService = warehouseTenantInitService;
        this.pipelineTenantInitService = pipelineTenantInitService;
    }

    @Override
    public void tenantInitialize(Long sourceTenantId, Long targetTenantId) {
        // 1. 系统参数
        sysParamTenantInitService.tenantInitialize(sourceTenantId, targetTenantId);

        // 2. 静态资源配置
        staticResourceTenantInitService.tenantInitialize(sourceTenantId, targetTenantId);

        // 3. 多语言文件管理
        mallLangPromptTenantInitService.tenantInitialize(sourceTenantId, targetTenantId);

        // 4. 平台
        platformDefineTenantInitService.tenantInitialize(sourceTenantId, targetTenantId);

        // 5. 仓库（虚拟仓）
        warehouseTenantInitService.tenantInitialize(sourceTenantId, targetTenantId);

        // 6. 流程器
        pipelineTenantInitService.tenantInitialize(sourceTenantId, targetTenantId);

    }

}
