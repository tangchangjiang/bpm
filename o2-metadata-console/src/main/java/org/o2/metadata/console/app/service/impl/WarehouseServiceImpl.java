package org.o2.metadata.console.app.service.impl;

import io.choerodon.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.boot.platform.code.builder.CodeRuleBuilder;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.o2.context.inventory.InventoryContext;
import org.o2.context.inventory.api.IInventoryContext;
import org.o2.context.inventory.vo.TriggerStockCalculationVO;
import org.o2.context.metadata.api.IWarehouseContext;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.console.app.service.WarehouseService;
import org.o2.metadata.console.infra.constant.O2MdConsoleConstants;
import org.o2.metadata.console.infra.repository.AcrossSchemaRepository;
import org.o2.metadata.core.domain.entity.Warehouse;
import org.o2.metadata.core.domain.repository.WarehouseRepository;
import org.o2.metadata.core.infra.constants.MetadataConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    private WarehouseRepository warehouseRepository;

    private AcrossSchemaRepository acrossSchemaRepository;

    private CodeRuleBuilder codeRuleBuilder;

    private final IWarehouseContext warehouseContext;

    private final IInventoryContext iInventoryContext;

    private RedisCacheClient redisCacheClient;

    @Autowired
    public WarehouseServiceImpl(WarehouseRepository warehouseRepository,
                                AcrossSchemaRepository acrossSchemaRepository,
                                CodeRuleBuilder codeRuleBuilder,
                                IWarehouseContext warehouseContext,
                                IInventoryContext iInventoryContext,
                                RedisCacheClient redisCacheClient) {
        this.warehouseRepository = warehouseRepository;
        this.acrossSchemaRepository = acrossSchemaRepository;
        this.codeRuleBuilder = codeRuleBuilder;
        this.warehouseContext = warehouseContext;
        this.iInventoryContext = iInventoryContext;
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
        /*for (Warehouse warehouse : warehouses) {
            // 编码规则：W + 6位流水
            final String warehouseCode = codeRuleBuilder.generateCode(tenantId, MetadataConstants.CodeRuleBuilder.RULE_CODE,
                    MetadataConstants.CodeRuleBuilder.LEVEL_CODE, MetadataConstants.CodeRuleBuilder.LEVEL_VALUE, null);
            log.info("create batch warehouseCode {}", warehouseCode);
            warehouse.setWarehouseCode(warehouseCode);
        }*/
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
        List<Warehouse> list = warehouseRepository.batchUpdateByPrimaryKeySelective(warehouses);
        // 更新 redis
        this.operationRedis(warehouses);
        // 触发线上可用库存计算
        if (CollectionUtils.isNotEmpty(triggerCalInfoList)) {
            iInventoryContext.triggerWhStockCal(tenantId, triggerCalInfoList);
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Warehouse> batchHandle(Long tenantId, List<Warehouse> warehouses) {
        List<Warehouse> updateList = new ArrayList<>();
        List<Warehouse> insertList = new ArrayList<>();
        for (Warehouse warehouse : warehouses) {
            if (O2MdConsoleConstants.Status.CREATE.equals(warehouse.get_status().name())) {
                insertList.add(warehouse);
            }
            if (O2MdConsoleConstants.Status.UPDATE.equals(warehouse.get_status().name())) {
                SecurityTokenHelper.validToken(warehouse);
                updateList.add(warehouse);
            }
        }
        List<Warehouse> createList = createBatch(tenantId, insertList);
        List<Warehouse> list = updateBatch(tenantId, updateList);
        List<Warehouse> totalList = new ArrayList<>();
        totalList.addAll(createList);
        totalList.addAll(list);
        return totalList ;
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
                if (!warehouse.getActiveFlag().equals(origin.getActiveFlag())) {
                    TriggerStockCalculationVO triggerStockCalculationVO = new TriggerStockCalculationVO();
                    triggerStockCalculationVO.setWarehouseCode(warehouseCode);
                    triggerStockCalculationVO.setSkuCode(skuCode);
                    triggerStockCalculationVO.setTriggerSource(InventoryContext.invCalCase.WH_ACTIVE);
                    triggerCalInfoList.add(triggerStockCalculationVO);
                    continue;
                }
                if (!warehouse.getExpressedFlag().equals(origin.getExpressedFlag())) {
                    TriggerStockCalculationVO triggerStockCalculationVO = new TriggerStockCalculationVO();
                    triggerStockCalculationVO.setWarehouseCode(warehouseCode);
                    triggerStockCalculationVO.setSkuCode(skuCode);
                    triggerStockCalculationVO.setTriggerSource(InventoryContext.invCalCase.WH_EXPRESS);
                    triggerCalInfoList.add(triggerStockCalculationVO);
                    continue;
                }
                if (!warehouse.getPickedUpFlag().equals(origin.getPickedUpFlag())) {
                    TriggerStockCalculationVO triggerStockCalculationVO = new TriggerStockCalculationVO();
                    triggerStockCalculationVO.setWarehouseCode(warehouseCode);
                    triggerStockCalculationVO.setSkuCode(skuCode);
                    triggerStockCalculationVO.setTriggerSource(InventoryContext.invCalCase.WH_PICKUP);
                    triggerCalInfoList.add(triggerStockCalculationVO);
                }
            }
        }
        return triggerCalInfoList;
    }


    /**
     * 新增到 redis
     *
     * @param warehouse warehouse
     */
    private void syncToRedis(final Warehouse warehouse) {
        Map<String, Object> hashMap = warehouse.buildRedisHashMap();
        this.warehouseContext.saveWarehouse(warehouse.getWarehouseCode(), hashMap, warehouse.getTenantId());
    }

    /**
     * update redis
     *
     * @param warehouse warehouse
     */
    private void updateWarehouseToRedis(final Warehouse warehouse) {
        Map<String, Object> hashMap = warehouse.buildRedisHashMap();
        this.warehouseContext.updateWarehouse(warehouse.getWarehouseCode(), hashMap, warehouse.getTenantId());
    }

    public void operationRedis(List<Warehouse> warehouses) {
        if (CollectionUtils.isNotEmpty(warehouses)) {
            warehouses.get(0).syncToRedis(warehouses,
                    O2MdConsoleConstants.LuaCode.BATCH_SAVE_WAREHOUSE_REDIS_HASH_VALUE_LUA,
                    O2MdConsoleConstants.LuaCode.BATCH_DELETE_REDIS_HASH_VALUE_LUA,
                    redisCacheClient);
        }
    }

}
