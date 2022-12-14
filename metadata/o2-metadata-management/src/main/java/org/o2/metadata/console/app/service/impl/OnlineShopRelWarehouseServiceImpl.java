package org.o2.metadata.console.app.service.impl;

import com.google.common.collect.Maps;
import io.choerodon.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseConstants;
import org.o2.core.helper.TransactionalHelper;
import org.o2.inventory.management.client.O2InventoryClient;
import org.o2.inventory.management.client.domain.constants.O2InventoryConstant;
import org.o2.metadata.console.api.co.OnlineShopRelWarehouseCO;
import org.o2.metadata.console.api.dto.OnlineShopRelWarehouseDTO;
import org.o2.metadata.console.api.dto.OnlineShopRelWarehouseInnerDTO;
import org.o2.metadata.console.api.vo.OnlineShopRelWarehouseVO;
import org.o2.metadata.console.app.service.OnlineShopRelWarehouseService;
import org.o2.metadata.console.app.service.SourcingCacheUpdateService;
import org.o2.metadata.console.infra.constant.OnlineShopConstants;
import org.o2.metadata.console.infra.convertor.OnlineShopRelWarehouseConverter;
import org.o2.metadata.console.infra.entity.OnlineShopRelWarehouse;
import org.o2.metadata.console.infra.redis.OnlineShopRedis;
import org.o2.metadata.console.infra.redis.OnlineShopRelWarehouseRedis;
import org.o2.metadata.console.infra.repository.OnlineShopRelWarehouseRepository;
import org.o2.metadata.domain.onlineshop.repository.OnlineShopRelWarehouseDomainRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 网店关联仓库应用服务默认实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Slf4j
@Service
public class OnlineShopRelWarehouseServiceImpl implements OnlineShopRelWarehouseService {
    private final OnlineShopRelWarehouseRepository onlineShopRelWarehouseRepository;
    private final O2InventoryClient o2InventoryClient;
    private final OnlineShopRelWarehouseDomainRepository onlineShopRelWarehouseDomainRepository;
    private final OnlineShopRedis onlineShopRedis;
    private final OnlineShopRelWarehouseRedis onlineShopRelWarehouseRedis;
    private final SourcingCacheUpdateService sourcingCacheService;
    private final TransactionalHelper transactionalHelper;

    public OnlineShopRelWarehouseServiceImpl(OnlineShopRelWarehouseRepository onlineShopRelWarehouseRepository,
                                             O2InventoryClient o2InventoryClient,
                                             OnlineShopRelWarehouseDomainRepository onlineShopRelWarehouseDomainRepository,
                                             OnlineShopRedis onlineShopRedis, OnlineShopRelWarehouseRedis onlineShopRelWarehouseRedis,
                                             SourcingCacheUpdateService sourcingCacheService, TransactionalHelper transactionalHelper) {
        this.onlineShopRelWarehouseRepository = onlineShopRelWarehouseRepository;
        this.o2InventoryClient = o2InventoryClient;
        this.onlineShopRelWarehouseDomainRepository = onlineShopRelWarehouseDomainRepository;
        this.onlineShopRedis = onlineShopRedis;
        this.onlineShopRelWarehouseRedis = onlineShopRelWarehouseRedis;
        this.sourcingCacheService = sourcingCacheService;
        this.transactionalHelper = transactionalHelper;
    }

    @Override
    public List<OnlineShopRelWarehouse> batchInsertSelective(Long organizationId, final List<OnlineShopRelWarehouse> relationships) {
        // 判断是否重复
        List<OnlineShopRelWarehouse> queryList = onlineShopRelWarehouseRepository.selectByShopIdAndWareIdAndPosId(relationships, organizationId);
        if (!CollectionUtils.isEmpty(queryList)) {
            throw new CommonException(BaseConstants.ErrorCode.DATA_EXISTS);
        }
        Set<String> shopCodeSet = new HashSet<>();
        for (OnlineShopRelWarehouse relationship : relationships) {
            relationship.setTenantId(organizationId);
        }
        List<OnlineShopRelWarehouse> list = new ArrayList<>();
        transactionalHelper.transactionOperation(() -> {
            list.addAll(onlineShopRelWarehouseRepository.batchInsertSelective(relationships));
            // 关联查询 网店编码和仓库编码
            List<OnlineShopRelWarehouse> onlineShopRelWarehouses = onlineShopRelWarehouseRepository.selectByShopIdAndWareIdAndPosId(relationships,
                    organizationId);
            for (OnlineShopRelWarehouse warehouse : onlineShopRelWarehouses) {
                shopCodeSet.add(warehouse.getOnlineShopCode());
            }
            onlineShopRedis.batchUpdateShopRelWh(onlineShopRelWarehouses, organizationId, OnlineShopConstants.Redis.UPDATE);
        });
        sourcingCacheService.refreshSourcingCache(organizationId, this.getClass().getSimpleName());
        if (!shopCodeSet.isEmpty()) {
            try {
                o2InventoryClient.triggerShopStockCalByShopCode(organizationId, shopCodeSet, O2InventoryConstant.invCalCase.SHOP_WH_SOURCED);
            } catch (Exception e) {
                log.error(" error.inner.request:o2Inventory#triggerShopStockCalByShopCode,param =[tenantId: {},shopCodeSet: {},triggerSource: {}]",
                        organizationId, shopCodeSet, O2InventoryConstant.invCalCase.SHOP_WH_ACTIVE);
                log.error(e.getMessage(), e);
            }
        }
        return list;
    }

    @Override
    public List<OnlineShopRelWarehouse> batchUpdateByPrimaryKey(Long tenantId, final List<OnlineShopRelWarehouse> relationships) {
        List<OnlineShopRelWarehouse> originList = onlineShopRelWarehouseRepository.selectByShopIdAndWareIdAndPosId(relationships, tenantId);
        if (originList.isEmpty()) {
            throw new CommonException(BaseConstants.ErrorCode.DATA_NOT_EXISTS);
        }
        Map<Long, OnlineShopRelWarehouse> originMap = Maps.newHashMapWithExpectedSize(originList.size());
        for (OnlineShopRelWarehouse rel : originList) {
            originMap.put(rel.getOnlineShopRelWarehouseId(), rel);
        }
        Set<String> shopCodeSet = new HashSet<>();
        for (OnlineShopRelWarehouse relationship : relationships) {
            relationship.setTenantId(tenantId);
            OnlineShopRelWarehouse origin = originMap.get(relationship.getOnlineShopRelWarehouseId());
            if (null == origin) {
                throw new CommonException(BaseConstants.ErrorCode.DATA_NOT_EXISTS);
            }
            if (!relationship.getActiveFlag().equals(origin.getActiveFlag())) {
                shopCodeSet.add(origin.getOnlineShopCode());
            }
            relationship.setWarehouseCode(origin.getWarehouseCode());
            relationship.setOnlineShopCode(origin.getOnlineShopCode());
        }
        List<OnlineShopRelWarehouse> list = new ArrayList<>();
        transactionalHelper.transactionOperation(() -> {
            list.addAll(onlineShopRelWarehouseRepository.batchUpdateByPrimaryKey(relationships));
            onlineShopRedis.batchUpdateShopRelWh(relationships, tenantId, OnlineShopConstants.Redis.UPDATE);
        });
        sourcingCacheService.refreshSourcingCache(tenantId, this.getClass().getSimpleName());
        if (!shopCodeSet.isEmpty()) {
            try {
                o2InventoryClient.triggerShopStockCalByShopCode(tenantId, shopCodeSet, O2InventoryConstant.invCalCase.SHOP_WH_ACTIVE);
            } catch (Exception e) {
                log.error(" error.inner.request:o2Inventory#triggerShopStockCalByShopCode,param =[tenantId: {},shopCodeSet: {},triggerSource: {}]",
                        tenantId, shopCodeSet, O2InventoryConstant.invCalCase.SHOP_WH_ACTIVE);
                log.error(e.getMessage(), e);
            }
        }
        return list;
    }

    @Override
    public List<OnlineShopRelWarehouseCO> listOnlineShopRelWarehouses(String onlineShopCode, Long tenantId) {
        return OnlineShopRelWarehouseConverter.doToCoListObjects(onlineShopRelWarehouseDomainRepository.listOnlineShopRelWarehouses(onlineShopCode,
                tenantId));
    }

    @Override
    public Map<String, List<OnlineShopRelWarehouseCO>> listOnlineShopRelWarehouses(OnlineShopRelWarehouseInnerDTO innerDTO, Long tenantId) {
        List<OnlineShopRelWarehouseCO> onlineShopRelWarehouseList = onlineShopRelWarehouseRedis.listOnlineShopRelWarehouses(innerDTO,
                tenantId);
        return onlineShopRelWarehouseList.stream().collect(Collectors.groupingBy(OnlineShopRelWarehouseCO::getOnlineShopCode));
    }

    @Override
    public List<OnlineShopRelWarehouseVO> listShopPosRelsByOption(Long onlineShopId, OnlineShopRelWarehouseDTO onlineShopRelWarehouseDTO) {
        List<OnlineShopRelWarehouseVO> list = onlineShopRelWarehouseRepository.listShopPosRelsByOption(onlineShopId, onlineShopRelWarehouseDTO);
        for (OnlineShopRelWarehouseVO vo : list) {
            int relActiveFlag = vo.getActiveFlag();
            int warehouseActiveFlag = vo.getWarehouseActiveFlag();
            int onlineActiveFlag = vo.getOnlineShopActiveFlag();
            boolean flag = Objects.equals(relActiveFlag, BaseConstants.Flag.YES) && Objects.equals(warehouseActiveFlag, BaseConstants.Flag.YES)
                    && Objects.equals(onlineActiveFlag, BaseConstants.Flag.YES);
            if (flag) {
                vo.setBusinessActiveFlag(BaseConstants.Flag.YES);
            } else {
                vo.setBusinessActiveFlag(BaseConstants.Flag.NO);
            }
        }
        return list;
    }

    @Override
    public List<OnlineShopRelWarehouse> listByCondition(OnlineShopRelWarehouse query) {
        return onlineShopRelWarehouseRepository.listByCondition(query);
    }

    @Override
    public List<OnlineShopRelWarehouseVO> listShopRelWarehouse(OnlineShopRelWarehouseDTO onlineShopRelWarehouseDTO) {
        return onlineShopRelWarehouseRepository.listOnlineShopRelWarehouseByCondition(onlineShopRelWarehouseDTO);
    }
}
