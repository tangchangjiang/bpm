package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.o2.metadata.console.app.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

    private final ShopTenantInitService shopTenantInitService;

    private final PlatformDefineTenantInitServiceImpl platformDefineTenantInitService;

    public MetadataTenantInitServiceImpl(SysParamTenantInitService sysParamTenantInitService, StaticResourceTenantInitService staticResourceTenantInitService, MallLangPromptTenantInitService mallLangPromptTenantInitService, ShopTenantInitService shopTenantInitService, PlatformDefineTenantInitServiceImpl platformDefineTenantInitService) {
        this.sysParamTenantInitService = sysParamTenantInitService;
        this.staticResourceTenantInitService = staticResourceTenantInitService;
        this.mallLangPromptTenantInitService = mallLangPromptTenantInitService;
        this.shopTenantInitService = shopTenantInitService;
        this.platformDefineTenantInitService = platformDefineTenantInitService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void tenantInitialize(List<String> tenantList) {
        if (CollectionUtils.isEmpty(tenantList)) {
            return;
        }

        List<Long> tenantIds = tenantList.stream().map(Long::parseLong).collect(Collectors.toList());
        for (Long tenantId : tenantIds) {
            // 1. 系统参数
            sysParamTenantInitService.tenantInitialize(tenantId);

            // 2. 静态资源配置
            staticResourceTenantInitService.tenantInitialize(tenantId);

            // 3. 多语言文件管理
            mallLangPromptTenantInitService.tenantInitialize(tenantId);

            // 4. 网店(OW-1)
            shopTenantInitService.tenantInitialize(tenantId);

            // 5. 平台(OW,JD,TM)
            platformDefineTenantInitService.tenantInitialize(tenantId);

        }

    }
}
