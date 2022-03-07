package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.o2.metadata.console.app.service.*;
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
    public void tenantInitialize(long sourceTenantId, List<String> tenantList) {
        if (CollectionUtils.isEmpty(tenantList)) {
            return;
        }

        List<Long> tenantIds = tenantList.stream().map(Long::parseLong).collect(Collectors.toList());
        for (Long tenantId : tenantIds) {
            // 1. 系统参数
            sysParamTenantInitService.tenantInitialize(sourceTenantId, tenantId);

            // 2. 静态资源配置
            staticResourceTenantInitService.tenantInitialize(sourceTenantId, tenantId);

            // 3. 多语言文件管理
            mallLangPromptTenantInitService.tenantInitialize(sourceTenantId, tenantId);

            // 4. 网店(OW-1)
            shopTenantInitService.tenantInitialize(sourceTenantId, tenantId);

            // 5. 平台(OW,JD,TM)
            platformDefineTenantInitService.tenantInitialize(sourceTenantId, tenantId);

            // 6. 目录&目录版本
            catalogTenantInitService.tenantInitialize(sourceTenantId, tenantId);

            // 7. 平台信息匹配
            platformInfoMapTenantInitService.tenantInitialize(sourceTenantId, tenantId);

            // 8. 仓库（虚拟仓）
            warehouseTenantInitService.tenantInitialize(sourceTenantId, tenantId);

        }

    }

    @Override
    public void tenantInitializeBusiness(long sourceTenantId, List<String> tenantList) {
        if (CollectionUtils.isEmpty(tenantList)) {
            return;
        }
        List<Long> tenantIds = tenantList.stream().map(Long::parseLong).collect(Collectors.toList());
        for (Long tenantId : tenantIds) {
            // 1. 保留网店编码为TM-1、JD-1、OW-2、OW-1的网店
            shopTenantInitService.tenantInitializeBusiness(sourceTenantId, tenantId);

            // 2. 服务点 保留编码为SH001、BJ001的服务点
            posTenantInitService.tenantInitializeBusiness(sourceTenantId,tenantId);

            // 3. 保留编码为VIRTUAL_POS、SH001、SH002、BJ001的仓库
            warehouseTenantInitService.tenantInitializeBusiness(sourceTenantId, tenantId);

            // 4. 保留TM-01、JD-1、OW-1、OW-2关联关系
            onlineShopRelHouseTenantInitService.tenantInitializeBusiness(sourceTenantId,tenantId);

            // 5.保留编码为STO、YTO、SF、EMS、JD的承运商
            carrierTenantInitService.tenantInitializeBusiness(sourceTenantId,tenantId);

            // 6. 承运商匹配 OW、JD、TM三个平台下全部保留
            carrierMappingTenantInitService.tenantInitializeBusiness(sourceTenantId,tenantId);

            // 7.目录版本管理
            catalogTenantInitService.tenantInitializeBusiness(sourceTenantId,tenantId);

            // 8.运费模板
            freightTenantInitService.tenantInitializeBusiness(sourceTenantId,tenantId);


            // 10.平台定义
            platformDefineTenantInitService.tenantInitializeBusiness(sourceTenantId,tenantId);

            //9.平台信息匹配
            platformInfoMapTenantInitService.tenantInitializeBusiness(sourceTenantId,tenantId);

        }
    }
}
