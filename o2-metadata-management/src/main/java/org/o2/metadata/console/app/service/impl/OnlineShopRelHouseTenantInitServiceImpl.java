package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.o2.initialize.domain.context.TenantInitContext;
import org.o2.metadata.console.app.service.OnlineShopRelHouseTenantInitService;
import org.o2.metadata.console.app.service.OnlineShopRelWarehouseService;
import org.o2.metadata.console.app.service.ShopTenantInitService;
import org.o2.metadata.console.app.service.WarehouseService;
import org.o2.metadata.console.infra.constant.OnlineShopConstants;
import org.o2.metadata.console.infra.constant.TenantInitConstants;
import org.o2.metadata.console.infra.entity.OnlineShop;
import org.o2.metadata.console.infra.entity.OnlineShopRelWarehouse;
import org.o2.metadata.console.infra.entity.Warehouse;
import org.o2.metadata.console.infra.redis.OnlineShopRedis;
import org.o2.metadata.console.infra.repository.OnlineShopRelWarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 网店关联服务点数据初始化
 *
 * @author tingting.wang@hand-china.com 2022-02-10
 */
@Service
@Slf4j
public class OnlineShopRelHouseTenantInitServiceImpl implements OnlineShopRelHouseTenantInitService {
    private final OnlineShopRedis onlineShopRedis;
    private final OnlineShopRelWarehouseRepository onlineShopRelWarehouseRepository;
    private final OnlineShopRelWarehouseService onlineShopRelWarehouseService;
    private final ShopTenantInitService shopTenantInitService;
    private final WarehouseService warehouseService;

    public OnlineShopRelHouseTenantInitServiceImpl(OnlineShopRedis onlineShopRedis,
                                                   OnlineShopRelWarehouseRepository onlineShopRelWarehouseRepository,
                                                   OnlineShopRelWarehouseService onlineShopRelWarehouseService,
                                                   ShopTenantInitService shopTenantInitService,
                                                   WarehouseService warehouseService) {
        this.onlineShopRedis = onlineShopRedis;
        this.onlineShopRelWarehouseRepository = onlineShopRelWarehouseRepository;
        this.onlineShopRelWarehouseService = onlineShopRelWarehouseService;
        this.shopTenantInitService = shopTenantInitService;
        this.warehouseService = warehouseService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tenantInitializeBusiness(TenantInitContext context) {
        // 1. 查询源目标数据库
        log.info("Business: initializeOnlineShopRelWarehouse start, tenantId[{}]", context.getTargetTenantId());
        OnlineShopRelWarehouse query = new OnlineShopRelWarehouse();
        query.setTenantId(context.getSourceTenantId());
        query.setOnlineShopCodes(Arrays.asList(context.getParamMap().get(TenantInitConstants.InitBusinessParam.BUSINESS_ONLINE_SHOP).split(",")));
        query.setWarehouseCodes(Arrays.asList(context.getParamMap().get(TenantInitConstants.InitBusinessParam.BUSINESS_WAREHOUSE).split(",")));
        List<OnlineShopRelWarehouse> sourceShopRelWarehouses = onlineShopRelWarehouseService.listByCondition(query);
        if (CollectionUtils.isEmpty(sourceShopRelWarehouses)) {
            log.warn("Business data not exists in sourceTenantId[{}]", context.getSourceTenantId());
            return;
        }
        // 2. 查询目标数据库
        query.setTenantId(context.getTargetTenantId());
        List<OnlineShopRelWarehouse> targetShopRelWarehouses = onlineShopRelWarehouseService.listByCondition(query);
        handleData(targetShopRelWarehouses, sourceShopRelWarehouses, context.getTargetTenantId());
    }

    /**
     *  更新&插入 租户数据
     * @param oldList 目标租户 已存在的
     * @param initList 目标租户 重新初始化
     * @param targetTenantId  目标租户
     */
    private void handleData(List<OnlineShopRelWarehouse> oldList, List<OnlineShopRelWarehouse> initList, Long targetTenantId) {
        // 插入目标数据库
        List<OnlineShopRelWarehouse> addList = new ArrayList<>(4);
        // 获取网店编码&仓库编码
        List<String> sourceShopCode = new ArrayList<>(initList.size());
        List<String> sourceWarehouseCode = new ArrayList<>(initList.size());
        for (OnlineShopRelWarehouse warehouse : initList) {
            sourceShopCode.add(warehouse.getOnlineShopCode());
            sourceWarehouseCode.add(warehouse.getWarehouseCode());
        }
        // 网店 编码和ID 对应关系
        List<OnlineShop> targetShop = shopTenantInitService.selectOnlineShop(targetTenantId, sourceShopCode);
        Map<String, Long> targetShopMap = new HashMap<>(targetShop.size());
        for (OnlineShop onlineShop : targetShop) {
            targetShopMap.put(onlineShop.getOnlineShopCode(), onlineShop.getOnlineShopId());
        }
        // 仓库 编码和ID  对应关系
        Warehouse query = new Warehouse();
        query.setTenantId(targetTenantId);
        query.setWarehouseCodes(sourceWarehouseCode);
        List<Warehouse> sourceWarehouses = warehouseService.selectByCondition(query);
        Map<String, Long> targetWarehouseMap = new HashMap<>(sourceWarehouses.size());
        for (Warehouse sourceWarehouse : sourceWarehouses) {
            targetWarehouseMap.put(sourceWarehouse.getWarehouseCode(), sourceWarehouse.getWarehouseId());
        }
        if (CollectionUtils.isNotEmpty(oldList)) {
            onlineShopRelWarehouseRepository.batchDeleteByPrimaryKey(oldList);
            // 按key删除
            onlineShopRedis.batchUpdateShopRelWh(oldList, targetTenantId, OnlineShopConstants.Redis.DELETE);
        }
        // 种子数据 关联关系 插入目标租户中
        for (OnlineShopRelWarehouse init : initList) {
            String wareHouseCode = init.getWarehouseCode();
            String shopCode = init.getOnlineShopCode();
            init.setOnlineShopId(targetShopMap.get(shopCode));
            init.setWarehouseId(targetWarehouseMap.get(wareHouseCode));
            init.setOnlineShopRelWarehouseId(null);
            init.setTenantId(targetTenantId);
            addList.add(init);
        }
        //  更新
        onlineShopRelWarehouseRepository.batchInsertSelective(addList);
        // 更新缓存
        onlineShopRedis.batchUpdateShopRelWh(initList, targetTenantId, OnlineShopConstants.Redis.UPDATE);

    }
}
