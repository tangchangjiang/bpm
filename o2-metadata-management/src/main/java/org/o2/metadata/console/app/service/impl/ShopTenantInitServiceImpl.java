package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.initialize.domain.context.TenantInitContext;
import org.o2.metadata.console.app.service.ShopTenantInitService;
import org.o2.metadata.console.infra.constant.TenantInitConstants;
import org.o2.metadata.console.infra.entity.OnlineShop;
import org.o2.metadata.console.infra.redis.OnlineShopRedis;
import org.o2.metadata.console.infra.repository.OnlineShopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
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
    @Transactional(rollbackFor = Exception.class)
    public void tenantInitialize(TenantInitContext context) {
        log.info("initializeOnlineShop start, tenantId[{}]", context.getTargetTenantId());
        // 1. 查询平台租户（默认OW-1）
        List<OnlineShop> sourceOnlineShop = selectOnlineShop(context.getSourceTenantId(), Arrays.asList(context.getParamMap().get(TenantInitConstants.InitBaseParam.BASE_ONLINE_SHOP).split(",")));

        if (CollectionUtils.isEmpty(sourceOnlineShop)) {
            log.info("platformOnlineShops is empty.");
            return;
        }

        // 2. 查询目标租户是否存在数据
        List<OnlineShop> oldOnlineShops = selectOnlineShop(context.getTargetTenantId(), Arrays.asList(context.getParamMap().get(TenantInitConstants.InitBaseParam.BASE_ONLINE_SHOP).split(",")));
        handleData(oldOnlineShops, sourceOnlineShop, context.getTargetTenantId());
        log.info("initializeOnlineShop finish, tenantId[{}]", context.getTargetTenantId());
    }

    @Override
    public List<OnlineShop> selectOnlineShop(Long tenantId, List<String> onlineShopCodes) {
        return onlineShopRepository.selectByCondition(Condition.builder(OnlineShop.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(OnlineShop.FIELD_TENANT_ID, tenantId)
                        .andIn(OnlineShop.FIELD_ONLINE_SHOP_CODE, onlineShopCodes))
                .build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tenantInitializeBusiness(TenantInitContext context) {
        // 1. 查询来源租户网店
        List<OnlineShop> onlineShops = selectOnlineShop(context.getSourceTenantId(), Arrays.asList(context.getParamMap().get(TenantInitConstants.InitBusinessParam.BUSINESS_ONLINE_SHOP).split(",")));
        if (CollectionUtils.isEmpty(onlineShops)) {
            log.info("Business: platformOnlineShops is empty.");
            return;
        }
        // 2. 查询目标租户是否存在数据
        List<OnlineShop> oldOnlineShops = selectOnlineShop(context.getTargetTenantId(), Arrays.asList(context.getParamMap().get(TenantInitConstants.InitBusinessParam.BUSINESS_ONLINE_SHOP).split(",")));
        handleData(oldOnlineShops, onlineShops, context.getTargetTenantId());
        log.info("Business: initializeOnlineShop finish, tenantId[{}]", context.getTargetTenantId());

    }

    /**
     * 处理网店数据：更新已存在的网店数据，插入未存在的目标数据
     *
     * @param oldOnlineShops        oldOnlineShops 已存在的数据
     * @param initializeOnlineShops 初始化的数据
     * @param targetTenantId        目标租户ID
     */
    private void handleData(List<OnlineShop> oldOnlineShops, List<OnlineShop> initializeOnlineShops, Long targetTenantId) {

        // 2.1 需要更新目标租户数据
        List<OnlineShop> updateList = new ArrayList<>(4);
        // 2.2 需要初始化目标租户数据
        List<OnlineShop> addList = new ArrayList<>(4);
        for (OnlineShop initialize : initializeOnlineShops) {
            String shopCode = initialize.getOnlineShopCode();
            boolean addFlag = true;
            if (CollectionUtils.isEmpty(oldOnlineShops)) {
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
            onlineShop.setObjectVersionNumber(1L);
        });
        // 3. 数据到目标租户
        onlineShopRepository.batchInsertSelective(addList);
        onlineShopRepository.batchUpdateByPrimaryKey(updateList);

        // 更新缓存
        initializeOnlineShops.clear();
        initializeOnlineShops.addAll(addList);
        initializeOnlineShops.addAll(updateList);
        initializeOnlineShops.forEach(onlineShop -> onlineShopRedis.updateRedis(onlineShop.getOnlineShopCode(), onlineShop.getTenantId()));
    }
}
