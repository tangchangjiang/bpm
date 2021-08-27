package org.o2.metadata.console.app.service.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.inventory.management.client.O2InventoryClient;
import org.o2.inventory.management.client.domain.constants.O2InventoryConstant;
import org.o2.inventory.management.client.domain.vo.TriggerStockCalculationVO;
import org.o2.metadata.console.api.co.WarehouseCO;
import org.o2.metadata.console.api.dto.WarehouseAddrQueryDTO;
import org.o2.metadata.console.api.dto.WarehouseQueryInnerDTO;
import org.o2.metadata.console.api.dto.WarehouseRelCarrierQueryDTO;
import org.o2.metadata.console.app.service.WarehouseService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.constant.WarehouseConstants;
import org.o2.metadata.console.infra.convertor.WarehouseConverter;
import org.o2.metadata.console.infra.entity.Carrier;
import org.o2.metadata.console.infra.entity.Warehouse;
import org.o2.metadata.console.infra.redis.WarehouseRedis;
import org.o2.metadata.console.infra.repository.AcrossSchemaRepository;
import org.o2.metadata.console.infra.repository.WarehouseRepository;
import org.o2.metadata.domain.warehouse.service.WarehouseDomainService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    private final O2InventoryClient o2InventoryClient;
    private final RedisCacheClient redisCacheClient;
    private final WarehouseDomainService warehouseDomainService;
    private final WarehouseRedis warehouseRedis;

    public WarehouseServiceImpl(final WarehouseRepository warehouseRepository,
                                final AcrossSchemaRepository acrossSchemaRepository,
                                final O2InventoryClient o2InventoryClient,
                                final RedisCacheClient redisCacheClient,
                                WarehouseDomainService warehouseDomainService, WarehouseRedis warehouseRedis) {
        this.warehouseRepository = warehouseRepository;
        this.acrossSchemaRepository = acrossSchemaRepository;
        this.o2InventoryClient = o2InventoryClient;
        this.redisCacheClient = redisCacheClient;
        this.warehouseDomainService = warehouseDomainService;
        this.warehouseRedis = warehouseRedis;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Warehouse> createBatch(final Long tenantId, final List<Warehouse> warehouses) {
        List<String> warehouseCodes = Lists.newArrayListWithExpectedSize(warehouses.size());
        for (Warehouse warehouse : warehouses) {
            warehouseCodes.add(warehouse.getWarehouseCode());
        }
        warehouseRepository.batchInsert(warehouses);
        warehouseRedis.batchUpdateWarehouse(warehouseCodes,tenantId);
        return warehouses;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Warehouse> updateBatch(final Long tenantId, final List<Warehouse> warehouses) {
        // 准备触发线上可用库存计算的数据
        List<TriggerStockCalculationVO> triggerCalInfoList = this.buildTriggerCalInfoList(tenantId, warehouses);
        // 更新MySQL
        List<Warehouse> list = warehouseRepository.batchUpdateByPrimaryKey(warehouses);
        List<String> warehouseCodes = Lists.newArrayListWithExpectedSize(warehouses.size());
        for (Warehouse warehouse : warehouses) {
            warehouseCodes.add(warehouse.getWarehouseCode());
        }
        // 更新 redis
        warehouseRedis.batchUpdateWarehouse(warehouseCodes,tenantId);
        // 触发线上可用库存计算
        if (CollectionUtils.isNotEmpty(triggerCalInfoList)) {
            o2InventoryClient.triggerWhStockCal(tenantId, triggerCalInfoList);
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Warehouse> batchHandle(Long tenantId, List<Warehouse> warehouses) {
        List<Warehouse> updateList = new ArrayList<>();
        List<Warehouse> insertList = new ArrayList<>();
        for (Warehouse warehouse : warehouses) {
            if (MetadataConstants.Status.CREATE.equals(warehouse.get_status().name())) {
                insertList.add(warehouse);
            }
            if (MetadataConstants.Status.UPDATE.equals(warehouse.get_status().name())) {
                SecurityTokenHelper.validToken(warehouse);
                updateList.add(warehouse);
            }
        }
        List<Warehouse> totalList = new ArrayList<>();
        if (!insertList.isEmpty()) {
            List<Warehouse> createList = createBatch(tenantId, insertList);
            totalList.addAll(createList);

        }
        if (!updateList.isEmpty()) {
            List<Warehouse> list = updateBatch(tenantId, updateList);
            totalList.addAll(list);
        }
        return totalList ;
    }

    @Override
    public List<WarehouseCO> listWarehouses(WarehouseQueryInnerDTO queryInnerDTO, Long tenantId) {
        Boolean dbFlag = queryInnerDTO.getDbFlag();
        // 查询数据库
        if (null == dbFlag || Boolean.TRUE.equals(dbFlag)) {
            // 通过网店查询有效的网店
            if (StringUtils.isNotEmpty(queryInnerDTO.getOnlineShopCode()) && BaseConstants.Flag.YES.equals(queryInnerDTO.getActiveFlag())) {
                return WarehouseConverter.poToCoListObjects(warehouseRepository.listActiveWarehouseByShopCode(queryInnerDTO.getOnlineShopCode(), tenantId));
            }
            return WarehouseConverter.poToCoListObjects(warehouseRepository.listWarehouses(queryInnerDTO, tenantId));
        }
        // 查询redis
        return WarehouseConverter.doToCoListObjects(warehouseDomainService.listWarehouses(queryInnerDTO.getWarehouseCodes(), tenantId));
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




    @Override
    public void saveExpressQuantity(String warehouseCode, String expressQuantity, Long tenantId) {
        executeScript(WarehouseConstants.WarehouseCache.EXPRESS_LIMIT_COLLECTION,warehouseCode, expressQuantity, tenantId, EXPRESS_LIMIT_CACHE_LUA);
    }

    @Override
    public void savePickUpQuantity(String warehouseCode, String pickUpQuantity, Long tenantId) {
        executeScript(WarehouseConstants.WarehouseCache.PICK_UP_LIMIT_COLLECTION,warehouseCode, pickUpQuantity, tenantId, PICK_UP_LIMIT_CACHE_LUA);
    }

    @Override
    public void updateExpressValue(String warehouseCode, String increment, Long tenantId) {
        executeScript(WarehouseConstants.WarehouseCache.EXPRESS_LIMIT_COLLECTION,warehouseCode, increment, tenantId, EXPRESS_VALUE_CACHE_LUA);
    }

    @Override
    public void updatePickUpValue(String warehouseCode, String increment, Long tenantId) {
        executeScript(WarehouseConstants.WarehouseCache.PICK_UP_LIMIT_COLLECTION,warehouseCode, increment, tenantId, PICK_UP_VALUE_CACHE_LUA);
    }

    @Override
    public String getExpressLimit(String warehouseCode, Long tenantId) {
        return this.redisCacheClient.<String, String>boundHashOps(warehouseCacheKey(warehouseCode, tenantId)).get(WarehouseConstants.WarehouseCache.EXPRESS_LIMIT_QUANTITY);
    }

    @Override
    public String getPickUpLimit(String warehouseCode, Long tenantId) {
        return this.redisCacheClient.<String, String>boundHashOps(warehouseCacheKey(warehouseCode, tenantId)).get(WarehouseConstants.WarehouseCache.PICK_UP_LIMIT_QUANTITY);
    }

    @Override
    public String getExpressValue(String warehouseCode, Long tenantId) {
        return this.redisCacheClient.<String, String>boundHashOps(warehouseCacheKey(warehouseCode, tenantId)).get(WarehouseConstants.WarehouseCache.EXPRESS_LIMIT_VALUE);
    }

    @Override
    public String getPickUpValue(String warehouseCode, Long tenantId) {
        return this.redisCacheClient.<String, String>boundHashOps(warehouseCacheKey(warehouseCode, tenantId)).get(WarehouseConstants.WarehouseCache.PICK_UP_LIMIT_VALUE);
    }


    @Override
    public String warehouseLimitCacheKey(String limit,Long tenantId) {
        if (tenantId == null) {
            return WarehouseConstants.WarehouseCache.warehouseLimitCacheKey(limit,0);
        }
        return WarehouseConstants.WarehouseCache.warehouseLimitCacheKey(limit,tenantId);
    }

    @Override
    public boolean isWarehouseExpressLimit(String warehouseCode, Long tenantId) {
        final Boolean result = this.redisCacheClient.boundSetOps(WarehouseConstants.WarehouseCache.EXPRESS_LIMIT_COLLECTION).isMember(warehouseCacheKey(warehouseCode, tenantId));
        return result != null && result;
    }

    @Override
    public boolean isWarehousePickUpLimit(String warehouseCode, Long tenantId) {
        final Boolean result = this.redisCacheClient.boundSetOps(WarehouseConstants.WarehouseCache.PICK_UP_LIMIT_COLLECTION).isMember(warehouseCacheKey(warehouseCode, tenantId));
        return result != null && result;
    }

    @Override
    public Set<String> expressLimitWarehouseCollection(Long tenantId) {
        final Set<String> members = this.redisCacheClient.boundSetOps(warehouseLimitCacheKey(WarehouseConstants.WarehouseCache.EXPRESS_LIMIT_COLLECTION,tenantId)).members();
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(members)) {
            return new HashSet<>(members);
        } else {
            return null;
        }
    }

    @Override
    public Set<String> pickUpLimitWarehouseCollection(Long tenantId) {
        final Set<String> members = this.redisCacheClient.boundSetOps(warehouseLimitCacheKey(WarehouseConstants.WarehouseCache.PICK_UP_LIMIT_COLLECTION,tenantId)).members();
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(members)) {
            return new HashSet<>(members);
        } else {
            return null;
        }
    }

    @Override
    public void resetWarehouseExpressLimit(String warehouseCode, Long tenantId) {
        executeScript(warehouseCode,WarehouseConstants.WarehouseCache.EXPRESS_LIMIT_COLLECTION,tenantId, EXPRESS_VALUE_CACHE_RESET_LUA);
    }


    @Override
    public void resetWarehousePickUpLimit(String warehouseCode, Long tenantId) {
        executeScript(warehouseCode, WarehouseConstants.WarehouseCache.PICK_UP_LIMIT_COLLECTION,tenantId, PICK_UP_VALUE_CACHE_RESET_LUA);

    }

    @Override
    public List<Carrier> listCarriers(WarehouseRelCarrierQueryDTO queryDTO) {
        return warehouseRepository.listCarriers(queryDTO);
    }

    @Override
    public List<Warehouse> listWarehouseAddr(WarehouseAddrQueryDTO queryDTO) {
        return warehouseRepository.listWarehouseAddr(queryDTO);
    }


    private void executeScript(final String limit,final String warehouseCode, final String num, final Long tenantId, final ScriptSource scriptSource) {
        final DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(scriptSource);
        this.redisCacheClient.execute(defaultRedisScript, Collections.singletonList(warehouseLimitCacheKey(limit, tenantId)), warehouseCode, num, String.valueOf(tenantId), warehouseCacheKey(warehouseCode, tenantId));
    }

    private void executeScript(final String warehouseCode,final String limit,final Long tenantId, final ScriptSource scriptSource) {
        final DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(scriptSource);
        this.redisCacheClient.execute(defaultRedisScript, Collections.singletonList(warehouseLimitCacheKey(limit, tenantId)), warehouseCode, String.valueOf(tenantId), warehouseCacheKey(warehouseCode, tenantId));
    }



    private static final ResourceScriptSource EXPRESS_LIMIT_CACHE_LUA =
            new ResourceScriptSource(new ClassPathResource("script/lua/warehouse/express_limit_cache.lua"));
    private static final ResourceScriptSource EXPRESS_VALUE_CACHE_LUA =
            new ResourceScriptSource(new ClassPathResource("script/lua/warehouse/express_value_cache.lua"));
    private static final ResourceScriptSource EXPRESS_VALUE_CACHE_RESET_LUA =
            new ResourceScriptSource(new ClassPathResource("script/lua/warehouse/express_value_cache_reset.lua"));
    private static final ResourceScriptSource PICK_UP_LIMIT_CACHE_LUA =
            new ResourceScriptSource(new ClassPathResource("script/lua/warehouse/pick_up_limit_cache.lua"));
    private static final ResourceScriptSource PICK_UP_VALUE_CACHE_LUA =
            new ResourceScriptSource(new ClassPathResource("script/lua/warehouse/pick_up_value_cache.lua"));
    private static final ResourceScriptSource PICK_UP_VALUE_CACHE_RESET_LUA =
            new ResourceScriptSource(new ClassPathResource("script/lua/warehouse/pick_up_value_cache_reset.lua"));

    private String warehouseCacheKey(String warehouseCode, Long tenantId) {
        if (tenantId == null) {
            return WarehouseConstants.WarehouseCache.warehouseCacheKey(0);
        }
        return WarehouseConstants.WarehouseCache.warehouseCacheKey(tenantId);
    }

}
