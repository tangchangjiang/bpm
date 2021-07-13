package org.o2.metadata.console.app.service.impl;

import io.choerodon.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.boot.platform.code.builder.CodeRuleBuilder;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.o2.core.helper.FastJsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.inventory.management.client.O2InventoryClient;
import org.o2.inventory.management.client.api.vo.TriggerStockCalculationVO;
import org.o2.inventory.management.client.infra.constants.O2InventoryConstant;
import org.o2.metadata.console.api.vo.WarehouseVO;
import org.o2.metadata.console.app.service.WarehouseService;
import org.o2.metadata.console.infra.constant.O2MdConsoleConstants;
import org.o2.metadata.console.infra.constant.WarehouseConstants;
import org.o2.metadata.console.infra.repository.AcrossSchemaRepository;
import org.o2.metadata.console.infra.entity.Warehouse;
import org.o2.metadata.console.infra.repository.WarehouseRepository;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hzero.core.base.BaseConstants.ErrorCode.DATA_NOT_EXISTS;

/**
 * @author NieYong
 * @Title WarehouseServiceImpl
 * @Description
 * @date 2020/3/4 13:30
 **/
@Slf4j
@Service
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final AcrossSchemaRepository acrossSchemaRepository;
    private final CodeRuleBuilder codeRuleBuilder;
    private O2InventoryClient o2InventoryClient;
    private final RedisCacheClient redisCacheClient;

    @Autowired
    public WarehouseServiceImpl(final WarehouseRepository warehouseRepository,
                                final AcrossSchemaRepository acrossSchemaRepository,
                                final CodeRuleBuilder codeRuleBuilder,
                                final O2InventoryClient o2InventoryClient,
                                final RedisCacheClient redisCacheClient) {
        this.warehouseRepository = warehouseRepository;
        this.acrossSchemaRepository = acrossSchemaRepository;
        this.codeRuleBuilder = codeRuleBuilder;
        this.o2InventoryClient = o2InventoryClient;
        this.redisCacheClient = redisCacheClient;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer create(final Long tenantId, final Warehouse warehouse) {
        final String warehouseCode = codeRuleBuilder.generateCode(tenantId, MetadataConstants.CodeRuleBuilder.RULE_CODE,
                MetadataConstants.CodeRuleBuilder.LEVEL_CODE, MetadataConstants.CodeRuleBuilder.LEVEL_VALUE, null);
        log.info("create warehouseCode {}", warehouseCode);
        int insert = warehouseRepository.insert(warehouse);
        // 更新 redis
        syncToRedis(warehouse);
        return insert;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Warehouse> createBatch(final Long tenantId, final List<Warehouse> warehouses) {
        warehouseRepository.batchInsert(warehouses);
        this.operationRedis(warehouses);
        return warehouses;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer update(Warehouse warehouse) {
        final Warehouse exists = warehouseRepository.selectByPrimaryKey(warehouse.getWarehouseId());
        log.info("warehouse update exists {}", exists);
        if (exists == null) {
            throw new CommonException(DATA_NOT_EXISTS);
        }
        int i = warehouseRepository.updateByPrimaryKey(warehouse);
        log.info("warehouse update result {}", i);
        // 更新 redis
        updateWarehouseToRedis(warehouse);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Warehouse> updateBatch(final Long tenantId, final List<Warehouse> warehouses) {
        // 准备触发线上可用库存计算的数据
        List<TriggerStockCalculationVO> triggerCalInfoList = this.buildTriggerCalInfoList(tenantId, warehouses);
        // 更新MySQL
        List<Warehouse> list = warehouseRepository.batchUpdateByPrimaryKey(warehouses);
        // 更新 redis
        this.operationRedis(warehouses);
        // 触发线上可用库存计算
        if (CollectionUtils.isNotEmpty(triggerCalInfoList)) {
            o2InventoryClient.triggerWhStockCal(tenantId, triggerCalInfoList);
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Warehouse> batchHandle(Long tenantId, List<Warehouse> warehouses) {
        log.info("warehouse batch handle tenantId({}), warehouse size({})", tenantId, warehouses.size());
        List<Warehouse> updateList = new ArrayList<>();
        List<Warehouse> insertList = new ArrayList<>();
        for (Warehouse warehouse : warehouses) {
            log.info("warehouse batch handle warehouse({}), _status({})", warehouse.getWarehouseId(), warehouse.get_status().name());
            if (O2MdConsoleConstants.Status.CREATE.equals(warehouse.get_status().name())) {
                insertList.add(warehouse);
            }
            if (O2MdConsoleConstants.Status.UPDATE.equals(warehouse.get_status().name())) {
                SecurityTokenHelper.validToken(warehouse);
                updateList.add(warehouse);
            }
        }
        log.info("warehouse batch handle insert({}), update({})", insertList.size(), updateList.size());
        List<Warehouse> createList = createBatch(tenantId, insertList);
        log.info("warehouse batch handle 5");
        List<Warehouse> list = updateBatch(tenantId, updateList);
        log.info("warehouse batch handle 6");
        List<Warehouse> totalList = new ArrayList<>();
        totalList.addAll(createList);
        totalList.addAll(list);
        return totalList ;
    }

    @Override
    public WarehouseVO getWarehouse(String warehouseCode, Long tenantId) {
        return null;
    }

    private List<TriggerStockCalculationVO> buildTriggerCalInfoList(final Long tenantId, final List<Warehouse> warehouses) {
        // 触发线上可用库存计算
        List<TriggerStockCalculationVO> triggerCalInfoList = new ArrayList<>();
        for (Warehouse warehouse : warehouses) {
            List<Warehouse> warehouseList = warehouseRepository.selectByCondition(Condition.builder(Warehouse.class).andWhere(Sqls.custom()
                    .andEqualTo(Warehouse.FIELD_WAREHOUSE_ID, warehouse.getWarehouseId())
                    .andEqualTo(Warehouse.FIELD_TENANT_ID, warehouse.getTenantId())).build());
            if (CollectionUtils.isEmpty(warehouseList)) {
                continue;
            }
            Warehouse origin = warehouseList.get(0);
            String warehouseCode = origin.getWarehouseCode();
            List<String> skuCodeList = acrossSchemaRepository.selectSkuByWarehouse(warehouseCode, tenantId);
            for (String skuCode : skuCodeList) {
                log.info("warehouse buildTriggerCalInfoList activeFlag({}),({});expressedFlag({}),({});pickedUpFlag({}),({})",
                        warehouse.getWarehouseStatusCode(), origin.getWarehouseStatusCode(), warehouse.getActiveFlag(), origin.getActiveFlag(),
                        warehouse.getExpressedFlag(), origin.getExpressedFlag(), warehouse.getPickedUpFlag(), origin.getPickedUpFlag());
                if (!warehouse.getWarehouseStatusCode().equals(origin.getWarehouseStatusCode())) {
                    TriggerStockCalculationVO triggerStockCalculationVO = new TriggerStockCalculationVO();
                    triggerStockCalculationVO.setWarehouseCode(warehouseCode);
                    triggerStockCalculationVO.setSkuCode(skuCode);
                    triggerStockCalculationVO.setTriggerSource(O2InventoryConstant.invCalCase.WH_STATUS);
                    triggerCalInfoList.add(triggerStockCalculationVO);
                    continue;
                }
                if (!warehouse.getActiveFlag().equals(origin.getActiveFlag())) {
                    TriggerStockCalculationVO triggerStockCalculationVO = new TriggerStockCalculationVO();
                    triggerStockCalculationVO.setWarehouseCode(warehouseCode);
                    triggerStockCalculationVO.setSkuCode(skuCode);
                    triggerStockCalculationVO.setTriggerSource(O2InventoryConstant.invCalCase.WH_ACTIVE);
                    triggerCalInfoList.add(triggerStockCalculationVO);
                    continue;
                }
                if (!warehouse.getExpressedFlag().equals(origin.getExpressedFlag())) {
                    TriggerStockCalculationVO triggerStockCalculationVO = new TriggerStockCalculationVO();
                    triggerStockCalculationVO.setWarehouseCode(warehouseCode);
                    triggerStockCalculationVO.setSkuCode(skuCode);
                    triggerStockCalculationVO.setTriggerSource(O2InventoryConstant.invCalCase.WH_EXPRESS);
                    triggerCalInfoList.add(triggerStockCalculationVO);
                    continue;
                }
                if (!warehouse.getPickedUpFlag().equals(origin.getPickedUpFlag())) {
                    TriggerStockCalculationVO triggerStockCalculationVO = new TriggerStockCalculationVO();
                    triggerStockCalculationVO.setWarehouseCode(warehouseCode);
                    triggerStockCalculationVO.setSkuCode(skuCode);
                    triggerStockCalculationVO.setTriggerSource(O2InventoryConstant.invCalCase.WH_PICKUP);
                    triggerCalInfoList.add(triggerStockCalculationVO);
                }
            }
        }
        return triggerCalInfoList;
    }

    /**
     * update redis
     *
     * @param warehouse warehouse
     */
    private void updateWarehouseToRedis(final Warehouse warehouse) {
        Map<String, Object> hashMap = warehouse.buildRedisHashMap();
        updateWarehouse(warehouse.getWarehouseCode(), hashMap,warehouse.getTenantId());
    }

    private void operationRedis(List<Warehouse> warehouses) {
        if (CollectionUtils.isNotEmpty(warehouses)) {
            warehouses.get(0).syncToRedis(warehouses,
                    O2MdConsoleConstants.LuaCode.BATCH_SAVE_WAREHOUSE_REDIS_HASH_VALUE_LUA,
                    O2MdConsoleConstants.LuaCode.BATCH_DELETE_REDIS_HASH_VALUE_LUA,
                    redisCacheClient);
        }
    }
    /**
     * 新增到 redis
     *
     * @param warehouse warehouse
     */
    private void syncToRedis(final Warehouse warehouse) {
        Map<String, Object> hashMap = warehouse.buildRedisHashMap();
        saveWarehouse(warehouse.getWarehouseCode(), hashMap,warehouse.getTenantId());
    }
    private void updateWarehouse(String warehouseCode, Map<String, Object> hashMap, Long tenantId) {
        executeScript(warehouseCode, hashMap, tenantId, UPDATE_WAREHOUSE_LUA);

    }
    private void saveWarehouse(String warehouseCode,Map<String, Object> hashMap,  Long tenantId) {
        executeScript(warehouseCode, hashMap, tenantId, SAVE_WAREHOUSE_LUA);
    }
    private void executeScript(final String warehouseCode, final Map<String,Object> hashMap, final Long tenantId, final ScriptSource scriptSource) {
        final DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(scriptSource);
        this.redisCacheClient.execute(defaultRedisScript, Collections.singletonList(warehouseCacheKey(warehouseCode, tenantId)), FastJsonHelper.objectToString(hashMap));
    }
    private String warehouseCacheKey(String warehouseCode, Long tenantId) {
        if (tenantId == null) {
            return WarehouseConstants.WarehouseCache.warehouseCacheKey(0, warehouseCode);
        }
        return WarehouseConstants.WarehouseCache.warehouseCacheKey(tenantId, warehouseCode);
    }

    private static final ResourceScriptSource SAVE_WAREHOUSE_LUA =
            new ResourceScriptSource(new ClassPathResource("script/lua/warehouse/save_warehouse_cache.lua"));
    private static final ResourceScriptSource UPDATE_WAREHOUSE_LUA =
            new ResourceScriptSource(new ClassPathResource("script/lua/warehouse/update_warehouse_cache.lua"));

}
