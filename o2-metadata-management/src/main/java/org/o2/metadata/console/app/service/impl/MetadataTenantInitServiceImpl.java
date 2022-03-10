package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.o2.initialize.domain.context.TenantInitContext;
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

    private final StaticResourceTenantInitService staticResourceTenantInitService;

    private final MallLangPromptTenantInitService mallLangPromptTenantInitService;

    private final ShopTenantInitService shopTenantInitService;

    private final OnlineShopRelHouseTenantInitService onlineShopRelHouseTenantInitService;

    private final PlatformDefineTenantInitServiceImpl platformDefineTenantInitService;

    private final CatalogTenantInitService catalogTenantInitService;

    private final PlatformInfoMapTenantInitService platformInfoMapTenantInitService;

    private final WarehouseTenantInitService warehouseTenantInitService;

    private final PosTenantInitService posTenantInitService;

    private final CarrierTenantInitService carrierTenantInitService;

    private final CarrierMappingTenantInitService carrierMappingTenantInitService;

    private final FreightTenantInitService freightTenantInitService;


    public MetadataTenantInitServiceImpl(SysParamTenantInitService sysParamTenantInitService, StaticResourceTenantInitService staticResourceTenantInitService,
                                         MallLangPromptTenantInitService mallLangPromptTenantInitService,
                                         ShopTenantInitService shopTenantInitService, OnlineShopRelHouseTenantInitService onlineShopRelHouseTenantInitService,
                                         PlatformDefineTenantInitServiceImpl platformDefineTenantInitService, CatalogTenantInitService catalogTenantInitService,
                                         PlatformInfoMapTenantInitService platformInfoMapTenantInitService, WarehouseTenantInitService warehouseTenantInitService,
                                         PosTenantInitService posTenantInitService, CarrierTenantInitService carrierTenantInitService,
                                         CarrierMappingTenantInitService carrierMappingTenantInitService, FreightTenantInitService freightTenantInitService) {
        this.sysParamTenantInitService = sysParamTenantInitService;
        this.staticResourceTenantInitService = staticResourceTenantInitService;
        this.mallLangPromptTenantInitService = mallLangPromptTenantInitService;
        this.shopTenantInitService = shopTenantInitService;
        this.onlineShopRelHouseTenantInitService = onlineShopRelHouseTenantInitService;
        this.platformDefineTenantInitService = platformDefineTenantInitService;
        this.catalogTenantInitService = catalogTenantInitService;
        this.platformInfoMapTenantInitService = platformInfoMapTenantInitService;
        this.warehouseTenantInitService = warehouseTenantInitService;
        this.posTenantInitService = posTenantInitService;
        this.carrierTenantInitService = carrierTenantInitService;
        this.carrierMappingTenantInitService = carrierMappingTenantInitService;
        this.freightTenantInitService = freightTenantInitService;
    }

    @Override
    public void tenantInitialize(TenantInitContext context) {
        // long sourceTenantId, List<String> tenantList
        if (null == context.getTargetTenantId()) {
            return;
        }

        // 1. 系统参数
        sysParamTenantInitService.tenantInitialize(context);

        // 2. 静态资源配置
        staticResourceTenantInitService.tenantInitialize(context);

        // 3. 多语言文件管理
        mallLangPromptTenantInitService.tenantInitialize(context);

        // 4. 网店(OW-1)
        shopTenantInitService.tenantInitialize(context);

        // 5. 平台(OW,JD,TM)
        platformDefineTenantInitService.tenantInitialize(context);

        // 6. 目录&目录版本
        catalogTenantInitService.tenantInitialize(context);

        // 7. 平台信息匹配
        platformInfoMapTenantInitService.tenantInitialize(context);

        // 8. 仓库（虚拟仓）
        warehouseTenantInitService.tenantInitialize(context);

    }

    @Override
    public void tenantInitializeBusiness(TenantInitContext context) {
        if (null == context.getTargetTenantId()) {
            return;
        }
        // 1. 系统参数
        sysParamTenantInitService.tenantInitialize(context);

        // 2. 静态资源配置
        staticResourceTenantInitService.tenantInitialize(context);

        // 3. 多语言文件管理
        mallLangPromptTenantInitService.tenantInitialize(context);

        // 1. 保留网店编码为TM-1、JD-1、OW-2、OW-1的网店
        shopTenantInitService.tenantInitializeBusiness(context);

        // 2. 服务点 保留编码为SH001、BJ001的服务点
        posTenantInitService.tenantInitializeBusiness(context);

        // 3. 保留编码为VIRTUAL_POS、SH001、SH002、BJ001的仓库
        warehouseTenantInitService.tenantInitializeBusiness(context);

        // 4. 保留TM-01、JD-1、OW-1、OW-2关联关系
        onlineShopRelHouseTenantInitService.tenantInitializeBusiness(context);

        // 5.保留编码为STO、YTO、SF、EMS、JD的承运商
        carrierTenantInitService.tenantInitializeBusiness(context);

        // 6. 承运商匹配 OW、JD、TM三个平台下全部保留
        carrierMappingTenantInitService.tenantInitializeBusiness(context);

        // 7.目录版本管理
        catalogTenantInitService.tenantInitializeBusiness(context);

        // 8.运费模板
        freightTenantInitService.tenantInitializeBusiness(context);


        // 10.平台定义
        platformDefineTenantInitService.tenantInitializeBusiness(context);

        //9.平台信息匹配
        platformInfoMapTenantInitService.tenantInitializeBusiness(context);

    }
}
