package org.o2.metadata.console.job;

import org.apache.commons.collections.CollectionUtils;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.hzero.core.message.MessageAccessor;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.constant.SystemParameterConstants;
import org.o2.metadata.console.infra.entity.OnlineShopRelWarehouse;
import org.o2.metadata.console.infra.entity.SystemParameter;
import org.o2.metadata.console.infra.entity.Warehouse;
import org.o2.metadata.console.infra.redis.CarrierRedis;
import org.o2.metadata.console.infra.redis.SystemParameterRedis;
import org.o2.metadata.console.infra.repository.OnlineShopRelWarehouseRepository;
import org.o2.metadata.console.infra.repository.SystemParameterRepository;
import org.o2.metadata.console.infra.repository.WarehouseRepository;
import org.o2.metadata.console.api.vo.OnlineShopRelWarehouseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * 缓存数据刷新
 * * 仓库 Warehouse
 * * 网店关联仓库 OnlineShopRelWarehouse
 * * 系统参数 SysParameter
 *
 * @author yuying.shi@hand-china.com 2020/3/24
 * @description 1、copy redis 的 expressLimitValue、pickUpLimitValue值，如果值不存在value = 0
 */
@Slf4j
@JobHandler(value = "mdRedisCacheRefreshJob")
public class MdRedisCacheRefreshJob implements IJobHandler {

    private static final Logger LOG = LoggerFactory.getLogger(MdRedisCacheRefreshJob.class);

    private final RedisCacheClient redisCacheClient;
    private final OnlineShopRelWarehouseRepository onlineShopRelWarehouseRepository;
    private final WarehouseRepository warehouseRepository;
    private final SystemParameterRepository systemParameterRepository;
    private final SystemParameterRedis systemParameterRedis;
    private final CarrierRedis carrierRedis;

    public MdRedisCacheRefreshJob(RedisCacheClient redisCacheClient,
                                  OnlineShopRelWarehouseRepository onlineShopRelWarehouseRepository,
                                  WarehouseRepository warehouseRepository,
                                  SystemParameterRepository systemParameterRepository,
                                  SystemParameterRedis systemParameterRedis,
                                  CarrierRedis carrierRedis) {
        this.redisCacheClient = redisCacheClient;
        this.onlineShopRelWarehouseRepository = onlineShopRelWarehouseRepository;
        this.warehouseRepository = warehouseRepository;
        this.systemParameterRepository = systemParameterRepository;
        this.systemParameterRedis = systemParameterRedis;
        this.carrierRedis = carrierRedis;
    }

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        if (map != null && !map.isEmpty()) {
            final Long tenantId = Long.valueOf(map.getOrDefault(MetadataConstants.CacheJob.TENANT_ID,MetadataConstants.CacheJob.DEFAULT_TENANT_ID));
            // 判断缓存操作
            final String warehouse = map.getOrDefault(MetadataConstants.CacheJob.CACHE_WAREHOUSE, MetadataConstants.CacheJob.DEFAULT_ACTION);
            final String onlineShopRelWarehouse = map.getOrDefault(MetadataConstants.CacheJob.CACHE_ONLINE_SHOP_REL_WAREHOUSE, MetadataConstants.CacheJob.DEFAULT_ACTION);
            final String sysParameter = map.getOrDefault(MetadataConstants.CacheJob.CACHE_SYS_PARAMETER, MetadataConstants.CacheJob.DEFAULT_ACTION);
            final String carrier = map.getOrDefault(MetadataConstants.CacheJob.CARRIER, MetadataConstants.CacheJob.DEFAULT_ACTION);

            LOG.info("start synchronize metadata basic data to redis...");
            if (MetadataConstants.CacheJob.REFRESH.equals(warehouse)) {
                // 全量同步 仓库 数据到Redis，判断失效时间
                LOG.info("synchronize warehouse to redis cache complete.");
                refreshWarehouse(tenantId);
            }

            if (MetadataConstants.CacheJob.REFRESH.equals(onlineShopRelWarehouse)) {
                // 全量同步 网店关联仓库 到Redis，判断生效标记 active_flag 和失效时间
                LOG.info("synchronize onlineShopRelWarehouse to redis cache complete.");
                refreshOnlineShopRelWarehouse(tenantId);
            }

            if (MetadataConstants.CacheJob.REFRESH.equals(sysParameter)) {
                // 全量同步 系统参数 到Redis 判断 active_flag
                LOG.info("synchronize sysParameter to redis cache complete.");
                refreshSysParameter(tenantId);
            }

            // 全量同步 承运商
            if (MetadataConstants.CacheJob.REFRESH.equals(carrier)) {
                refreshCarrier(tenantId);
            }
        }
        return ReturnT.SUCCESS;
    }

    /**
     * 仓库
     *
     * @param tenantId 租户ID
     */
    private void refreshWarehouse(Long tenantId) {
        List<Warehouse> warehouseList = warehouseRepository.queryAllWarehouseByTenantId(tenantId);
        if (CollectionUtils.isNotEmpty(warehouseList)) {
            warehouseList.get(0).syncToRedis(warehouseList,
                    MetadataConstants.LuaCode.BATCH_SAVE_WAREHOUSE_REDIS_HASH_VALUE_LUA,
                    MetadataConstants.LuaCode.BATCH_DELETE_REDIS_HASH_VALUE_LUA,
                    redisCacheClient);
        }
    }

    /**
     * 网点关联仓库
     *
     * @param tenantId 租户ID
     */
    private void refreshOnlineShopRelWarehouse(Long tenantId) {
        List<OnlineShopRelWarehouseVO> onlineShopRelWarehouseVOList = onlineShopRelWarehouseRepository.queryAllShopRelWarehouseByTenantId(tenantId);
        OnlineShopRelWarehouse onlineShopRelWarehouse = new OnlineShopRelWarehouse();
        if (CollectionUtils.isNotEmpty(onlineShopRelWarehouseVOList)) {
            onlineShopRelWarehouse.syncToRedis(onlineShopRelWarehouseVOList,
                    MetadataConstants.LuaCode.BATCH_SAVE_REDIS_HASH_VALUE_LUA,
                    MetadataConstants.LuaCode.BATCH_DELETE_SHOP_REL_WH_REDIS_HASH_VALUE_LUA,
                    redisCacheClient);

        }

    }

    /**
     * 系统参数
     *
     * @param tenantId 租户ID
     */
    private void refreshSysParameter(Long tenantId) {
        // 获取系统参数
        List<SystemParameter> systemParameterList = systemParameterRepository.selectByCondition(Condition.builder(SystemParameter.class)
                .andWhere(Sqls.custom().andEqualTo(SystemParameter.FIELD_TENANT_ID, tenantId)).build());

        if (CollectionUtils.isEmpty(systemParameterList)) {
            log.warn(MessageAccessor.getMessage(SystemParameterConstants.Message.SYSTEM_PARAMETER_NOT_FOUND).desc());
            return;
        }

        systemParameterRedis.synToRedis(systemParameterList, tenantId);
    }
    /**
     * 承运商
     * @param tenantId 租户ID
     */
    private void refreshCarrier(Long tenantId) {
        carrierRedis.batchUpdateRedis(tenantId);
    }

}
