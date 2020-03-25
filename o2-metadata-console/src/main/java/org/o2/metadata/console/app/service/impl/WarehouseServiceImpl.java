package org.o2.metadata.console.app.service.impl;

import io.choerodon.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.boot.platform.code.builder.CodeRuleBuilder;
import org.o2.context.metadata.api.IWarehouseContext;
import org.o2.context.metadata.config.MetadataContextConsumer;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.console.app.service.WarehouseService;
import org.o2.metadata.console.infra.constant.O2MdConsoleConstants;
import org.o2.metadata.core.domain.entity.Warehouse;
import org.o2.metadata.core.domain.repository.WarehouseRepository;
import org.o2.metadata.core.infra.constants.MetadataConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    private CodeRuleBuilder codeRuleBuilder;

    private final IWarehouseContext warehouseContext;

    private RedisCacheClient redisCacheClient;

    @Autowired
    public WarehouseServiceImpl(WarehouseRepository warehouseRepository, CodeRuleBuilder codeRuleBuilder,final MetadataContextConsumer metadataContextConsumer,RedisCacheClient redisCacheClient) {
        this.warehouseRepository = warehouseRepository;
        this.codeRuleBuilder = codeRuleBuilder;
        this.warehouseContext = metadataContextConsumer.warehouseContext();
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
    public List<Warehouse> updateBatch(List<Warehouse> warehouses) {
        List<Warehouse> list = warehouseRepository.batchUpdateByPrimaryKeySelective(warehouses);
        // 更新 redis
        this.operationRedis(warehouses);
        return list;
    }


    /**
     * 新增到 redis
     * @param warehouse warehouse
     */
    private void syncToRedis(final Warehouse warehouse) {
        Map<String, Object> hashMap = warehouse.getRedisHashMap();
        this.warehouseContext.saveWarehouse(warehouse.getWarehouseCode(),hashMap,warehouse.getTenantId());
    }

    /**
     *  update redis
     * @param warehouse warehouse
     */
    private void updateWarehouseToRedis(final Warehouse warehouse) {
        Map<String, Object> hashMap = warehouse.getRedisHashMap();
        this.warehouseContext.updateWarehouse(warehouse.getWarehouseCode(),hashMap,warehouse.getTenantId());
    }

    public void operationRedis (List<Warehouse> warehouses) {
        if (CollectionUtils.isNotEmpty(warehouses)) {
            warehouses.get(0).syncToRedis(warehouses,
                    O2MdConsoleConstants.LuaCode.BATCH_SAVE_WAREHOUSE_REDIS_HASH_VALUE_LUA,
                    O2MdConsoleConstants.LuaCode.BATCH_DELETE_REDIS_HASH_VALUE_LUA,
                    redisCacheClient);
        }
    }

}
