package org.o2.metadata.console.app.service.impl;

import io.choerodon.core.exception.CommonException;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseConstants.Flag;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.core.helper.FastJsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.console.app.service.OnlineShopRelWarehouseService;
import org.o2.metadata.core.domain.entity.*;
import org.o2.metadata.core.domain.repository.OnlineShopRelWarehouseRepository;
import org.o2.metadata.core.domain.repository.OnlineShopRepository;
import org.o2.metadata.core.domain.repository.PosRepository;
import org.o2.metadata.core.domain.repository.WarehouseRepository;
import org.o2.metadata.core.infra.constants.MetadataConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.ScriptSource;
import org.springframework.stereotype.Service;
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
    private RedisCacheClient redisCacheClient;

    public OnlineShopRelWarehouseServiceImpl(OnlineShopRelWarehouseRepository onlineShopRelWarehouseRepository, OnlineShopRepository onlineShopRepository, WarehouseRepository warehouseRepository, PosRepository posRepository, RedisCacheClient redisCacheClient) {
        this.onlineShopRelWarehouseRepository = onlineShopRelWarehouseRepository;
        this.onlineShopRepository = onlineShopRepository;
        this.warehouseRepository = warehouseRepository;
        this.posRepository = posRepository;
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public List<OnlineShopRelWarehouse>  batchInsertSelective(Long organizationId,final List<OnlineShopRelWarehouse> relationships) {
        relationships.forEach(relationship -> {
            relationship.setTenantId(organizationId);
            Assert.isTrue(!relationship.exist(onlineShopRelWarehouseRepository), BaseConstants.ErrorCode.DATA_EXISTS);
            relationship.baseValidate(onlineShopRepository, warehouseRepository);
            relationship.setBusinessActiveFlag(getIsInvCalculated(relationship));
        });

        List<OnlineShopRelWarehouse> list = onlineShopRelWarehouseRepository.batchInsertSelective(relationships);
        syncToRedis(list,MetadataConstants.LuaCode.SAVE_REDIS_HASH_VALUE_LUA);
        return list;
    }

    @Override
    public List<OnlineShopRelWarehouse> batchUpdateByPrimaryKey(Long organizationId,final List<OnlineShopRelWarehouse> relationships) {
        relationships.forEach(relationship -> {
            relationship.setTenantId(organizationId);
            Assert.isTrue(relationship.exist(onlineShopRelWarehouseRepository), BaseConstants.ErrorCode.DATA_NOT_EXISTS);
            relationship.baseValidate(onlineShopRepository, warehouseRepository);
            relationship.setBusinessActiveFlag(getIsInvCalculated(relationship));
        });
        List<OnlineShopRelWarehouse> list = onlineShopRelWarehouseRepository.batchUpdateByPrimaryKey(relationships);
        syncToRedis(list,MetadataConstants.LuaCode.UPDATE_REDIS_HASH_VALUE_LUA);
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

    private int getIsInvCalculated(final OnlineShopRelWarehouse onlineShopRelWarehouse) {
        if (onlineShopRelWarehouse == null) {
            return 0;
        }

        final OnlineShop onlineShop = onlineShopRepository.selectByPrimaryKey(onlineShopRelWarehouse.getOnlineShopId());
        final Pos pos = posRepository.selectByPrimaryKey(onlineShopRelWarehouse.getPosId());
        return getIsInvCalculated(onlineShopRelWarehouse, onlineShop, pos);
    }


    /**
     * * 满足以下条件，返回1
     * 1.网店关联POS有效
     * 2.网店有效
     * 3.POS状态为正常
     * 4.如果POS为门店，需要满足POS可快递发货且接单量未达到上限
     *
     * @return 判断结果
     */
    private int getIsInvCalculated(final OnlineShopRelWarehouse onlineShopRelWarehouse, final OnlineShop onlineShop, final Pos pos) {
        if (onlineShopRelWarehouse == null || Flag.NO.equals(onlineShopRelWarehouse.getActiveFlag())) {
            return 0;
        }

        if (onlineShop == null || Flag.NO.equals(onlineShop.getActiveFlag())) {
            return 0;
        }

        if (pos == null || !MetadataConstants.PosStatus.NORMAL.equals(pos.getPosStatusCode())) {
            return 0;
        }

        return 1;
    }

    /**
     * 同步到redis
     * @param relationships
     * @param scriptSource
     */
    public void syncToRedis (final List<OnlineShopRelWarehouse> relationships,final ScriptSource scriptSource) {
        for (OnlineShopRelWarehouse onlineShopRelWarehouse : relationships) {
            final Pos pos = posRepository.selectByPrimaryKey(onlineShopRelWarehouse.getPosId());
            final Warehouse warehouse = warehouseRepository.selectByPrimaryKey(onlineShopRelWarehouse.getWarehouseId());
            final OnlineShop onlineShop = onlineShopRepository.selectByPrimaryKey(onlineShopRelWarehouse.getOnlineShopId());
            Map<String, String> hashMap = onlineShopRelWarehouse.getRedisHashMap(pos.getPosCode(),warehouse.getWarehouseCode(),onlineShopRelWarehouse.getBusinessActiveFlag());
            String hashKey = onlineShopRelWarehouse.getRedisHashKey(onlineShop.getOnlineShopCode(),warehouse.getWarehouseCode());
            executeScript(hashMap,hashKey,scriptSource);
        }
    }


    public void executeScript( final Map<String,String> hashMap, final String hashKey, final ScriptSource scriptSource) {
        final DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(scriptSource);
        this.redisCacheClient.execute(defaultRedisScript, Collections.singletonList(hashKey), FastJsonHelper.objectToString(hashMap));
    }
}