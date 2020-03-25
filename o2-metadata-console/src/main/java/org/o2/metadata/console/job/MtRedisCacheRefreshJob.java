package org.o2.metadata.console.job;

import io.choerodon.core.oauth.DetailsHelper;
import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.o2.core.helper.FastJsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.console.infra.constant.O2MtConsoleConstants;
import org.o2.metadata.core.domain.entity.Warehouse;
import org.o2.metadata.core.domain.repository.OnlineShopRelWarehouseRepository;
import org.o2.metadata.core.domain.repository.WarehouseRepository;
import org.o2.metadata.core.domain.vo.OnlineShopRelWarehouseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 缓存数据刷新
 * * 仓库 Warehouse
 * * 网店关联仓库 OnlineShopRelWarehouse
 * @author yuying.shi@hand-china.com 2020/3/24
 * @description 1、copy redis 的 expressLimitValue、pickUpLimitValue值，如果值不存在value = 0
 */
@Slf4j
@JobHandler(value = "mtRedisCacheRefreshJob")
public class MtRedisCacheRefreshJob implements IJobHandler {

    private static final Logger LOG = LoggerFactory.getLogger(MtRedisCacheRefreshJob.class);

    private static final String DEFAULT_ACTION = "SKIP";
    private static final String REFRESH = "REFRESH";

    private static final String CACHE_WAREHOUSE = "Warehouse";
    private static final String CACHE_ONLINE_SHOP_REL_WAREHOUSE = "OnlineShopRelWarehouse";

    private final RedisCacheClient redisCacheClient;
    private final OnlineShopRelWarehouseRepository onlineShopRelWarehouseRepository;
    private final WarehouseRepository warehouseRepository;

    @Autowired
    public MtRedisCacheRefreshJob(RedisCacheClient redisCacheClient, OnlineShopRelWarehouseRepository onlineShopRelWarehouseRepository, WarehouseRepository warehouseRepository) {
        this.redisCacheClient = redisCacheClient;
        this.onlineShopRelWarehouseRepository = onlineShopRelWarehouseRepository;
        this.warehouseRepository = warehouseRepository;
    }


    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        if (!CollectionUtils.isEmpty(map)) {
            final Long tenantId = DetailsHelper.getUserDetails().getTenantId();
            final Date nowTime = new Date();
            // 判断缓存操作
            final String warehouse = map.getOrDefault(CACHE_WAREHOUSE, DEFAULT_ACTION);
            final String onlineShopRelWarehouse = map.getOrDefault(CACHE_ONLINE_SHOP_REL_WAREHOUSE, DEFAULT_ACTION);

            LOG.info("start synchronize metadata basic data to redis...");
            if (REFRESH.equals(warehouse)) {
                // 全量同步 仓库 数据到Redis，判断失效时间
                //
                LOG.info("synchronize warehouse to redis chche complete.");
                refreshWarehouse(tenantId,nowTime);
            }

            if (REFRESH.equals(onlineShopRelWarehouse)) {
                // 全量同步 网店关联仓库 到Redis，判断生效标记 active_flag 和失效时间
                LOG.info("synchronize onlineShopRelWarehouse to redis chche complete.");
                refreshOnlineShopRelWarehouse(tenantId,nowTime);
            }
        }
        return ReturnT.SUCCESS;
    }

    /**
     * 仓库
     * @param tenantId  租户ID
     * @param nowTime   当前时间
     */
    public void refreshWarehouse (Long tenantId, final Date nowTime) {
        Map<Integer, List<Warehouse>> warehouseMap = new HashMap<>(2);
        List<Warehouse> warehouseList =  warehouseRepository.queryAllWarehouseByTenantId(tenantId);
        List<Warehouse> efficacyWarehouseList = new ArrayList<>();
        List<Warehouse> loseEfficacyWarehouseList = new ArrayList<>();
        warehouseList.forEach(warehouse -> {
            if (warehouse.getActivedDateTo() == null || warehouse.getActivedDateTo().after(nowTime)) {
                efficacyWarehouseList.add(warehouse);
            } else {
                loseEfficacyWarehouseList.add(warehouse);
            }
        });
        warehouseMap.put(0,loseEfficacyWarehouseList);
        warehouseMap.put(1,efficacyWarehouseList);

        if ( ! CollectionUtils.isEmpty(warehouseMap)) {
            for (Map.Entry<Integer, List<Warehouse>> warehouseEntry : warehouseMap.entrySet()){
                List<String> keyList = new ArrayList<>();
                Map<String, Map<String, Object>> filedMaps = new HashMap<>();
                for (Warehouse warehouse : warehouseEntry.getValue()) {
                    final String hashKey = warehouse.getRedisHashKey(warehouse.getWarehouseCode(),tenantId);
                    keyList.add(hashKey);
                    filedMaps.put(hashKey, warehouse.getRedisHashMap());
                }
                if (warehouseEntry.getKey() == 1) {
                    this.executeScript (filedMaps,keyList,O2MtConsoleConstants.LuaCode.BATCH_SAVE_WAREHOUSE_REDIS_HASH_VALUE_LUA);
                } else {
                    this.executeScript (filedMaps,keyList,O2MtConsoleConstants.LuaCode.BATCH_DELETE_REDIS_HASH_VALUE_LUA);
                }
            }
        }
    }

    /**
     * 网点关联仓库
     * @param tenantId  租户ID
     * @param nowTime   当前时间
     */
    public void refreshOnlineShopRelWarehouse (Long tenantId, final Date nowTime) {
        Map<Integer, List<OnlineShopRelWarehouseVO>> onlineShopRelWarehouseMap = new HashMap<>(2);
        List<OnlineShopRelWarehouseVO> onlineShopRelWarehouseVOList = onlineShopRelWarehouseRepository.queryAllShopRelWarehouseByTenantId(tenantId);
        List<OnlineShopRelWarehouseVO> efficacyOnlineShopRelWarehouseList = new ArrayList<>();
        List<OnlineShopRelWarehouseVO> loseEfficacyOnlineShopRelWarehouseList = new ArrayList<>();
        onlineShopRelWarehouseVOList.forEach(onlineShopRelWarehouseVO -> {
            Boolean flag = Boolean.FALSE;
            if (onlineShopRelWarehouseVO.getActivedDateTo() == null || onlineShopRelWarehouseVO.getActivedDateTo().after(nowTime)) {
                flag = Boolean.TRUE;
            }
            if (onlineShopRelWarehouseVO.getActiveFlag() == 1 && flag) {
                efficacyOnlineShopRelWarehouseList.add(onlineShopRelWarehouseVO);
            } else {
                loseEfficacyOnlineShopRelWarehouseList.add(onlineShopRelWarehouseVO);
            }
        });
        onlineShopRelWarehouseMap.put(0,loseEfficacyOnlineShopRelWarehouseList);
        onlineShopRelWarehouseMap.put(1,efficacyOnlineShopRelWarehouseList);

        if ( ! CollectionUtils.isEmpty(onlineShopRelWarehouseMap)) {
            for (Map.Entry<Integer, List<OnlineShopRelWarehouseVO>> OnlineShopRelWarehouseEntry : onlineShopRelWarehouseMap.entrySet()){
                List<String> keyList = new ArrayList<>();
                Map<String, Map<String, Object>> filedMaps = new HashMap<>();
                for (OnlineShopRelWarehouseVO onlineShopRelWarehouseVO : OnlineShopRelWarehouseEntry.getValue()) {
                    final String hashKey = onlineShopRelWarehouseVO.getRedisHashKey(onlineShopRelWarehouseVO.getPosCode(),onlineShopRelWarehouseVO.getWarehouseCode());
                    keyList.add(hashKey);
                    filedMaps.put(hashKey, onlineShopRelWarehouseVO.getRedisHashMap(onlineShopRelWarehouseVO.getPosCode(),onlineShopRelWarehouseVO.getWarehouseCode(),onlineShopRelWarehouseVO.getBusinessActiveFlag()));
                }
                if (OnlineShopRelWarehouseEntry.getKey() == 1) {
                    this.executeScript (filedMaps,keyList,O2MtConsoleConstants.LuaCode.BATCH_SAVE_REDIS_HASH_VALUE_LUA);
                } else {
                    this.executeScript (filedMaps,keyList, O2MtConsoleConstants.LuaCode.BATCH_DELETE_REDIS_HASH_VALUE_LUA);
                }
            }
        }

    }


    /**
     *  operation redis
     * @param filedMaps filedMaps
     * @param keyList   keyList
     */
    public void executeScript(final Map<String, Map<String, Object>> filedMaps, final List<String> keyList, final ResourceScriptSource resourceScriptSource) {
        final DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(resourceScriptSource);
        this.redisCacheClient.execute(defaultRedisScript,keyList, FastJsonHelper.mapToString(filedMaps));
    }

}
