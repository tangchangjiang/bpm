package org.o2.metadata.console.app.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseConstants.Flag;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.context.inventory.InventoryContext;
import org.o2.context.inventory.api.IInventoryContext;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.console.app.service.OnlineShopRelWarehouseService;
import org.o2.metadata.console.infra.constant.O2MdConsoleConstants;
import org.o2.metadata.core.domain.entity.*;
import org.o2.metadata.core.domain.repository.OnlineShopRelWarehouseRepository;
import org.o2.metadata.core.domain.repository.OnlineShopRepository;
import org.o2.metadata.core.domain.repository.PosRepository;
import org.o2.metadata.core.domain.repository.WarehouseRepository;
import org.o2.metadata.core.domain.vo.OnlineShopRelWarehouseVO;
import org.o2.metadata.core.infra.constants.MetadataConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;


/**
 * 网店关联仓库应用服务默认实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Service
public class OnlineShopRelWarehouseServiceImpl implements OnlineShopRelWarehouseService {

    private static final Logger LOG = LoggerFactory.getLogger(OnlineShopRelWarehouseServiceImpl.class);

    private final OnlineShopRelWarehouseRepository onlineShopRelWarehouseRepository;
    private final OnlineShopRepository onlineShopRepository;
    private final WarehouseRepository warehouseRepository;
    private final PosRepository posRepository;
    private final IInventoryContext iInventoryContext;
    private RedisCacheClient redisCacheClient;

    @Autowired
    public OnlineShopRelWarehouseServiceImpl(OnlineShopRelWarehouseRepository onlineShopRelWarehouseRepository, OnlineShopRepository onlineShopRepository, WarehouseRepository warehouseRepository, PosRepository posRepository,
                                             IInventoryContext iInventoryContext, RedisCacheClient redisCacheClient) {
        this.onlineShopRelWarehouseRepository = onlineShopRelWarehouseRepository;
        this.onlineShopRepository = onlineShopRepository;
        this.warehouseRepository = warehouseRepository;
        this.posRepository = posRepository;
        this.iInventoryContext = iInventoryContext;
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<OnlineShopRelWarehouse>  batchInsertSelective(Long organizationId,final List<OnlineShopRelWarehouse> relationships) {
        Set<String> shopCodeSet = new HashSet<>();
        relationships.forEach(relationship -> {
            relationship.setTenantId(organizationId);
            Assert.isTrue(!relationship.exist(onlineShopRelWarehouseRepository), BaseConstants.ErrorCode.DATA_EXISTS);
            relationship.baseValidate(onlineShopRepository, warehouseRepository);
            relationship.setBusinessActiveFlag(getIsInvCalculated(relationship));
            OnlineShop onlineShop = onlineShopRepository.selectByPrimaryKey(relationship.getOnlineShopId());
            shopCodeSet.add(onlineShop.getOnlineShopCode());
        });

        List<OnlineShopRelWarehouse> list = onlineShopRelWarehouseRepository.batchInsertSelective(relationships);
        syncToRedis(list);
        if (!shopCodeSet.isEmpty()) {
            iInventoryContext.triggerShopStockCalByShopCode(organizationId, shopCodeSet, InventoryContext.invCalCase.SHOP_WH_SOURCED);
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<OnlineShopRelWarehouse> batchUpdateByPrimaryKey(Long tenantId, final List<OnlineShopRelWarehouse> relationships) {
        Set<String> shopCodeSet = new HashSet<>();
        relationships.forEach(relationship -> {
            relationship.setTenantId(tenantId);
            Assert.isTrue(relationship.exist(onlineShopRelWarehouseRepository), BaseConstants.ErrorCode.DATA_NOT_EXISTS);
            relationship.baseValidate(onlineShopRepository, warehouseRepository);
            relationship.setBusinessActiveFlag(getIsInvCalculated(relationship));
            OnlineShop onlineShop = onlineShopRepository.selectByPrimaryKey(relationship.getOnlineShopId());
            Warehouse warehouse = warehouseRepository.selectByPrimaryKey(relationship.getWarehouseId());
            OnlineShopRelWarehouse origin = onlineShopRelWarehouseRepository.selectByPrimaryKey(relationship);
            if (null != onlineShop && null != warehouse && null != origin) {
                if (!relationship.getActiveFlag().equals(origin.getActiveFlag())) {
                    shopCodeSet.add(onlineShop.getOnlineShopCode());
                }
                String key = String.format(MetadataConstants.OnlineShopRelWarehouse.KEY_ONLINE_SHOP_REL_WAREHOUSE, tenantId, onlineShop.getOnlineShopCode());
                redisCacheClient.opsForHash().put(key, warehouse.getWarehouseCode(), String.valueOf(relationship.getActiveFlag()));
            }
        });
        List<OnlineShopRelWarehouse> list = onlineShopRelWarehouseRepository.batchUpdateByPrimaryKey(relationships);
        syncToRedis(list);
        if (!shopCodeSet.isEmpty()) {
            iInventoryContext.triggerShopStockCalByShopCode(tenantId, shopCodeSet, InventoryContext.invCalCase.SHOP_WH_ACTIVE);
        }
        return list;
    }

    @Override
    public List<OnlineShopRelWarehouse> resetIsInvCalculated(final String onlineShopCode, final String warehouseCode,final Long tenantId) {
        OnlineShop onlineShop = null;
        Warehouse warehouse = null;

        //分别获取OnlineShop和Pos
        if (StringUtils.isNotEmpty(onlineShopCode)) {
            OnlineShop shop = new OnlineShop();
            shop.setTenantId(tenantId);
            shop.setOnlineShopCode(onlineShopCode);
            final List<OnlineShop> list = onlineShopRepository.select(shop);
            if (list.size() == 1) {
                onlineShop = list.get(0);
            }
        }

        if (StringUtils.isNotEmpty(warehouseCode)) {
            Warehouse provisional = new Warehouse();
            provisional.setTenantId(tenantId);
            provisional.setWarehouseCode(warehouseCode);
            final List<Warehouse> list = warehouseRepository.select(provisional);
            if (list.size() == 1) {
                warehouse = list.get(0);
            }
        }

        if (onlineShop == null && warehouse == null) {
            LOG.error("onlineShop 和 warehouse 有且仅能传一个值");
            return Collections.emptyList();
        } else if (onlineShop != null && warehouse != null) {
            LOG.error("onlineShop 和 pos 有且仅能传一个值");
            return Collections.emptyList();
        }

        //查询对应的onlineShopRelPos
        final OnlineShopRelWarehouse onlineShopRelWarehouse = new OnlineShopRelWarehouse();
        if (onlineShop != null) {
            onlineShopRelWarehouse.setOnlineShopId(onlineShop.getOnlineShopId());
        }
        if (warehouse != null) {
            onlineShopRelWarehouse.setWarehouseId(warehouse.getWarehouseId());
            onlineShopRelWarehouse.setPosId(warehouse.getPosId());

        }

        //进行更新数据
        final List<OnlineShopRelWarehouse> onlineShopRelWarehouseList = onlineShopRelWarehouseRepository.select(onlineShopRelWarehouse);
        final List<OnlineShopRelWarehouse> toUpdateList = new ArrayList<>(onlineShopRelWarehouseList.size());

        int oldValue;
        int newValue;
        for (final OnlineShopRelWarehouse relWarehouse : onlineShopRelWarehouseList) {
            oldValue = relWarehouse.getBusinessActiveFlag();
            newValue = getIsInvCalculated(relWarehouse);
            if (oldValue != newValue) {
                relWarehouse.setBusinessActiveFlag(newValue);
                toUpdateList.add(relWarehouse);
            }
        }
        onlineShopRelWarehouseRepository.batchUpdateByPrimaryKey(toUpdateList);

        //触发网店计算库存
        if (onlineShop != null) {

        } else {
            final Set<String> toUpdateCodeSet = new HashSet<>(toUpdateList.size());
            OnlineShop shop;
            for (final OnlineShopRelWarehouse o : toUpdateList) {
                shop = onlineShopRepository.selectByPrimaryKey(o.getOnlineShopId());
                if (shop != null) {
                    toUpdateCodeSet.add(shop.getOnlineShopCode());
                }
            }
        }
        return onlineShopRelWarehouseList;
    }

    @Override
    public void updateByShop(Long onlineShopId, String onlineShopCode, Integer activeFlag, Long tenantId) {

        List<OnlineShopRelWarehouse> onlineShopRelWarehouses = onlineShopRelWarehouseRepository.selectByCondition(
                Condition.builder(OnlineShopRelWarehouse.class).andWhere(Sqls.custom()
                        .andEqualTo(OnlineShopRelWarehouse.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(OnlineShopRelWarehouse.FIELD_ONLINE_SHOP_ID, onlineShopId)).build());

        onlineShopRelWarehouses.forEach(rel -> rel.setBusinessActiveFlag(activeFlag));

        onlineShopRelWarehouseRepository.batchUpdateByPrimaryKey(onlineShopRelWarehouses);

        String key = String.format(MetadataConstants.OnlineShopRelWarehouse.KEY_ONLINE_SHOP_REL_WAREHOUSE, tenantId, onlineShopCode);
        Map<Object, Object> map = redisCacheClient.opsForHash().entries(key);
        if (map.isEmpty()) {
            return;
        }
        String activeFlagStr = String.valueOf(activeFlag);
        Map<Object, String> m = new HashMap<>(2);
        map.keySet().forEach(hashKey -> m.put(hashKey, activeFlagStr));
        redisCacheClient.opsForHash().putAll(key, m);
    }

    private int getIsInvCalculated(final OnlineShopRelWarehouse onlineShopRelWarehouse) {
        if (onlineShopRelWarehouse == null) {
            return 0;
        }

        final OnlineShop onlineShop = onlineShopRepository.selectByPrimaryKey(onlineShopRelWarehouse.getOnlineShopId());
        final Warehouse warehouse = warehouseRepository.selectByPrimaryKey(onlineShopRelWarehouse.getWarehouseId());
        return getIsInvCalculated(onlineShopRelWarehouse, onlineShop, warehouse);
    }


    /**
     * * 满足以下条件，返回1
     * 1.网店o2md_online_shop.active_flag有效
     * 2.仓库o2md_warehouse.active_flag有效
     * 3.网店关联仓库o2md_online_shop_rel_warehouse.active_flag有效
     * @return 判断结果
     */
    private int getIsInvCalculated(final OnlineShopRelWarehouse onlineShopRelWarehouse, final OnlineShop onlineShop, final Warehouse warehouse) {
        if (onlineShopRelWarehouse == null || Flag.NO.equals(onlineShopRelWarehouse.getActiveFlag())) {
            return 0;
        }

        if (onlineShop == null || Flag.NO.equals(onlineShop.getActiveFlag())) {
            return 0;
        }

        if (warehouse == null || Flag.NO.equals(warehouse.getActiveFlag())) {
            return 0;
        }

        return 1;
    }

    /**
     * 同步到redis
     * @param relationships relationships
     */
    public void syncToRedis (final List<OnlineShopRelWarehouse> relationships) {
        List<OnlineShopRelWarehouseVO> onlineShopRelWarehouseVOList = new ArrayList<>();
        for (OnlineShopRelWarehouse onlineShopRelWarehouse : relationships) {
            final Pos pos = posRepository.selectByPrimaryKey(onlineShopRelWarehouse.getPosId());
            final Warehouse warehouse = warehouseRepository.selectByPrimaryKey(onlineShopRelWarehouse.getWarehouseId());
            final OnlineShop onlineShop = onlineShopRepository.selectByPrimaryKey(onlineShopRelWarehouse.getOnlineShopId());
            OnlineShopRelWarehouseVO onlineShopRelWarehouseVO = new OnlineShopRelWarehouseVO();
            onlineShopRelWarehouseVOList.add(onlineShopRelWarehouseVO
                    .buildOnlineShopRelWarehouseVO(pos,warehouse,onlineShop,onlineShopRelWarehouse));
        }
        if (CollectionUtils.isNotEmpty(onlineShopRelWarehouseVOList)) {
            onlineShopRelWarehouseVOList.get(0).syncToRedis(onlineShopRelWarehouseVOList,
                    O2MdConsoleConstants.LuaCode.BATCH_SAVE_REDIS_HASH_VALUE_LUA,
                    O2MdConsoleConstants.LuaCode.BATCH_DELETE_SHOP_REL_WH_REDIS_HASH_VALUE_LUA,
                    redisCacheClient);
        }
    }
}
