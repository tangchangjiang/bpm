package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.o2.metadata.console.app.service.OnlineShopRelHouseTenantInitService;
import org.o2.metadata.console.app.service.OnlineShopRelWarehouseService;
import org.o2.metadata.console.infra.constant.OnlineShopConstants;
import org.o2.metadata.console.infra.constant.TenantInitConstants;
import org.o2.metadata.console.infra.entity.OnlineShopRelWarehouse;
import org.o2.metadata.console.infra.redis.OnlineShopRedis;
import org.o2.metadata.console.infra.repository.OnlineShopRelWarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    private final OnlineShopRelWarehouseService  onlineShopRelWarehouseService;

    public OnlineShopRelHouseTenantInitServiceImpl(OnlineShopRedis onlineShopRedis, OnlineShopRelWarehouseRepository onlineShopRelWarehouseRepository, OnlineShopRelWarehouseService onlineShopRelWarehouseService) {
        this.onlineShopRedis = onlineShopRedis;
        this.onlineShopRelWarehouseRepository = onlineShopRelWarehouseRepository;
        this.onlineShopRelWarehouseService = onlineShopRelWarehouseService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tenantInitializeBusiness(long sourceTenantId, Long targetTenantId) {
      // 1. 查询源目标数据库
        log.info("Business: initializeOnlineShopRelWarehouse start, tenantId[{}]", targetTenantId);
        OnlineShopRelWarehouse query = new OnlineShopRelWarehouse();
        query.setTenantId(sourceTenantId);
        query.setOnlineShopCodes(TenantInitConstants.OnlineShopRelHouseBusiness.onlineShops);
        List<OnlineShopRelWarehouse> sourceShopRelWarehouses = onlineShopRelWarehouseService.listByCondition(query);
        if (CollectionUtils.isEmpty(sourceShopRelWarehouses)) {
            log.warn("Business data not exists in sourceTenantId[{}]", sourceTenantId);
            return;
        }
        // 2. 查询目标数据库
        query.setTenantId(targetTenantId);
        List<OnlineShopRelWarehouse> targetShopRelWarehouses = onlineShopRelWarehouseService.listByCondition(query);
        handleData(targetShopRelWarehouses,sourceShopRelWarehouses,targetTenantId);
    }
    private void handleData(List<OnlineShopRelWarehouse> oldList,List<OnlineShopRelWarehouse> initList ,Long targetTenantId) {
        // 3.1 插入目标数据库
        List<OnlineShopRelWarehouse> addList = new ArrayList<>(4);
        // 3.2 更新目标数据库
        List<OnlineShopRelWarehouse> updateList = new ArrayList<>(4);
        for (OnlineShopRelWarehouse init : initList) {
            String wareHouseCode = init.getWarehouseCode();
            String shopCode = init.getOnlineShopCode();
            boolean addFlag = true;
            if (CollectionUtils.isEmpty(oldList)) {
                addList.add(init);
                continue;
            }
            for (OnlineShopRelWarehouse old :oldList ) {
                String oldWareHouseCode = init.getWarehouseCode();
                String oldShopCode = init.getOnlineShopCode();
                if (wareHouseCode.equals(oldWareHouseCode) && shopCode.equals(oldShopCode)) {
                    init.setOnlineShopRelWarehouseId(old.getOnlineShopRelWarehouseId());
                    init.setObjectVersionNumber(old.getObjectVersionNumber());
                    init.setTenantId(targetTenantId);
                    updateList.add(init);
                    addFlag = false;
                    break;
                }
            }
            if (addFlag) {
                addList.add(init);
            }
        }
        for (OnlineShopRelWarehouse onlineShopRelWarehouse : addList) {
            onlineShopRelWarehouse.setOnlineShopRelWarehouseId(null);
            onlineShopRelWarehouse.setTenantId(targetTenantId);
        }
        //  更新
        onlineShopRelWarehouseRepository.batchInsertSelective(addList);
        onlineShopRelWarehouseRepository.batchUpdateByPrimaryKey(updateList);

        for (OnlineShopRelWarehouse onlineShopRelWarehouse : initList) {
            onlineShopRelWarehouse.setTenantId(targetTenantId);
        }
        // 更新缓存
        onlineShopRedis.batchUpdateShopRelWh(initList,targetTenantId, OnlineShopConstants.Redis.UPDATE);

    }
}
