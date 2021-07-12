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
import org.o2.metadata.console.infra.constant.O2MdConsoleConstants;
import org.o2.metadata.console.infra.entity.SystemParameter;
import org.o2.metadata.console.infra.entity.Warehouse;
import org.o2.metadata.console.infra.redis.SystemParameterRedis;
import org.o2.metadata.console.infra.repository.OnlineShopRelWarehouseRepository;
import org.o2.metadata.console.infra.repository.SystemParameterRepository;
import org.o2.metadata.console.infra.repository.WarehouseRepository;
import org.o2.metadata.console.api.vo.OnlineShopRelWarehouseVO;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import io.choerodon.core.oauth.DetailsHelper;
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

    private static final String DEFAULT_ACTION = "SKIP";
    private static final String REFRESH = "REFRESH";

    private static final String CACHE_WAREHOUSE = "Warehouse";
    private static final String CACHE_ONLINE_SHOP_REL_WAREHOUSE = "OnlineShopRelWarehouse";
    private static final String CACHE_SYS_PARAMETER = "SysParameter";

    private final RedisCacheClient redisCacheClient;
    private final OnlineShopRelWarehouseRepository onlineShopRelWarehouseRepository;
    private final WarehouseRepository warehouseRepository;
    private final SystemParameterRepository systemParameterRepository;
    private final SystemParameterRedis systemParameterRedis;

    @Autowired
    public MdRedisCacheRefreshJob(RedisCacheClient redisCacheClient,
                                  OnlineShopRelWarehouseRepository onlineShopRelWarehouseRepository,
                                  WarehouseRepository warehouseRepository,
                                  SystemParameterRepository systemParameterRepository, SystemParameterRedis systemParameterRedis) {
        this.redisCacheClient = redisCacheClient;
        this.onlineShopRelWarehouseRepository = onlineShopRelWarehouseRepository;
        this.warehouseRepository = warehouseRepository;
        this.systemParameterRepository = systemParameterRepository;
        this.systemParameterRedis = systemParameterRedis;
    }

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        if (map != null && !map.isEmpty()) {
            final Long tenantId = DetailsHelper.getUserDetails().getTenantId();
            // 判断缓存操作
            final String warehouse = map.getOrDefault(CACHE_WAREHOUSE, DEFAULT_ACTION);
            final String onlineShopRelWarehouse = map.getOrDefault(CACHE_ONLINE_SHOP_REL_WAREHOUSE, DEFAULT_ACTION);
            final String sysParameter = map.getOrDefault(CACHE_SYS_PARAMETER, DEFAULT_ACTION);

            LOG.info("start synchronize metadata basic data to redis...");
            if (REFRESH.equals(warehouse)) {
                // 全量同步 仓库 数据到Redis，判断失效时间
                LOG.info("synchronize warehouse to redis cache complete.");
                refreshWarehouse(tenantId);
            }

            if (REFRESH.equals(onlineShopRelWarehouse)) {
                // 全量同步 网店关联仓库 到Redis，判断生效标记 active_flag 和失效时间
                LOG.info("synchronize onlineShopRelWarehouse to redis cache complete.");
                refreshOnlineShopRelWarehouse(tenantId);
            }

            if (REFRESH.equals(sysParameter)) {
                // 全量同步 系统参数 到Redis 判断 active_flag
                LOG.info("synchronize sysParameter to redis cache complete.");
                refreshSysParameter(tenantId);
            }
        }
        return ReturnT.SUCCESS;
    }

    /**
     * 仓库
     *
     * @param tenantId 租户ID
     */
    public void refreshWarehouse(Long tenantId) {
        List<Warehouse> warehouseList = warehouseRepository.queryAllWarehouseByTenantId(tenantId);
        if (CollectionUtils.isNotEmpty(warehouseList)) {
            warehouseList.get(0).syncToRedis(warehouseList,
                    O2MdConsoleConstants.LuaCode.BATCH_SAVE_WAREHOUSE_REDIS_HASH_VALUE_LUA,
                    O2MdConsoleConstants.LuaCode.BATCH_DELETE_REDIS_HASH_VALUE_LUA,
                    redisCacheClient);
        }
    }

    /**
     * 网点关联仓库
     *
     * @param tenantId 租户ID
     */
    public void refreshOnlineShopRelWarehouse(Long tenantId) {
        List<OnlineShopRelWarehouseVO> onlineShopRelWarehouseVOList = onlineShopRelWarehouseRepository.queryAllShopRelWarehouseByTenantId(tenantId);
        if (CollectionUtils.isNotEmpty(onlineShopRelWarehouseVOList)) {
            onlineShopRelWarehouseVOList.get(0).syncToRedis(onlineShopRelWarehouseVOList,
                    O2MdConsoleConstants.LuaCode.BATCH_SAVE_REDIS_HASH_VALUE_LUA,
                    O2MdConsoleConstants.LuaCode.BATCH_DELETE_SHOP_REL_WH_REDIS_HASH_VALUE_LUA,
                    redisCacheClient);

        }

    }

    /**
     * 系统参数
     *
     * @param tenantId 租户ID
     */
    public void refreshSysParameter(Long tenantId) {
        // 获取系统参数
        List<SystemParameter> systemParameterList = systemParameterRepository.selectByCondition(Condition.builder(SystemParameter.class)
                .andWhere(Sqls.custom().andEqualTo(SystemParameter.FIELD_TENANT_ID, tenantId)).build());

        if (CollectionUtils.isEmpty(systemParameterList)) {
            log.warn(MessageAccessor.getMessage(MetadataConstants.Message.SYSTEM_PARAMETER_NOT_FOUND).desc());
            return;
        }

        systemParameterRedis.synToRedis(systemParameterList, tenantId);

    }

}
