package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.core.message.MessageAccessor;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.console.app.service.CacheJobService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.constant.OnlineShopConstants;
import org.o2.metadata.console.infra.constant.SystemParameterConstants;
import org.o2.metadata.console.infra.entity.*;
import org.o2.metadata.console.infra.redis.*;
import org.o2.metadata.console.infra.repository.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * job
 *
 * @author yipeng.zhu@hand-china.com 2021-08-12
 **/
@Slf4j
@Component
public class CacheJobServiceImpl implements CacheJobService {

    private final RedisCacheClient redisCacheClient;
    private final OnlineShopRelWarehouseRepository onlineShopRelWarehouseRepository;
    private final WarehouseRepository warehouseRepository;
    private final SystemParameterRepository systemParameterRepository;
    private final SystemParameterRedis systemParameterRedis;
    private final CarrierRedis carrierRedis;
    private final OnlineShopRedis onlineShopRedis;
    private final FreightRedis freightRedis;
    private final FreightTemplateRepository freightTemplateRepository;
    private final FreightTemplateDetailRepository freightTemplateDetailRepository;

    public CacheJobServiceImpl(RedisCacheClient redisCacheClient,
                               OnlineShopRelWarehouseRepository onlineShopRelWarehouseRepository,
                               WarehouseRepository warehouseRepository,
                               SystemParameterRepository systemParameterRepository,
                               SystemParameterRedis systemParameterRedis,
                               CarrierRedis carrierRedis,
                               OnlineShopRedis onlineShopRedis,
                               FreightRedis freightRedis, FreightTemplateRepository freightTemplateRepository, FreightTemplateDetailRepository freightTemplateDetailRepository) {
        this.redisCacheClient = redisCacheClient;
        this.onlineShopRelWarehouseRepository = onlineShopRelWarehouseRepository;
        this.warehouseRepository = warehouseRepository;
        this.systemParameterRepository = systemParameterRepository;
        this.systemParameterRedis = systemParameterRedis;
        this.carrierRedis = carrierRedis;
        this.onlineShopRedis = onlineShopRedis;
        this.freightRedis = freightRedis;
        this.freightTemplateRepository = freightTemplateRepository;
        this.freightTemplateDetailRepository = freightTemplateDetailRepository;
    }

    @Override
    public void refreshWarehouse(Long tenantId) {
        List<Warehouse> warehouseList = warehouseRepository.queryAllWarehouseByTenantId(tenantId);
        if (CollectionUtils.isNotEmpty(warehouseList)) {
            warehouseList.get(0).syncToRedis(warehouseList,
                    MetadataConstants.LuaCode.BATCH_SAVE_WAREHOUSE_REDIS_HASH_VALUE_LUA,
                    MetadataConstants.LuaCode.BATCH_DELETE_REDIS_HASH_VALUE_LUA,
                    redisCacheClient);
        }
    }

    @Override
    public void refreshOnlineShopRelWarehouse(Long tenantId) {
        List<OnlineShopRelWarehouse> list = onlineShopRelWarehouseRepository.queryAllShopRelWarehouseByTenantId(tenantId);
        onlineShopRedis.batchUpdateShopRelWh(list,tenantId, OnlineShopConstants.Redis.UPDATE);
    }

    @Override
    public void refreshSysParameter(Long tenantId) {
        // 获取系统参数
        List<SystemParameter> systemParameterList = systemParameterRepository.selectByCondition(Condition.builder(SystemParameter.class)
                .andWhere(Sqls.custom().andEqualTo(SystemParameter.FIELD_TENANT_ID, tenantId)).build());

        if (CollectionUtils.isEmpty(systemParameterList)) {
            log.warn(MessageAccessor.getMessage(SystemParameterConstants.Message.SYSTEM_PARAMETER_NOT_FOUND).desc());
            return;
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
}
