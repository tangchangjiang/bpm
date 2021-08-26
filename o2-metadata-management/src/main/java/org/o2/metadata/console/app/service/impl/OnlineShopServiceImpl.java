package org.o2.metadata.console.app.service.impl;

import com.google.common.base.Preconditions;
import io.choerodon.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.base.BaseConstants;
import org.o2.inventory.management.client.O2InventoryClient;
import org.o2.inventory.management.client.domain.constants.O2InventoryConstant;
import org.o2.metadata.console.api.co.OnlineShopCO;
import org.o2.metadata.console.api.dto.OnlineShopCatalogVersionDTO;
import org.o2.metadata.console.api.dto.OnlineShopQueryInnerDTO;
import org.o2.metadata.console.app.service.OnlineShopRelWarehouseService;
import org.o2.metadata.console.app.service.OnlineShopService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.convertor.OnlineShopConverter;
import org.o2.metadata.console.infra.entity.Catalog;
import org.o2.metadata.console.infra.entity.CatalogVersion;
import org.o2.metadata.console.infra.entity.OnlineShop;
import org.o2.metadata.console.infra.redis.OnlineShopRedis;
import org.o2.metadata.console.infra.repository.CatalogRepository;
import org.o2.metadata.console.infra.repository.CatalogVersionRepository;
import org.o2.metadata.console.infra.repository.OnlineShopRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 网店应用服务默认实现
 * @author yipeng.zhu@hand-china.com 2020-06-03 09:53
 **/
@Slf4j
@Service
public class OnlineShopServiceImpl implements OnlineShopService {
    private final OnlineShopRepository onlineShopRepository;
    private final OnlineShopRelWarehouseService onlineShopRelWarehouseService;
    private final CatalogRepository catalogRepository;
    private final CatalogVersionRepository catalogVersionRepository;
    private O2InventoryClient o2InventoryClient;
    private final OnlineShopRedis onlineShopRedis;

    public OnlineShopServiceImpl(OnlineShopRepository onlineShopRepository,
                                 OnlineShopRelWarehouseService onlineShopRelWarehouseService,
                                 CatalogRepository catalogRepository,
                                 CatalogVersionRepository catalogVersionRepository,
                                 O2InventoryClient o2InventoryClient, OnlineShopRedis onlineShopRedis) {
        this.onlineShopRepository = onlineShopRepository;
        this.onlineShopRelWarehouseService = onlineShopRelWarehouseService;
        this.catalogRepository = catalogRepository;
        this.catalogVersionRepository = catalogVersionRepository;
        this.o2InventoryClient = o2InventoryClient;
        this.onlineShopRedis = onlineShopRedis;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOnlineShop(OnlineShop onlineShop) {
        if (null == onlineShop.getCatalogCode()) {
            throw new CommonException(MetadataConstants.ErrorCode.BASIC_DATA_CATALOG_CODE_IS_NULL);
        }
        Catalog catalog = catalogRepository.selectOne(Catalog.builder().catalogCode(onlineShop.getCatalogCode()).tenantId(onlineShop.getTenantId()).build());
        try {
            onlineShop.setCatalogId(catalog.getCatalogId());
            CatalogVersion catalogVersion = catalogVersionRepository.selectOne(CatalogVersion.builder().catalogVersionCode(onlineShop.getCatalogVersionCode()).tenantId(onlineShop.getTenantId()).build());
            Preconditions.checkArgument(null != catalogVersion, "illegal combination catalogVersionCode && organizationId");
            onlineShop.setCatalogVersionId(catalogVersion.getCatalogVersionId());
            if (MetadataConstants.DefaultShop.DEFAULT.equals(onlineShop.getIsDefault())) {
                onlineShopRepository.updateDefaultShop(onlineShop.getTenantId());
            }
            this.onlineShopRepository.insertSelective(onlineShop);
            onlineShopRedis.updateRedis(onlineShop.getOnlineShopCode(),onlineShop.getTenantId());
        } catch (final DuplicateKeyException e) {
            throw new CommonException(MetadataConstants.ErrorCode.BASIC_DATA_DUPLICATE_CODE, e,
                    "OnlineShop(" + onlineShop.getOnlineShopId() + ")");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOnlineShop(OnlineShop onlineShop) {
        Catalog catalog = catalogRepository.selectOne(Catalog.builder().catalogCode(onlineShop.getCatalogCode()).tenantId(onlineShop.getTenantId()).build());
        Preconditions.checkArgument(null != catalog, "illegal combination catalogCode && organizationId");
        onlineShop.validate(onlineShopRepository);
        if (!onlineShop.exist(onlineShopRepository)) {
            throw new CommonException(BaseConstants.ErrorCode.NOT_FOUND);
        }
        final OnlineShop origin = onlineShopRepository.selectByPrimaryKey(onlineShop);
        onlineShop.setCatalogId(catalog.getCatalogId());
        CatalogVersion catalogVersion = catalogVersionRepository.selectOne(CatalogVersion.builder().catalogId(catalog.getCatalogId()).catalogVersionCode(onlineShop.getCatalogVersionCode()).tenantId(onlineShop.getTenantId()).build());
        Preconditions.checkArgument(null != catalogVersion, "illegal combination catalogId && organizationId && catalogVersionCode");
        onlineShop.setCatalogVersionId(catalogVersion.getCatalogVersionId());
        onlineShop.setCatalogId(catalog.getCatalogId());
        boolean flag = (MetadataConstants.DefaultShop.DEFAULT.equals(onlineShop.getIsDefault())) && (!onlineShop.getIsDefault().equals(origin.getIsDefault()));
        if (flag) {
            onlineShopRepository.updateDefaultShop(onlineShop.getTenantId());
        }
        onlineShopRepository.updateByPrimaryKeySelective(onlineShop);
        if (!onlineShop.getActiveFlag().equals(origin.getActiveFlag())) {
            //触发网店关联仓库更新
            onlineShopRelWarehouseService.updateByShop(onlineShop.getOnlineShopId(), origin.getOnlineShopCode(), onlineShop.getActiveFlag(), onlineShop.getTenantId());
            // 触发渠道可用库存计算
            o2InventoryClient.triggerShopStockCalByShopCode(onlineShop.getTenantId(), Collections.singleton(origin.getOnlineShopCode()), O2InventoryConstant.invCalCase.SHOP_ACTIVE);
        }
        onlineShopRedis.updateRedis(onlineShop.getOnlineShopCode(),onlineShop.getTenantId());
    }

    @Override
    public Map<String,OnlineShopCO> listOnlineShops(OnlineShopQueryInnerDTO onlineShopQueryInnerDTO, Long tenantId) {
        Map<String, OnlineShopCO> map = new HashMap<>(16);
       OnlineShop query = new OnlineShop();
       query.setTenantId(tenantId);
       query.setOnlineShopCodes(onlineShopQueryInnerDTO.getOnlineShopCodes());
       query.setOnlineShopNames(onlineShopQueryInnerDTO.getOnlineShopNames());
       query.setPlatformCode(onlineShopQueryInnerDTO.getPlatformCode());
       List<OnlineShopCO> voList =  OnlineShopConverter.poToCoListObjects(onlineShopRepository.selectByCondition(query));
        if (voList.isEmpty()) {
           return map;
        }
        for (OnlineShopCO co : voList) {
            map.put(co.getOnlineShopCode(),co);
        }
        return  map;
    }

    @Override
    public Map<String, List<OnlineShopCO>> listOnlineShops(List<OnlineShopCatalogVersionDTO> onlineShopCatalogVersionList, Long tenantId) {
        Map<String,List<OnlineShopCO>> map = new HashMap<>(16);
        List<OnlineShopCO> voList = OnlineShopConverter.poToCoListObjects(onlineShopRepository.listOnlineShops(onlineShopCatalogVersionList,tenantId));
        if (voList.isEmpty()) {
            return map;
        }
        for (OnlineShopCO co : voList) {
            String key = co.getCatalogCode() + "-" + co.getCatalogVersionCode();
            List<OnlineShopCO> list = map.getOrDefault(key,new ArrayList<>());
            if (list.isEmpty()) {
                List<OnlineShopCO> onlineShops = new ArrayList<>();
                onlineShops.add(co);
                map.put(key,onlineShops);
                continue;
            }
            list.add(co);
            map.put(key,list);
        }
        return map;
    }
}
