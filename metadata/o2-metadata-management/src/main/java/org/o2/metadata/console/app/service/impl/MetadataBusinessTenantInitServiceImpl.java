package org.o2.metadata.console.app.service.impl;

import io.choerodon.core.exception.CommonException;
import org.apache.logging.log4j.util.Strings;
import org.hzero.core.base.BaseConstants;
import org.o2.initialize.domain.context.TenantInitContext;
import org.o2.metadata.console.app.bo.TenantInitBO;
import org.o2.metadata.console.app.service.*;
import org.springframework.stereotype.Service;

@Service
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
        String warehouseCode = context.getParamMap().get("warehouseCode");
        String onlineShopCode = context.getParamMap().get("onlineShopCode");
        String carrierCode = context.getParamMap().get("carrierCode");
        boolean flag = (Strings.isBlank(warehouseCode) || Strings.isBlank(onlineShopCode) || Strings.isBlank(carrierCode));
        if (flag) {
            throw new CommonException("job param is null");
        }
        String[] onlineShops = onlineShopCode.split(BaseConstants.Symbol.COMMA);
        TenantInitBO bo = new TenantInitBO();
        bo.setSourceTenantId(sourceTenantId);
        bo.setTargetTenantId(targetTenantId);
        bo.setCarrierCode(carrierCode);
        bo.setWarehouseCode(warehouseCode);
        for (String code : onlineShops) {
            bo.setOnlineShopCode(code);
            shopTenantInitService.tenantInitializeBusiness(bo);
        }
    }
}
