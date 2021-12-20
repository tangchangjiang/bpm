package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.console.app.service.ShopTenantInitService;
import org.o2.metadata.console.infra.entity.OnlineShop;
import org.o2.metadata.console.infra.redis.OnlineShopRedis;
import org.o2.metadata.console.infra.repository.OnlineShopRepository;
import org.springframework.stereotype.Service;


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

    private final OnlineShopRedis onlineShopRedis;


    public ShopTenantInitServiceImpl(OnlineShopRepository onlineShopRepository, OnlineShopRedis onlineShopRedis) {
        this.onlineShopRepository = onlineShopRepository;
        this.onlineShopRedis = onlineShopRedis;
    }


    @Override
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
        final List<OnlineShop> targetOnlineShops = onlineShopRepository.selectByCondition(Condition.builder(OnlineShop.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(OnlineShop.FIELD_TENANT_ID, targetTenantId)
                        .andEqualTo(OnlineShop.FIELD_ONLINE_SHOP_CODE, "OW-1"))
                .build());

        if (CollectionUtils.isNotEmpty(targetOnlineShops)) {
            // 2.1 删除目标租户数据
            onlineShopRepository.batchDeleteByPrimaryKey(targetOnlineShops);
        }

        // 3. 插入平台数据到目标租户
        platformOnlineShops.forEach(onlineShop -> {
            onlineShop.setOnlineShopId(null);
            onlineShop.setTenantId(targetTenantId);
        });
        onlineShopRepository.batchInsert(platformOnlineShops);
        // 更新缓存
        platformOnlineShops.forEach(onlineShop -> onlineShopRedis.updateRedis(onlineShop.getOnlineShopCode(), onlineShop.getTenantId()));

        log.info("initializeOnlineShop finish, tenantId[{}]", targetTenantId);
    }
}
