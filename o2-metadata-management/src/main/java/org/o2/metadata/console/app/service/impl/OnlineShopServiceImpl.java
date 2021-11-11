package org.o2.metadata.console.app.service.impl;

import com.google.common.base.Preconditions;
import io.choerodon.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.core.exception.O2CommonException;
import org.o2.inventory.management.client.O2InventoryClient;
import org.o2.inventory.management.client.domain.constants.O2InventoryConstant;
import org.o2.metadata.console.api.co.OnlineShopCO;
import org.o2.metadata.console.api.dto.OnlineShopCatalogVersionDTO;
import org.o2.metadata.console.api.dto.OnlineShopQueryInnerDTO;
import org.o2.metadata.console.app.bo.CurrencyBO;
import org.o2.metadata.console.app.service.LovAdapterService;
import org.o2.metadata.console.app.service.OnlineShopRelWarehouseService;
import org.o2.metadata.console.app.service.OnlineShopService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.constant.OnlineShopConstants;
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
    private final LovAdapterService lovAdapterService;

    public OnlineShopServiceImpl(OnlineShopRepository onlineShopRepository,
                                 OnlineShopRelWarehouseService onlineShopRelWarehouseService,
                                 CatalogRepository catalogRepository,
                                 CatalogVersionRepository catalogVersionRepository,
                                 O2InventoryClient o2InventoryClient,
                                 OnlineShopRedis onlineShopRedis,
                                 final LovAdapterService lovAdapterService) {
        this.onlineShopRepository = onlineShopRepository;
        this.onlineShopRelWarehouseService = onlineShopRelWarehouseService;
        this.catalogRepository = catalogRepository;
        this.catalogVersionRepository = catalogVersionRepository;
        this.o2InventoryClient = o2InventoryClient;
        this.onlineShopRedis = onlineShopRedis;
        this.lovAdapterService = lovAdapterService;
    }

    @Override
    public List<OnlineShop> selectByCondition(OnlineShop condition) {
        Preconditions.checkArgument(null != condition.getTenantId(), MetadataConstants.ErrorCode.BASIC_DATA_TENANT_ID_IS_NULL);
        List<OnlineShop> onlineShops = onlineShopRepository.selectByCondition(condition);
        List<String> currencyCodes = new ArrayList<>(16);
        for (OnlineShop onlineShop : onlineShops) {
            String currencyCode = onlineShop.getDefaultCurrency();
            currencyCodes.add(currencyCode);
        }
        if(CollectionUtils.isNotEmpty(currencyCodes)) {
            Map<String, CurrencyBO> map = lovAdapterService.findCurrencyByCodes(condition.getTenantId(), currencyCodes);
            for (OnlineShop onlineShop : onlineShops) {
                String currencyCode = onlineShop.getDefaultCurrency();
                CurrencyBO currencyBO = map.get(currencyCode);
                onlineShop.setCurrencyName(null == currencyBO ? null : currencyBO.getName());
            }
        }
        return onlineShops;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OnlineShop createOnlineShop(OnlineShop onlineShop) {
        validateOnlineShopCode(onlineShop);
        validateOnlineShopName(onlineShop);
        Catalog catalog = catalogRepository.selectOne(Catalog.builder().catalogCode(onlineShop.getCatalogCode()).tenantId(onlineShop.getTenantId()).build());
        onlineShop.setCatalogId(catalog.getCatalogId());
        CatalogVersion catalogVersion = catalogVersionRepository.selectOne(CatalogVersion.builder().catalogVersionCode(onlineShop.getCatalogVersionCode()).tenantId(onlineShop.getTenantId()).build());
        onlineShop.setCatalogVersionId(catalogVersion.getCatalogVersionId());
        if (MetadataConstants.DefaultShop.DEFAULT.equals(onlineShop.getIsDefault())) {
            onlineShopRepository.updateDefaultShop(onlineShop.getTenantId());
        }
        this.onlineShopRepository.insertSelective(onlineShop);
        onlineShopRedis.updateRedis(onlineShop.getOnlineShopCode(), onlineShop.getTenantId());
        return onlineShop;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OnlineShop updateOnlineShop(OnlineShop onlineShop) {
         OnlineShop origin = onlineShopRepository.selectByPrimaryKey(onlineShop);
        // 网店编码变动
        if (!origin.getOnlineShopCode().equals(onlineShop.getOnlineShopCode())){
            throw new O2CommonException(null, OnlineShopConstants.ErrorCode.ERROR_ONLINE_SHOP_CODE_UPDATE, OnlineShopConstants.ErrorCode.ERROR_ONLINE_SHOP_CODE_UPDATE);
        }
        if (!origin.getOnlineShopName().equals(onlineShop.getOnlineShopName())) {
            validateOnlineShopName(onlineShop);
        }
        // 目录
        if (!origin.getCatalogCode().equals(onlineShop.getCatalogCode())) {
            Catalog catalog = catalogRepository.selectOne(Catalog.builder().catalogCode(onlineShop.getCatalogCode()).tenantId(onlineShop.getTenantId()).build());
            onlineShop.setCatalogId(catalog.getCatalogId());
        }
        // 目录版本
        if (!origin.getCatalogVersionCode().equals(onlineShop.getCatalogVersionCode())) {
            CatalogVersion catalogVersion = catalogVersionRepository.selectOne(CatalogVersion.builder().catalogVersionCode(onlineShop.getCatalogVersionCode()).tenantId(onlineShop.getTenantId()).build());
            onlineShop.setCatalogVersionId(catalogVersion.getCatalogVersionId());
        }
        boolean flag = (MetadataConstants.DefaultShop.DEFAULT.equals(onlineShop.getIsDefault())) && (!onlineShop.getIsDefault().equals(origin.getIsDefault()));
        if (flag) {
            onlineShopRepository.updateDefaultShop(onlineShop.getTenantId());
        }
        onlineShopRepository.updateByPrimaryKeySelective(onlineShop);
        if (!onlineShop.getActiveFlag().equals(origin.getActiveFlag())) {
            // 触发渠道可用库存计算
            o2InventoryClient.triggerShopStockCalByShopCode(onlineShop.getTenantId(), Collections.singleton(origin.getOnlineShopCode()), O2InventoryConstant.invCalCase.SHOP_ACTIVE);
        }
        onlineShopRedis.updateRedis(onlineShop.getOnlineShopCode(),onlineShop.getTenantId());
        return onlineShop;
    }

    /**
     * 校验网店编码唯一性
     * @param onlineShop 网店
     */
    private void validateOnlineShopCode(OnlineShop onlineShop) {
        Sqls sqls = Sqls.custom();
        sqls.andEqualTo(OnlineShop.FIELD_ONLINE_SHOP_CODE, onlineShop.getOnlineShopCode());
        sqls.andEqualTo(OnlineShop.FIELD_TENANT_ID, onlineShop.getTenantId());
        int number = onlineShopRepository.selectCountByCondition(Condition.builder(OnlineShop.class).andWhere(sqls).build());
        if (number > 0) {
            throw new O2CommonException(null, OnlineShopConstants.ErrorCode.ERROR_ONLINE_SHOP_CODE_UNIQUE, OnlineShopConstants.ErrorCode.ERROR_ONLINE_SHOP_CODE_UNIQUE);
        }
    }

    /**
     * 校验网店名称唯一性
     * @param onlineShop 网店
     */
    private void validateOnlineShopName(OnlineShop onlineShop) {
        Sqls sqls = Sqls.custom();
        sqls.andEqualTo(OnlineShop.FIELD_ONLINE_SHOP_NAME, onlineShop.getOnlineShopName());
        sqls.andEqualTo(OnlineShop.FIELD_TENANT_ID, onlineShop.getTenantId());
        sqls.andEqualTo(OnlineShop.FIELD_PLATFORM_CODE,onlineShop.getPlatformCode());
        int number = onlineShopRepository.selectCountByCondition(Condition.builder(OnlineShop.class).andWhere(sqls).build());
        if (number > 0) {
            throw new O2CommonException(null, OnlineShopConstants.ErrorCode.ERROR_ONLINE_SHOP_NAME_UNIQUE, OnlineShopConstants.ErrorCode.ERROR_ONLINE_SHOP_NAME_UNIQUE);
        }
    }

    @Override
    public Map<String,OnlineShopCO> listOnlineShops(OnlineShopQueryInnerDTO onlineShopQueryInnerDTO, Long tenantId) {
        Map<String, OnlineShopCO> map = new HashMap<>(16);
        List<OnlineShopCO> voList = OnlineShopConverter.poToCoListObjects(onlineShopRepository.listOnlineShops(onlineShopQueryInnerDTO, tenantId));
        if (voList.isEmpty()) {
            return map;
        }
        for (OnlineShopCO co : voList) {
            map.put(co.getOnlineShopCode(), co);
        }
        return map;
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
