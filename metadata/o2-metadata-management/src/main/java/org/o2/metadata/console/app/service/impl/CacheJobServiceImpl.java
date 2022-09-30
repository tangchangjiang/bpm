package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.message.MessageAccessor;
import org.o2.metadata.console.app.service.CacheJobService;
import org.o2.metadata.console.infra.constant.OnlineShopConstants;
import org.o2.metadata.console.infra.constant.SystemParameterConstants;
import org.o2.metadata.console.infra.entity.*;
import org.o2.metadata.console.infra.redis.*;
import org.o2.metadata.console.infra.repository.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 *
 * job
 *
 * @author yipeng.zhu@hand-china.com 2021-08-12
 **/
@Slf4j
@Component
public class CacheJobServiceImpl implements CacheJobService {

    private final OnlineShopRelWarehouseRepository onlineShopRelWarehouseRepository;
    private final SystemParameterRepository systemParameterRepository;
    private final SystemParameterRedis systemParameterRedis;
    private final CarrierRedis carrierRedis;
    private final OnlineShopRedis onlineShopRedis;
    private final FreightRedis freightRedis;
    private final FreightTemplateRepository freightTemplateRepository;
    private final FreightTemplateDetailRepository freightTemplateDetailRepository;
    private final WarehouseRedis warehouseRedis;
    private final OnlineShopRepository onlineShopRepository;
    private final PosRedis posRedis;

    public CacheJobServiceImpl(OnlineShopRelWarehouseRepository onlineShopRelWarehouseRepository,
                               SystemParameterRepository systemParameterRepository,
                               SystemParameterRedis systemParameterRedis,
                               CarrierRedis carrierRedis,
                               OnlineShopRedis onlineShopRedis,
                               FreightRedis freightRedis, FreightTemplateRepository freightTemplateRepository,
                               FreightTemplateDetailRepository freightTemplateDetailRepository,
                               WarehouseRedis warehouseRedis, OnlineShopRepository onlineShopRepository,
                               PosRedis posRedis) {
        this.onlineShopRelWarehouseRepository = onlineShopRelWarehouseRepository;
        this.systemParameterRepository = systemParameterRepository;
        this.systemParameterRedis = systemParameterRedis;
        this.carrierRedis = carrierRedis;
        this.onlineShopRedis = onlineShopRedis;
        this.freightRedis = freightRedis;
        this.freightTemplateRepository = freightTemplateRepository;
        this.freightTemplateDetailRepository = freightTemplateDetailRepository;
        this.warehouseRedis = warehouseRedis;
        this.onlineShopRepository = onlineShopRepository;
        this.posRedis = posRedis;
    }

    @Override
    public void refreshWarehouse(Long tenantId) {
        warehouseRedis.batchUpdateWarehouse(null, tenantId);

    }

    @Override
    public void refreshOnlineShopRelWarehouse(Long tenantId) {
        List<OnlineShopRelWarehouse> list = onlineShopRelWarehouseRepository.queryAllShopRelWarehouseByTenantId(tenantId);
        onlineShopRedis.batchUpdateShopRelWh(list,tenantId, OnlineShopConstants.Redis.UPDATE);
    }

    @Override
    public void refreshSysParameter(Long tenantId) {
        // 获取系统参数
        List<SystemParameter> systemParameterList = systemParameterRepository.queryInitData(tenantId);
        if (CollectionUtils.isEmpty(systemParameterList)) {
            log.warn(MessageAccessor.getMessage(SystemParameterConstants.Message.SYSTEM_PARAMETER_NOT_FOUND).desc());
            return;
        }
        for (SystemParameter systemParameter : systemParameterList) {
            Set<SystemParamValue> systemParamValues = systemParameter.getSetSystemParamValue();
            if (CollectionUtils.isEmpty(systemParameterList)) {
                break;
            }
            for (SystemParamValue value : systemParamValues) {
                value.set_token(null);
                value.setFlex(null);
            }
        }
        systemParameterRedis.synToRedis(systemParameterList, tenantId);
    }

    @Override
    public void refreshCarrier(Long tenantId) {
        carrierRedis.batchUpdateRedis(tenantId);
    }

    @Override
    public void refreshFreight(Long tenantId) {
       List<FreightTemplate> freightTemplateList = freightTemplateRepository.selectAllByTenantId(tenantId);
       List<FreightTemplateDetail> details = freightTemplateDetailRepository.selectAllByTenantId(tenantId);
       freightRedis.batchUpdateRedis(freightTemplateList,details,tenantId);
    }

    @Override
    public void refreshOnlineShop(Long tenantId) {
        OnlineShop query = new OnlineShop();
        query.setTenantId(tenantId);
        query.setActiveFlag(BaseConstants.Flag.YES);
        List<OnlineShop> onlineShops = onlineShopRepository.select(query);
        onlineShopRedis.batchUpdateRedis(onlineShops,tenantId);
    }

    @Override
    public void refreshPos(Long tenantId) {
        posRedis.syncPosToRedis(null, tenantId);
    }
}
