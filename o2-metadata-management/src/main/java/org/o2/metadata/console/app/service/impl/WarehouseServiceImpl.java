package org.o2.metadata.console.app.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.o2.core.helper.JsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.inventory.management.client.O2InventoryClient;
import org.o2.inventory.management.client.domain.constants.O2InventoryConstant;
import org.o2.inventory.management.client.domain.vo.TriggerStockCalWithWhVO;
import org.o2.metadata.console.api.co.WarehouseCO;
import org.o2.metadata.console.api.dto.WarehouseAddrQueryDTO;
import org.o2.metadata.console.api.dto.WarehousePageQueryInnerDTO;
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
        List<TriggerStockCalWithWhVO> triggerCalInfoList = this.buildTriggerCalInfoList(warehouses);
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
            o2InventoryClient.triggerWhStockCalWithWh(tenantId, triggerCalInfoList);
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

    private List<TriggerStockCalWithWhVO> buildTriggerCalInfoList(final List<Warehouse> warehouses) {
        // 触发线上可用库存计算
        List<TriggerStockCalWithWhVO> calInfoList = new ArrayList<>(4);
        for (Warehouse warehouse : warehouses) {
            List<Warehouse> warehouseList = warehouseRepository.selectByCondition(Condition.builder(Warehouse.class).andWhere(Sqls.custom()
                    .andEqualTo(Warehouse.FIELD_WAREHOUSE_ID, warehouse.getWarehouseId())
                    .andEqualTo(Warehouse.FIELD_TENANT_ID, warehouse.getTenantId())).build());
            if (CollectionUtils.isEmpty(warehouseList)) {
                continue;
            }
            Warehouse origin = warehouseList.get(0);
            String warehouseCode = origin.getWarehouseCode();
                if (!warehouse.getWarehouseStatusCode().equals(origin.getWarehouseStatusCode())) {
                    TriggerStockCalWithWhVO vo = new  TriggerStockCalWithWhVO();
                    vo.setTriggerSource(O2InventoryConstant.invCalCase.WH_STATUS);
                    vo.setWarehouseCode(warehouseCode);
                    calInfoList.add(vo);
                }
                if (!warehouse.getActiveFlag().equals(origin.getActiveFlag())) {
                    TriggerStockCalWithWhVO vo = new  TriggerStockCalWithWhVO();
                    vo.setTriggerSource(O2InventoryConstant.invCalCase.WH_ACTIVE);
                    vo.setWarehouseCode(warehouseCode);
                    calInfoList.add(vo);
                }
                if (!warehouse.getExpressedFlag().equals(origin.getExpressedFlag())) {
                    TriggerStockCalWithWhVO vo = new  TriggerStockCalWithWhVO();
                    vo.setTriggerSource(O2InventoryConstant.invCalCase.WH_EXPRESS);
                    vo.setWarehouseCode(warehouseCode);
                    calInfoList.add(vo);
                }
                if (!warehouse.getPickedUpFlag().equals(origin.getPickedUpFlag())) {
                    TriggerStockCalWithWhVO vo = new  TriggerStockCalWithWhVO();
                    vo.setTriggerSource(O2InventoryConstant.invCalCase.WH_PICKUP);
                    vo.setWarehouseCode(warehouseCode);
                    calInfoList.add(vo);
                }

        }
        return calInfoList;
    }



    @Override
    public Integer updateExpressValue(String warehouseCode, String increment, Long tenantId) {
         return warehouseRedis.updateExpressQuantity(warehouseCode,increment,tenantId);
    }

    @Override
    public Integer updatePickUpValue(String warehouseCode, String increment, Long tenantId) {
        return warehouseRedis.updatePickUpValue(warehouseCode,increment,tenantId);
    }


    @Override
    public String warehouseLimitCacheKey(String limit,Long tenantId) {
        if (tenantId == null) {
            return WarehouseConstants.WarehouseCache.getLimitCacheKey(limit,0);
        }
        return WarehouseConstants.WarehouseCache.getLimitCacheKey(limit,tenantId);
    }

    @Override
    public boolean isWarehouseExpressLimit(String warehouseCode, Long tenantId) {
        // 仓库快递配送接单量限制 key
        String expressLimitKey = WarehouseConstants.WarehouseCache.getLimitCacheKey(WarehouseConstants.WarehouseCache.EXPRESS_LIMIT_KEY,tenantId);
        String result = this.redisCacheClient.<String, String>opsForHash().get(expressLimitKey,warehouseCode);
        JSONObject object = JsonHelper.stringToJsonObject(result);
        return  object.getBoolean(WarehouseConstants.WarehouseCache.FLAG);
    }

    @Override
    public boolean isWarehousePickUpLimit(String warehouseCode, Long tenantId) {
        // 仓库快递配送接单量限制 key
        String pickUpLimitKey = WarehouseConstants.WarehouseCache.getLimitCacheKey(WarehouseConstants.WarehouseCache.PICK_UP_LIMIT_KEY,tenantId);
        String result = this.redisCacheClient.<String, String>opsForHash().get(pickUpLimitKey,warehouseCode);
        JSONObject object = JsonHelper.stringToJsonObject(result);
        return  object.getBoolean(WarehouseConstants.WarehouseCache.FLAG);
    }

    @Override
    public Set<String> expressLimitWarehouseCollection(Long tenantId) {
        // 仓库快递配送接单量限制 key
        String expressLimitKey = WarehouseConstants.WarehouseCache.getLimitCacheKey(WarehouseConstants.WarehouseCache.EXPRESS_LIMIT_KEY, tenantId);
        return getWarehouseCodes(expressLimitKey);
    }

    @Override
    public Set<String> pickUpLimitWarehouseCollection(Long tenantId) {
        // 仓库自提单量限制 key
        String pickUpLimitKey = WarehouseConstants.WarehouseCache.getLimitCacheKey(WarehouseConstants.WarehouseCache.PICK_UP_LIMIT_KEY, tenantId);
        return getWarehouseCodes(pickUpLimitKey);
    }
   /**
    * 获取仓库编码
    * @param key redis key
    * @return  map
    */
    private Set<String> getWarehouseCodes(String key) {
        Map<String, String> map = this.redisCacheClient.<String, String>opsForHash().entries(key);
        if (map.isEmpty()) {
            return new HashSet<>(2);
        }
        Set<String> set = new HashSet<>(16);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            JSONObject object = JsonHelper.stringToJsonObject(v);
            boolean flag = object.getBoolean(WarehouseConstants.WarehouseCache.FLAG);
            if (Boolean.TRUE.equals(flag)) {
                set.add(k);
            }
        }
        return set;
    }
    @Override
    public void resetWarehouseExpressLimit(String warehouseCode, Long tenantId) {
        // 仓库快递配送接单量限制 key
        String expressLimitKey = WarehouseConstants.WarehouseCache.getLimitCacheKey(WarehouseConstants.WarehouseCache.EXPRESS_LIMIT_KEY, tenantId);
        this.redisCacheClient.opsForHash().delete(expressLimitKey,warehouseCode);
    }


    @Override
    public void resetWarehousePickUpLimit(String warehouseCode, Long tenantId) {
        // 仓库自提单量限制 key
        String pickUpLimitKey = WarehouseConstants.WarehouseCache.getLimitCacheKey(WarehouseConstants.WarehouseCache.PICK_UP_LIMIT_KEY, tenantId);
        this.redisCacheClient.opsForHash().delete(pickUpLimitKey,warehouseCode);

    }

    @Override
    public List<Carrier> listCarriers(WarehouseRelCarrierQueryDTO queryDTO) {
        return warehouseRepository.listCarriers(queryDTO);
    }

    @Override
    public List<Warehouse> listWarehouseAddr(WarehouseAddrQueryDTO queryDTO) {
        return warehouseRepository.listWarehouseAddr(queryDTO);
    }

    @Override
    public List<WarehouseCO> pageWarehouses(WarehousePageQueryInnerDTO innerDTO) {
        String warehouseId = innerDTO.getWarehouseId();
        if (StringUtils.isNotEmpty(warehouseId)) {
            List<Long> idsList = new ArrayList<>();
            String[] strings = StringUtils.split(warehouseId,BaseConstants.Symbol.COMMA);
            for (String str : strings) {
                Long id = Long.valueOf(str);
                idsList.add(id);
                innerDTO.setWarehouseIdList(idsList);
            }
        }
        String warehouseCode = innerDTO.getWarehouseCode();
        if (StringUtils.isNotEmpty(warehouseCode)) {
            innerDTO.setWarehouseCodeList(Arrays.asList(StringUtils.split(warehouseCode, BaseConstants.Symbol.COMMA)));
        }
        return warehouseRepository.pageWarehouses(innerDTO);
    }

}
