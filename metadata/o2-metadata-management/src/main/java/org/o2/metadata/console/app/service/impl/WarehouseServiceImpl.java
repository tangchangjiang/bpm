package org.o2.metadata.console.app.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import io.choerodon.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.core.exception.O2CommonException;
import org.o2.core.helper.JsonHelper;
import org.o2.core.helper.TransactionalHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.inventory.management.client.O2InventoryClient;
import org.o2.inventory.management.client.domain.constants.O2InventoryConstant;
import org.o2.inventory.management.client.domain.vo.TriggerStockCalWithWhVO;
import org.o2.metadata.console.api.co.WarehouseCO;
import org.o2.metadata.console.api.co.WarehouseRelAddressCO;
import org.o2.metadata.console.api.dto.WarehouseAddrQueryDTO;
import org.o2.metadata.console.api.dto.WarehousePageQueryInnerDTO;
import org.o2.metadata.console.api.dto.WarehouseQueryInnerDTO;
import org.o2.metadata.console.api.dto.WarehouseRelCarrierQueryDTO;
import org.o2.metadata.console.app.bo.WarehouseLimitBO;
import org.o2.metadata.console.app.service.SourcingCacheUpdateService;
import org.o2.metadata.console.app.service.WarehouseService;
import org.o2.metadata.console.infra.constant.PosConstants;
import org.o2.metadata.console.infra.constant.WarehouseConstants;
import org.o2.metadata.console.infra.convertor.WarehouseConverter;
import org.o2.metadata.console.infra.entity.Carrier;
import org.o2.metadata.console.infra.entity.Pos;
import org.o2.metadata.console.infra.entity.Warehouse;
import org.o2.metadata.console.infra.redis.PosRedis;
import org.o2.metadata.console.infra.redis.WarehouseRedis;
import org.o2.metadata.console.infra.repository.PosRepository;
import org.o2.metadata.console.infra.repository.WarehouseRepository;
import org.o2.metadata.domain.warehouse.service.WarehouseDomainService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final O2InventoryClient o2InventoryClient;
    private final RedisCacheClient redisCacheClient;
    private final WarehouseDomainService warehouseDomainService;
    private final WarehouseRedis warehouseRedis;
    private final PosRedis posRedis;
    private final PosRepository posRepository;
    private final SourcingCacheUpdateService sourcingCacheService;
    private final TransactionalHelper transactionalHelper;

    public WarehouseServiceImpl(final WarehouseRepository warehouseRepository,
                                final O2InventoryClient o2InventoryClient,
                                final RedisCacheClient redisCacheClient,
                                WarehouseDomainService warehouseDomainService, WarehouseRedis warehouseRedis,
                                PosRedis posRedis,
                                PosRepository posRepository, SourcingCacheUpdateService sourcingCacheService, TransactionalHelper transactionalHelper) {
        this.warehouseRepository = warehouseRepository;
        this.o2InventoryClient = o2InventoryClient;
        this.redisCacheClient = redisCacheClient;
        this.warehouseDomainService = warehouseDomainService;
        this.warehouseRedis = warehouseRedis;
        this.posRedis = posRedis;
        this.posRepository = posRepository;
        this.sourcingCacheService = sourcingCacheService;
        this.transactionalHelper = transactionalHelper;
    }

    @Override
    public List<Warehouse> createBatch(final Long tenantId, final List<Warehouse> warehouses) {
        List<String> warehouseCodes = Lists.newArrayListWithExpectedSize(warehouses.size());
        // 中台页面 控制了不能批量新建
        for (Warehouse warehouse : warehouses) {
            validNameUnique(warehouse);
            // 校验一个门店服务点只能关联一个仓库，不能重复
            checkPosMatchWarehouse(warehouse);
            warehouseCodes.add(warehouse.getWarehouseCode());
        }
        transactionalHelper.transactionOperation(() -> {
            warehouseRepository.batchInsertSelective(warehouses);
            warehouseRedis.batchUpdateWarehouse(warehouseCodes, tenantId);
            // 更新服务点门店Redis
            List<String> posCodes = warehouses.stream().map(Warehouse::getPosCode).collect(Collectors.toList());
            posRedis.updatePosDetail(null, posCodes, tenantId);
        });
        sourcingCacheService.refreshSourcingCache(tenantId, this.getClass().getSimpleName());
        return warehouses;
    }

    @Override
    public List<Warehouse> batchSave(Long tenantId, List<Warehouse> warehouses) {
        Warehouse warehouse = warehouses.iterator().next();
        if (warehouse.getWarehouseId() == null) {
            createBatch(tenantId, warehouses);
        } else {
            updateBatch(tenantId, warehouses);
        }
        return warehouses;
    }

    @Override
    public List<Warehouse> updateBatch(final Long tenantId, final List<Warehouse> warehouses) {
        List<Warehouse> list = new ArrayList<>();
        transactionalHelper.transactionOperation(() -> {
            // 更新MySQL
            list.addAll(warehouseRepository.batchUpdateByPrimaryKey(warehouses));
            List<String> warehouseCodes = Lists.newArrayListWithExpectedSize(warehouses.size());
            for (Warehouse warehouse : warehouses) {
                // 中台页面 控制了不能批量更新
                validNameUnique(warehouse);
                checkPosMatchWarehouse(warehouse);
                warehouseCodes.add(warehouse.getWarehouseCode());
            }
            // 更新 redis
            warehouseRedis.batchUpdateWarehouse(warehouseCodes, tenantId);
        });
        // 更新服务点门店Redis
        sourcingCacheService.refreshSourcingCache(tenantId, this.getClass().getSimpleName());
        return list;

    }

    /**
     * 名称校验唯一性
     *
     * @param warehouse 仓库
     */
    private void validNameUnique(Warehouse warehouse) {
        if (null != warehouse.getWarehouseId()) {
            Warehouse original = warehouseRepository.selectByPrimaryKey(warehouse);
            if (original.getWarehouseName().equals(warehouse.getWarehouseName())) {
                return;
            }
        }
        Warehouse query = new Warehouse();
        query.setWarehouseName(warehouse.getWarehouseName());
        query.setTenantId(warehouse.getTenantId());
        List<Warehouse> list = warehouseRepository.select(query);
        if (!list.isEmpty()) {
            throw new O2CommonException(null, WarehouseConstants.ErrorCode.ERROR_WAREHOUSE_NAME_DUPLICATE, WarehouseConstants.ErrorCode.ERROR_WAREHOUSE_NAME_DUPLICATE);
        }
    }

    /**
     * 校验一个门店服务点只能关联一个仓库，不能重复
     *
     * @param warehouse 仓库
     */
    private void checkPosMatchWarehouse(Warehouse warehouse) {
        Long posId = warehouse.getPosId();
        Pos pos = posRepository.selectByPrimaryKey(posId);
        String posTypeCode = pos.getPosTypeCode();
        String posStatusCode = pos.getPosStatusCode();
        if (PosConstants.PosTypeCode.STORE.equals(posTypeCode) &&
                !PosConstants.PosStatusCode.CLOSE.equals(posStatusCode)) {
            List<Warehouse> query = warehouseRepository.selectByCondition(Condition.builder(Warehouse.class)
                    .andWhere(Sqls.custom()
                            .andEqualTo(Warehouse.FIELD_TENANT_ID, warehouse.getTenantId())
                            .andEqualTo(Warehouse.FIELD_POS_ID, posId))
                    .build());
            if (null == warehouse.getWarehouseId() && !query.isEmpty()) {
                throw new CommonException(WarehouseConstants.ErrorCode.ERROR_WAREHOUSE_REL_POS_NOT_UNIQUE);
            }
            if (null != warehouse.getWarehouseId() && query.size() > BaseConstants.Digital.ONE) {
                throw new CommonException(WarehouseConstants.ErrorCode.ERROR_WAREHOUSE_REL_POS_NOT_UNIQUE);
            }
        }
    }

    @Override
    public void triggerWhStockCalWithWh(Long tenantId, List<Warehouse> oldWarehouses, List<Warehouse> newWarehouses) {
        // 准备触发线上可用库存计算的数据
        List<TriggerStockCalWithWhVO> triggerCalInfoList = this.buildTriggerCalInfoList(oldWarehouses, newWarehouses);
        // 触发线上可用库存计算
        if (CollectionUtils.isNotEmpty(triggerCalInfoList)) {
            try {
                o2InventoryClient.triggerWhStockCalWithWh(tenantId, triggerCalInfoList);
            } catch (Exception e) {
                log.error(" error.inner.request:o2Inventory#triggerWhStockCalWithWh,param =[tenantId: {},calInfoList: {}]", tenantId, JsonHelper.objectToString(triggerCalInfoList));
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public List<WarehouseCO> listWarehouses(WarehouseQueryInnerDTO queryInnerDTO, Long tenantId) {
        Boolean dbFlag = queryInnerDTO.getDbFlag();
        // 查询数据库
        if (null == dbFlag || Boolean.TRUE.equals(dbFlag)) {
            // 通过网店或者寻源 查询有效的网店,
            if ((Boolean.TRUE.equals(queryInnerDTO.getSourcingFlag()) || StringUtils.isNotEmpty(queryInnerDTO.getOnlineShopCode()))
                    && BaseConstants.Flag.YES.equals(queryInnerDTO.getActiveFlag())) {
                return WarehouseConverter.poToCoListObjects(warehouseRepository.listActiveWarehouseByShopCode(queryInnerDTO.getOnlineShopCode(), tenantId));
            }
            return WarehouseConverter.poToCoListObjects(warehouseRepository.listWarehouses(queryInnerDTO, tenantId));
        }
        // 查询redis
        return WarehouseConverter.doToCoListObjects(warehouseDomainService.listWarehouses(queryInnerDTO.getWarehouseCodes(), tenantId));
    }

    private List<TriggerStockCalWithWhVO> buildTriggerCalInfoList(final List<Warehouse> oldWarehouses, List<Warehouse> newWarehouses) {
        // 触发线上可用库存计算
        List<TriggerStockCalWithWhVO> calInfoList = new ArrayList<>(4);
        Map<String, Warehouse> map = new HashMap<>(16);
        for (Warehouse warehouse : newWarehouses) {
            map.put(warehouse.getWarehouseCode(), warehouse);
        }
        for (Warehouse warehouse : oldWarehouses) {
            Warehouse old = map.get(warehouse.getWarehouseCode());
            if (null == old) {
                continue;
            }
            String warehouseCode = old.getWarehouseCode();
            if (!warehouse.getWarehouseStatusCode().equals(old.getWarehouseStatusCode())) {
                TriggerStockCalWithWhVO vo = new TriggerStockCalWithWhVO();
                vo.setTriggerSource(O2InventoryConstant.invCalCase.WH_STATUS);
                vo.setWarehouseCode(warehouseCode);
                calInfoList.add(vo);
            }
            if (!warehouse.getActiveFlag().equals(old.getActiveFlag())) {
                TriggerStockCalWithWhVO vo = new TriggerStockCalWithWhVO();
                vo.setTriggerSource(O2InventoryConstant.invCalCase.WH_ACTIVE);
                vo.setWarehouseCode(warehouseCode);
                calInfoList.add(vo);
            }
            if (!warehouse.getExpressedFlag().equals(old.getExpressedFlag())) {
                TriggerStockCalWithWhVO vo = new TriggerStockCalWithWhVO();
                vo.setTriggerSource(O2InventoryConstant.invCalCase.WH_EXPRESS);
                vo.setWarehouseCode(warehouseCode);
                calInfoList.add(vo);
            }
            if (!warehouse.getPickedUpFlag().equals(old.getPickedUpFlag())) {
                TriggerStockCalWithWhVO vo = new TriggerStockCalWithWhVO();
                vo.setTriggerSource(O2InventoryConstant.invCalCase.WH_PICKUP);
                vo.setWarehouseCode(warehouseCode);
                calInfoList.add(vo);
            }

        }
        return calInfoList;
    }

    @Override
    public Long updateExpressValue(String warehouseCode, String increment, Long tenantId) {
        return warehouseRedis.updateExpressQuantity(warehouseCode, increment, tenantId);
    }

    @Override
    public Long updatePickUpValue(String warehouseCode, String increment, Long tenantId) {
        return warehouseRedis.updatePickUpValue(warehouseCode, increment, tenantId);
    }

    @Override
    public String warehouseLimitCacheKey(String limit, Long tenantId) {
        if (tenantId == null) {
            return WarehouseConstants.WarehouseCache.getLimitCacheKey(limit, 0);
        }
        return WarehouseConstants.WarehouseCache.getLimitCacheKey(limit, tenantId);
    }

    @Override
    public boolean isWarehouseExpressLimit(String warehouseCode, Long tenantId) {
        // 仓库快递配送接单量限制 key
        String expressLimitKey = WarehouseConstants.WarehouseCache.getLimitCacheKey(WarehouseConstants.WarehouseCache.EXPRESS_LIMIT_KEY, tenantId);
        String result = this.redisCacheClient.<String, String>opsForHash().get(expressLimitKey, warehouseCode);
        JSONObject object = JsonHelper.stringToJsonObject(result);
        return object.getBoolean(WarehouseConstants.WarehouseCache.FLAG);
    }

    @Override
    public boolean isWarehousePickUpLimit(String warehouseCode, Long tenantId) {
        // 仓库快递配送接单量限制 key
        String pickUpLimitKey = WarehouseConstants.WarehouseCache.getLimitCacheKey(WarehouseConstants.WarehouseCache.PICK_UP_LIMIT_KEY, tenantId);
        String result = this.redisCacheClient.<String, String>opsForHash().get(pickUpLimitKey, warehouseCode);
        JSONObject object = JsonHelper.stringToJsonObject(result);
        return object.getBoolean(WarehouseConstants.WarehouseCache.FLAG);
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
     *
     * @param key redis key
     * @return map
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
            WarehouseLimitBO bo = JsonHelper.stringToObject(v, WarehouseLimitBO.class);
            if (Boolean.TRUE.equals(bo.getLimitFlag())) {
                set.add(k);
            }
        }
        return set;
    }

    @Override
    public void resetWarehouseExpressLimit(String warehouseCode, Long tenantId) {
        // 仓库快递配送接单量限制 key
        String expressLimitKey = WarehouseConstants.WarehouseCache.getLimitCacheKey(WarehouseConstants.WarehouseCache.EXPRESS_LIMIT_KEY, tenantId);
        this.redisCacheClient.opsForHash().delete(expressLimitKey, warehouseCode);
    }

    @Override
    public void resetWarehousePickUpLimit(String warehouseCode, Long tenantId) {
        // 仓库自提单量限制 key
        String pickUpLimitKey = WarehouseConstants.WarehouseCache.getLimitCacheKey(WarehouseConstants.WarehouseCache.PICK_UP_LIMIT_KEY, tenantId);
        this.redisCacheClient.opsForHash().delete(pickUpLimitKey, warehouseCode);

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
            String[] strings = StringUtils.split(warehouseId, BaseConstants.Symbol.COMMA);
            for (String str : strings) {
                Long id = Long.valueOf(str);
                idsList.add(id);
                innerDTO.setWarehouseIdList(idsList);
            }
        }
        String warehouseCode = innerDTO.getWarehouseCode();
        if (StringUtils.isNotEmpty(warehouseCode)) {
            List<String> codeList = Arrays.asList(StringUtils.split(warehouseCode, BaseConstants.Symbol.COMMA));
            if (codeList.size() == 1) {
                innerDTO.setWarehouseCode(warehouseCode);
                innerDTO.setWarehouseCodeList(null);
            } else {
                innerDTO.setWarehouseCodeList(codeList);
                innerDTO.setWarehouseCode(null);
            }
        }
        return warehouseRepository.pageWarehouses(innerDTO);
    }

    @Override
    public List<WarehouseRelAddressCO> selectAllDeliveryWarehouse(Long tenantId) {
        return warehouseRepository.selectAllDeliveryWarehouse(tenantId);
    }

    @Override
    public List<Warehouse> selectByCondition(Warehouse query) {
        return warehouseRepository.listWarehouseByCondition(query);
    }

    @Override
    public List<WarehouseCO> listWarehousesByPosCode(List<String> posCodes, Long tenantId) {
        return WarehouseConverter.poToCoListObjects(warehouseRepository.listWarehousesByPosCode(posCodes, tenantId));
    }

}
