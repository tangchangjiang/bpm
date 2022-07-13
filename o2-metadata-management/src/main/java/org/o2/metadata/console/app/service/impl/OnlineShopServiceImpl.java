package org.o2.metadata.console.app.service.impl;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.core.exception.O2CommonException;
import org.o2.core.helper.TransactionalHelper;
import org.o2.metadata.console.api.co.OnlineShopCO;
import org.o2.metadata.console.api.dto.OnlineShopCatalogVersionDTO;
import org.o2.metadata.console.api.dto.OnlineShopQueryInnerDTO;
import org.o2.metadata.console.app.bo.CurrencyBO;
import org.o2.metadata.console.app.service.LovAdapterService;
import org.o2.metadata.console.app.service.OnlineShopService;
import org.o2.metadata.console.app.service.SourcingCacheUpdateService;
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
import org.o2.metadata.management.client.domain.dto.OnlineShopDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 网店应用服务默认实现
 * @author yipeng.zhu@hand-china.com 2020-06-03 09:53
 **/
@Slf4j
@Service
public class OnlineShopServiceImpl implements OnlineShopService {
    private final OnlineShopRepository onlineShopRepository;
    private final OnlineShopRedis onlineShopRedis;
    private final LovAdapterService lovAdapterService;
    private final TransactionalHelper transactionalHelper;
    private final SourcingCacheUpdateService sourcingCacheService;
    private final CatalogRepository catalogRepository;
    private final CatalogVersionRepository catalogVersionRepository;

    public OnlineShopServiceImpl(OnlineShopRepository onlineShopRepository,
                                 OnlineShopRedis onlineShopRedis,
                                 final LovAdapterService lovAdapterService,
                                 TransactionalHelper transactionalHelper,
                                 SourcingCacheUpdateService sourcingCacheService,
                                 CatalogRepository catalogRepository,
                                 CatalogVersionRepository catalogVersionRepository) {
        this.onlineShopRepository = onlineShopRepository;
        this.onlineShopRedis = onlineShopRedis;
        this.lovAdapterService = lovAdapterService;
        this.transactionalHelper = transactionalHelper;
        this.sourcingCacheService = sourcingCacheService;
        this.catalogRepository = catalogRepository;
        this.catalogVersionRepository = catalogVersionRepository;
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
        if (CollectionUtils.isNotEmpty(currencyCodes)) {
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
    public OnlineShop createOnlineShop(OnlineShop onlineShop) {
        validateOnlineShopCode(onlineShop);
        validateOnlineShopName(onlineShop);
        String code  = onlineShop.getOnlineShopCode();
        String name = onlineShop.getOnlineShopName();
        Integer activeFlag = onlineShop.getActiveFlag();
        Long tenantId = onlineShop.getTenantId();
        // 目录
        Catalog catalog = new Catalog();
        catalog.setCatalogCode(code);
        catalog.setCatalogName(name);
        catalog.setActiveFlag(activeFlag);
        catalog.setTenantId(tenantId);
        Map<String, Map<String, String>>  catalogLanguage = new HashMap<>(2);
        Map<String, String> catalogMap = new HashMap<>(2);
        catalogMap.put(OnlineShopConstants.Language.EN_US,catalog.getCatalogName());
        catalogMap.put(OnlineShopConstants.Language.ZH_CN,catalog.getCatalogName());
        catalogLanguage.put(OnlineShopConstants.Language.CATALOG_NAME,catalogMap);
        catalog.set_tls(catalogLanguage);
        // 目录版本
        CatalogVersion catalogVersion = new CatalogVersion();
        catalogVersion.setCatalogVersionCode(code);
        catalogVersion.setCatalogVersionName(name);
        catalogVersion.setActiveFlag(activeFlag);
        catalogVersion.setTenantId(tenantId);
        Map<String, Map<String, String>>  catalogVersionLanguage = new HashMap<>(2);
        Map<String, String> catalogVersionMap = new HashMap<>(2);
        catalogVersionMap.put(OnlineShopConstants.Language.EN_US,catalog.getCatalogName());
        catalogVersionMap.put(OnlineShopConstants.Language.ZH_CN,catalog.getCatalogName());
        catalogVersionLanguage.put(OnlineShopConstants.Language.CATALOG_VERSION_NAME,catalogVersionMap);
        catalog.set_tls(catalogVersionLanguage);

        onlineShop.setCatalogCode(code);
        onlineShop.setCatalogVersionCode(code);

        transactionalHelper.transactionOperation(() -> {
            if (MetadataConstants.DefaultShop.DEFAULT.equals(onlineShop.getIsDefault())) {
                onlineShopRepository.updateDefaultShop(onlineShop.getTenantId());
            }
            this.onlineShopRepository.insertSelective(onlineShop);
            this.catalogRepository.insert(catalog);
            catalogVersion.setCatalogId(catalog.getCatalogId());
            this.catalogVersionRepository.insert(catalogVersion);
            onlineShopRedis.updateRedis(onlineShop.getOnlineShopCode(), onlineShop.getTenantId());
        });
        sourcingCacheService.refreshSourcingCache(onlineShop.getTenantId(), this.getClass().getSimpleName());
        return onlineShop;
    }

    @Override
    public OnlineShop updateOnlineShop(OnlineShop onlineShop) {
         OnlineShop origin = onlineShopRepository.selectByPrimaryKey(onlineShop);
        // 网店编码变动
        if (!origin.getOnlineShopCode().equals(onlineShop.getOnlineShopCode())) {
            throw new O2CommonException(null, OnlineShopConstants.ErrorCode.ERROR_ONLINE_SHOP_CODE_UPDATE, OnlineShopConstants.ErrorCode.ERROR_ONLINE_SHOP_CODE_UPDATE);
        }
        if (!origin.getOnlineShopName().equals(onlineShop.getOnlineShopName())) {
            validateOnlineShopName(onlineShop);
        }

        String cataLog = origin.getCatalogCode();
        Catalog queryCatalog = new Catalog();
        queryCatalog.setCatalogCode(cataLog);
        queryCatalog.setTenantId(onlineShop.getTenantId());
        Catalog catalogBean = catalogRepository.selectOne(queryCatalog);
        catalogBean.setActiveFlag(onlineShop.getActiveFlag());
        catalogBean.setCatalogName(onlineShop.getOnlineShopName());

        String catalogVersion = origin.getCatalogVersionCode();
        CatalogVersion queryCatalogVersion = new CatalogVersion();
        queryCatalogVersion.setCatalogVersionCode(catalogVersion);
        queryCatalogVersion.setTenantId(onlineShop.getTenantId());
        CatalogVersion queryVersionBean = catalogVersionRepository.selectOne(queryCatalogVersion);
        queryVersionBean.setActiveFlag(onlineShop.getActiveFlag());
        queryVersionBean.setCatalogVersionName(onlineShop.getOnlineShopName());

        boolean flag = (MetadataConstants.DefaultShop.DEFAULT.equals(onlineShop.getIsDefault())) && (!onlineShop.getIsDefault().equals(origin.getIsDefault()));
        transactionalHelper.transactionOperation(() -> {
            onlineShopRepository.updateByPrimaryKeySelective(onlineShop);
            catalogRepository.updateByPrimaryKeySelective(catalogBean);
            catalogVersionRepository.updateByPrimaryKeySelective(queryVersionBean);
            if (flag) {
                onlineShopRepository.updateDefaultShop(onlineShop.getTenantId());
            }
            onlineShopRedis.updateRedis(onlineShop.getOnlineShopCode(), onlineShop.getTenantId());
        });
        sourcingCacheService.refreshSourcingCache(onlineShop.getTenantId(), this.getClass().getSimpleName());
        return onlineShop;
    }

    /**
     * 校验网店编码唯一性
     * @param onlineShop 网店
     */
    private void validateOnlineShopCode(OnlineShop onlineShop) {
        if(null == onlineShop.getOnlineShopCode()){
            return;
        }
        // 租户id+平台编码+平台网店编码
        Sqls sqls = Sqls.custom();
        sqls.andEqualTo(OnlineShop.FIELD_TENANT_ID, onlineShop.getTenantId());
        sqls.andEqualTo(OnlineShop.FIELD_PLATFORM_CODE, onlineShop.getPlatformCode());
        sqls.andEqualTo(OnlineShop.FIELD_PLATFORM_SHOP_CODE, onlineShop.getPlatformShopCode());
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
        // 租户id+平台编码+网店名称
        Sqls sqls = Sqls.custom();
        sqls.andEqualTo(OnlineShop.FIELD_TENANT_ID, onlineShop.getTenantId());
        sqls.andEqualTo(OnlineShop.FIELD_PLATFORM_CODE, onlineShop.getPlatformCode());
        sqls.andEqualTo(OnlineShop.FIELD_ONLINE_SHOP_NAME, onlineShop.getOnlineShopName());
        int number = onlineShopRepository.selectCountByCondition(Condition.builder(OnlineShop.class).andWhere(sqls).build());
        if (number > 0) {
            throw new O2CommonException(null, OnlineShopConstants.ErrorCode.ERROR_ONLINE_SHOP_NAME_UNIQUE, OnlineShopConstants.ErrorCode.ERROR_ONLINE_SHOP_NAME_UNIQUE);
        }
    }

    @Override
    public Map<String, OnlineShopCO> listOnlineShops(OnlineShopQueryInnerDTO onlineShopQueryInnerDTO, Long tenantId) {
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
        Map<String, List<OnlineShopCO>> map = new HashMap<>(16);
        List<OnlineShopCO> voList = OnlineShopConverter.poToCoListObjects(onlineShopRepository.listOnlineShops(onlineShopCatalogVersionList, tenantId));
        if (voList.isEmpty()) {
            return map;
        }
        for (OnlineShopCO co : voList) {
            String key = co.getCatalogCode() + "-" + co.getCatalogVersionCode();
            List<OnlineShopCO> list = map.getOrDefault(key, new ArrayList<>());
            if (list.isEmpty()) {
                List<OnlineShopCO> onlineShops = new ArrayList<>();
                onlineShops.add(co);
                map.put(key, onlineShops);
                continue;
            }
            list.add(co);
            map.put(key, list);
        }
        return map;
    }

    @Override
    public OnlineShopCO saveOnlineShop(OnlineShopDTO onlineShopDTO) {
        OnlineShop onlineShop = OnlineShopConverter.dtoToBoOnlineShop(onlineShopDTO);
        OnlineShop onlineShopQuery = new OnlineShop();
        onlineShopQuery.setOnlineShopCode(onlineShop.getOnlineShopCode());
        onlineShopQuery.setTenantId(onlineShop.getTenantId());
        OnlineShop onlineShopResult = onlineShopRepository.selectOne(onlineShopQuery);

        if (ObjectUtils.isEmpty(onlineShopResult)) {
            OnlineShop query = new OnlineShop();
            query.setOnlineShopName(onlineShopDTO.getOnlineShopName());
            query.setTenantId(onlineShopDTO.getTenantId());
            OnlineShop result = onlineShopRepository.selectOne(query);
            if (ObjectUtils.isEmpty(result)) {

                onlineShopResult = this.createOnlineShop(onlineShop);
            } else {
                return OnlineShopConverter.poToCoObject(result);
            }
        } else {
            onlineShopResult.setOnlineShopName(onlineShop.getOnlineShopName());
            onlineShopResult.setPlatformShopCode(onlineShop.getPlatformShopCode());
            onlineShopResult.setPlatformCode(onlineShop.getPlatformCode());
            onlineShopResult = this.updateOnlineShop(onlineShopResult);
        }
        return OnlineShopConverter.poToCoObject(onlineShopResult);
    }

}
