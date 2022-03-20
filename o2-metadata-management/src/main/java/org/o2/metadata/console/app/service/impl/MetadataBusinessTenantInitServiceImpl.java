package org.o2.metadata.console.app.service.impl;

import org.o2.core.helper.JsonHelper;
import org.o2.initialize.domain.context.TenantInitContext;
import org.o2.metadata.console.app.bo.TenantInitBO;
import org.o2.metadata.console.app.service.*;

import java.util.List;

public class MetadataBusinessTenantInitServiceImpl implements MetadataBusinessTenantInitService {

    private final ShopTenantInitService shopTenantInitService;


    public MetadataBusinessTenantInitServiceImpl(
                                                 ShopTenantInitService shopTenantInitService) {

        this.shopTenantInitService = shopTenantInitService;
    }

    @Override
    public void tenantInitializeBusiness(TenantInitContext context) {
        long sourceTenantId = context.getSourceTenantId();
        long targetTenantId = context.getTargetTenantId();
        String tenantInitParam = context.getParamMap().get("tenantInitParam");
        List<TenantInitBO> list = JsonHelper.stringToArray(tenantInitParam,TenantInitBO.class);
        for (TenantInitBO bo : list) {
            bo.setSourceTenantId(sourceTenantId);
            bo.setTargetTenantId(targetTenantId);
            shopTenantInitService.tenantInitializeBusiness(bo);
        }

    }
}
