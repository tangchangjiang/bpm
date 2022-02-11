package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.console.app.service.OnlineShopService;
import org.o2.metadata.console.app.service.ShopTenantInitService;
import org.o2.metadata.console.infra.constant.TenantInitConstants;
import org.o2.metadata.console.infra.entity.OnlineShop;
import org.o2.metadata.console.infra.redis.OnlineShopRedis;
import org.o2.metadata.console.infra.repository.OnlineShopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

/**
 * 网店租户初始化
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/17 15:46
 */
@Slf4j
@Service
public class ShopTenantInitServiceImpl implements ShopTenantInitService {

    private final OnlineShopRepository onlineShopRepository;
    private final OnlineShopService onlineShopService;

    private final OnlineShopRedis onlineShopRedis;


    public ShopTenantInitServiceImpl(OnlineShopRepository onlineShopRepository, OnlineShopService onlineShopService, OnlineShopRedis onlineShopRedis) {
        this.onlineShopRepository = onlineShopRepository;
        this.onlineShopService = onlineShopService;
        this.onlineShopRedis = onlineShopRedis;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tenantInitialize(long sourceTenantId, Long targetTenantId) {
        log.info("initializeOnlineShop start, tenantId[{}]", targetTenantId);
        // 1. 查询平台租户（默认OW-1）
        final List<OnlineShop> platformOnlineShops = onlineShopRepository.selectByCondition(Condition.builder(OnlineShop.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(OnlineShop.FIELD_TENANT_ID, sourceTenantId)
                        .andEqualTo(OnlineShop.FIELD_ONLINE_SHOP_CODE, "OW-1")
                )
                .build());

        if (CollectionUtils.isEmpty(platformOnlineShops)) {
            log.info("platformOnlineShops is empty.");
            return;
        }

        // 2. 查询目标租户是否存在数据
        final List<OnlineShop> oldOnlineShops = onlineShopRepository.selectByCondition(Condition.builder(OnlineShop.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(OnlineShop.FIELD_TENANT_ID, targetTenantId)
                        .andEqualTo(OnlineShop.FIELD_ONLINE_SHOP_CODE, "OW-1"))
                .build());
        handleData(oldOnlineShops,platformOnlineShops,targetTenantId);
        log.info("initializeOnlineShop finish, tenantId[{}]", targetTenantId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tenantInitializeBusiness(long sourceTenantId, Long targetTenantId) {
        // 1. 查询来源租户网店
        OnlineShop query = new  OnlineShop();
        query.setTenantId(sourceTenantId);
        query.setOnlineShopCodes(TenantInitConstants.InitOnlineShopBusiness.onlineShops);
        List<OnlineShop> onlineShops = onlineShopService.selectByCondition(query);
        if (CollectionUtils.isEmpty(onlineShops)) {
            log.info("Business: platformOnlineShops is empty.");
            return;
        }
        // 2. 查询目标租户是否存在数据
        query.setTenantId(targetTenantId);
        List<OnlineShop> oldOnlineShops = onlineShopService.selectByCondition(query);
        handleData(oldOnlineShops,onlineShops,targetTenantId);
        log.info("Business: initializeOnlineShop finish, tenantId[{}]", targetTenantId);

    }

    /**
     * 处理网店数据：更新已存在的网店数据，插入未存在的目标数据
     * @param oldOnlineShops oldOnlineShops 已存在的数据
     * @param initializeOnlineShops   初始化的数据
     * @param targetTenantId 目标租户ID
     */
    private void handleData(List<OnlineShop> oldOnlineShops, List<OnlineShop> initializeOnlineShops, Long targetTenantId) {

        // 2.1 需要更新目标租户数据
        List<OnlineShop> updateList = new ArrayList<>(4);
        // 2.2 需要初始化目标租户数据
        List<OnlineShop> addList = new ArrayList<>(4);
        for (OnlineShop initialize : initializeOnlineShops) {
            String shopCode = initialize.getOnlineShopCode();
            boolean addFlag = true;
            if (CollectionUtils.isNotEmpty(oldOnlineShops)) {
                addList.add(initialize);
                continue;
            }
            for (OnlineShop old : oldOnlineShops) {
                String oldShopCode = old.getOnlineShopCode();
                if (shopCode.equals(oldShopCode)) {
                    initialize.setTenantId(old.getTenantId());
                    initialize.setObjectVersionNumber(old.getObjectVersionNumber());
                    initialize.setOnlineShopId(old.getOnlineShopId());
                    updateList.add(initialize);
                    addFlag = false;
                    break;
                }
            }
            if (addFlag) {
                addList.add(initialize);
            }
        }
        addList.forEach(onlineShop -> {
            onlineShop.setOnlineShopId(null);
            onlineShop.setTenantId(targetTenantId);
        });
        // 3. 数据到目标租户
        onlineShopRepository.batchInsert(addList);
        onlineShopRepository.batchUpdateByPrimaryKey(updateList);

        // 更新缓存
        initializeOnlineShops.clear();
        initializeOnlineShops.addAll(addList);
        initializeOnlineShops.addAll(updateList);
        initializeOnlineShops.forEach(onlineShop -> onlineShopRedis.updateRedis(onlineShop.getOnlineShopCode(), onlineShop.getTenantId()));
    }
}
