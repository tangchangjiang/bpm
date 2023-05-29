package org.o2.metadata.console.app.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.cms.queue.client.O2CmsProducer;
import org.o2.cms.queue.client.domain.SiteRelShopBO;
import org.o2.core.O2CoreConstants;
import org.o2.core.exception.O2CommonException;
import org.o2.core.helper.TransactionalHelper;
import org.o2.ecp.order.b2c.management.client.O2EcpOrderB2cManagementClient;
import org.o2.ecp.order.b2c.management.client.domain.dto.EcpInteractionContext;
import org.o2.ecp.order.b2c.management.client.infra.constants.EcpOrderClientConstants;
import org.o2.metadata.console.api.co.OnlineShopCO;
import org.o2.metadata.console.api.co.SystemParameterCO;
import org.o2.metadata.console.api.dto.OnlineShopCatalogVersionDTO;
import org.o2.metadata.console.api.dto.OnlineShopQueryInnerDTO;
import org.o2.metadata.console.app.bo.CurrencyBO;
import org.o2.metadata.console.app.bo.MerchantInfoBO;
import org.o2.metadata.console.app.service.LovAdapterService;
import org.o2.metadata.console.app.service.OnlineShopService;
import org.o2.metadata.console.app.service.PlatformService;
import org.o2.metadata.console.app.service.PosService;
import org.o2.metadata.console.app.service.SourcingCacheUpdateService;
import org.o2.metadata.console.app.service.SysParamService;
import org.o2.metadata.console.app.service.WarehouseService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.constant.OnlineShopConstants;
import org.o2.metadata.console.infra.constant.SystemParameterConstants;
import org.o2.metadata.console.infra.convertor.OnlineShopConverter;
import org.o2.metadata.console.infra.entity.Catalog;
import org.o2.metadata.console.infra.entity.CatalogVersion;
import org.o2.metadata.console.infra.entity.OnlineShop;
import org.o2.metadata.console.infra.entity.OnlineShopRelWarehouse;
import org.o2.metadata.console.infra.entity.Platform;
import org.o2.metadata.console.infra.entity.Pos;
import org.o2.metadata.console.infra.entity.Warehouse;
import org.o2.metadata.console.infra.redis.OnlineShopRedis;
import org.o2.metadata.console.infra.repository.CatalogRepository;
import org.o2.metadata.console.infra.repository.CatalogVersionRepository;
import org.o2.metadata.console.infra.repository.OnlineShopRelWarehouseRepository;
import org.o2.metadata.console.infra.repository.OnlineShopRepository;
import org.o2.metadata.console.infra.repository.WarehouseRepository;
import org.o2.metadata.management.client.domain.dto.OnlineShopDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 网店应用服务默认实现
 *
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
    private final PlatformService platformService;
    private final PosService posService;
    private final WarehouseService warehouseService;
    private final WarehouseRepository warehouseRepository;
    private final OnlineShopRelWarehouseRepository onlineShopRelWarehouseRepository;
    private final O2CmsProducer o2CmsProducer;
    private final SysParamService sysParamService;
    private final O2EcpOrderB2cManagementClient orderB2cManagementClient;

    public OnlineShopServiceImpl(OnlineShopRepository onlineShopRepository,
                                 OnlineShopRedis onlineShopRedis,
                                 final LovAdapterService lovAdapterService,
                                 TransactionalHelper transactionalHelper,
                                 SourcingCacheUpdateService sourcingCacheService,
                                 CatalogRepository catalogRepository,
                                 CatalogVersionRepository catalogVersionRepository,
                                 PlatformService platformService,
                                 PosService posService,
                                 WarehouseService warehouseService,
                                 WarehouseRepository warehouseRepository,
                                 OnlineShopRelWarehouseRepository onlineShopRelWarehouseRepository,
                                 O2CmsProducer o2CmsProducer,
                                 SysParamService sysParamService, O2EcpOrderB2cManagementClient orderB2cManagementClient) {
        this.onlineShopRepository = onlineShopRepository;
        this.onlineShopRedis = onlineShopRedis;
        this.lovAdapterService = lovAdapterService;
        this.transactionalHelper = transactionalHelper;
        this.sourcingCacheService = sourcingCacheService;
        this.catalogRepository = catalogRepository;
        this.catalogVersionRepository = catalogVersionRepository;
        this.platformService = platformService;
        this.posService = posService;
        this.warehouseService = warehouseService;
        this.warehouseRepository = warehouseRepository;
        this.onlineShopRelWarehouseRepository = onlineShopRelWarehouseRepository;
        this.o2CmsProducer = o2CmsProducer;
        this.sysParamService = sysParamService;
        this.orderB2cManagementClient = orderB2cManagementClient;
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
        validatePlatformOnlineShopCode(onlineShop);
        validateOnlineShopName(onlineShop);
        String code = onlineShop.getOnlineShopCode();
        // 目录
        Catalog catalog = buildCatalog(onlineShop);
        // 目录版本
        CatalogVersion catalogVersion = buildCatalogVersion(onlineShop);

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
        pushDefaultSiteRelShop(onlineShop);
        sourcingCacheService.refreshSourcingCache(onlineShop.getTenantId(), this.getClass().getSimpleName());
        return onlineShop;
    }

    @Override
    public OnlineShop updateOnlineShop(OnlineShop onlineShop) {
        OnlineShop origin = onlineShopRepository.selectByPrimaryKey(onlineShop);
        // 网店编码变动
        if (!origin.getOnlineShopCode().equals(onlineShop.getOnlineShopCode())) {
            throw new O2CommonException(null, OnlineShopConstants.ErrorCode.ERROR_ONLINE_SHOP_CODE_UPDATE,
                    OnlineShopConstants.ErrorCode.ERROR_ONLINE_SHOP_CODE_UPDATE);
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

        boolean flag =
                (MetadataConstants.DefaultShop.DEFAULT.equals(onlineShop.getIsDefault())) && (!onlineShop.getIsDefault().equals(origin.getIsDefault()));
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
     * 构建并验证网店信息
     *
     * @param merchantInfo 商家信息
     * @return 网店信息
     */
    protected OnlineShop buildAndVerifyOnlineShop(MerchantInfoBO merchantInfo) {
        OnlineShop onlineShop = new OnlineShop();
        onlineShop.initDefaultProperties();
        onlineShop.setPlatformCode(O2CoreConstants.PlatformFrom.OW);
        onlineShop.setOnlineShopCode(merchantInfo.getOnlineShopCode());
        onlineShop.setOnlineShopName(merchantInfo.getOnlineShopName());
        onlineShop.setActiveFlag(merchantInfo.getActiveFlag());
        onlineShop.setOnlineShopType(MetadataConstants.OnlineShopType.ONLINE_SHOP);
        onlineShop.setBusinessTypeCode(O2CoreConstants.BusinessType.B2C);
        onlineShop.setIsDefault(BaseConstants.Flag.NO);
        onlineShop.setDefaultCurrency(MetadataConstants.Currency.CNY);
        onlineShop.setTenantId(merchantInfo.getTenantId());
        onlineShop.setCatalogCode(merchantInfo.getOnlineShopCode());
        onlineShop.setCatalogVersionCode(merchantInfo.getOnlineShopCode());
        onlineShop.setLogoUrl(merchantInfo.getLogoUrl());
        onlineShop.setShopMediaUrl(merchantInfo.getShopMediaUrl());
        onlineShop.setSelfSalesFlag(merchantInfo.getSelfSalesFlag());
        if (MapUtils.isNotEmpty(merchantInfo.getOnlineShopNameTls())) {
            Map<String, Map<String, String>> tls = Maps.newHashMap();
            tls.put(OnlineShop.FIELD_ONLINE_SHOP_NAME, merchantInfo.getOnlineShopNameTls());
            onlineShop.set_tls(tls);
        }
        validateOnlineShopCode(onlineShop);
        validatePlatformOnlineShopCode(onlineShop);
        validateOnlineShopName(onlineShop);
        return onlineShop;
    }

    /**
     * 构建目录信息
     *
     * @param onlineShop 网店信息
     * @return 目录信息
     */
    protected Catalog buildCatalog(OnlineShop onlineShop) {
        Catalog catalog = new Catalog();
        catalog.setCatalogCode(onlineShop.getOnlineShopCode());
        catalog.setCatalogName(onlineShop.getOnlineShopName());
        catalog.setActiveFlag(onlineShop.getActiveFlag());
        catalog.setTenantId(onlineShop.getTenantId());
        Map<String, Map<String, String>> catalogLanguage = new HashMap<>(2);
        Map<String, String> catalogMap = new HashMap<>(2);
        catalogMap.put(OnlineShopConstants.Language.EN_US, catalog.getCatalogName());
        catalogMap.put(OnlineShopConstants.Language.ZH_CN, catalog.getCatalogName());
        catalogLanguage.put(OnlineShopConstants.Language.CATALOG_NAME, catalogMap);
        catalog.set_tls(catalogLanguage);
        return catalog;
    }

    /**
     * 构建目录版本信息
     *
     * @param onlineShop 网店信息
     * @return 目录版本信息
     */
    protected CatalogVersion buildCatalogVersion(OnlineShop onlineShop) {
        CatalogVersion catalogVersion = new CatalogVersion();
        catalogVersion.setCatalogVersionCode(onlineShop.getOnlineShopCode());
        catalogVersion.setCatalogVersionName(onlineShop.getOnlineShopName());
        catalogVersion.setActiveFlag(onlineShop.getActiveFlag());
        catalogVersion.setTenantId(onlineShop.getTenantId());
        Map<String, Map<String, String>> catalogVersionLanguage = new HashMap<>(2);
        Map<String, String> catalogVersionMap = new HashMap<>(2);
        catalogVersionMap.put(OnlineShopConstants.Language.EN_US, catalogVersion.getCatalogVersionName());
        catalogVersionMap.put(OnlineShopConstants.Language.ZH_CN, catalogVersion.getCatalogVersionName());
        catalogVersionLanguage.put(OnlineShopConstants.Language.CATALOG_VERSION_NAME, catalogVersionMap);
        catalogVersion.set_tls(catalogVersionLanguage);
        return catalogVersion;
    }

    /**
     * 网店编码唯一性校验
     *
     * @param onlineShop 网店
     */
    protected void validateOnlineShopCode(OnlineShop onlineShop) {
        if (null == onlineShop.getOnlineShopCode()) {
            throw new O2CommonException(null, OnlineShopConstants.ErrorCode.ERROR_ONLINE_SHOP_CODE_NULL);
        }
        // 网店编码唯一
        Sqls sqls = Sqls.custom();
        sqls.andEqualTo(OnlineShop.FIELD_ONLINE_SHOP_CODE, onlineShop.getOnlineShopCode());
        int number = onlineShopRepository.selectCountByCondition(Condition.builder(OnlineShop.class).andWhere(sqls).build());
        if (number > 0) {
            throw new O2CommonException(null, OnlineShopConstants.ErrorCode.ERROR_ONLINE_SHOP_CODE_UNIQUE);
        }
    }

    /**
     * 校验平台网店编码唯一性
     *
     * @param onlineShop 网店
     */
    private void validatePlatformOnlineShopCode(OnlineShop onlineShop) {
        if (null == onlineShop.getPlatformShopCode()) {
            return;
        }
        // 租户id+平台编码+平台网店编码
        Sqls sqls = Sqls.custom();
        sqls.andEqualTo(OnlineShop.FIELD_TENANT_ID, onlineShop.getTenantId());
        sqls.andEqualTo(OnlineShop.FIELD_PLATFORM_CODE, onlineShop.getPlatformCode());
        sqls.andEqualTo(OnlineShop.FIELD_PLATFORM_SHOP_CODE, onlineShop.getPlatformShopCode());
        int number = onlineShopRepository.selectCountByCondition(Condition.builder(OnlineShop.class).andWhere(sqls).build());
        if (number > 0) {
            throw new O2CommonException(null, OnlineShopConstants.ErrorCode.ERROR_ONLINE_SHOP_CODE_UNIQUE);
        }
    }

    /**
     * 校验网店名称唯一性
     *
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
            throw new O2CommonException(null, OnlineShopConstants.ErrorCode.ERROR_ONLINE_SHOP_NAME_UNIQUE,
                    OnlineShopConstants.ErrorCode.ERROR_ONLINE_SHOP_NAME_UNIQUE);
        }
    }

    @Override
    public Map<String, OnlineShopCO> listOnlineShops(OnlineShopQueryInnerDTO onlineShopQueryInnerDTO, Long tenantId) {
        Map<String, OnlineShopCO> map = new HashMap<>(16);
        List<OnlineShopCO> voList = OnlineShopConverter.poToCoListObjects(onlineShopRepository.listOnlineShops(onlineShopQueryInnerDTO, tenantId,
                BaseConstants.Flag.NO));
        if (voList.isEmpty()) {
            return map;
        }
        for (OnlineShopCO co : voList) {
            map.put(co.getOnlineShopCode(), co);
        }
        return map;
    }

    @Override
    public Map<String, OnlineShopCO> listOnlineShops(OnlineShopQueryInnerDTO onlineShopQueryInnerDTO) {
        Map<String, OnlineShopCO> map = new HashMap<>(16);
        List<OnlineShopCO> voList = OnlineShopConverter.poToCoListObjects(onlineShopRepository.listOnlineShops(onlineShopQueryInnerDTO, null, BaseConstants.Flag.YES));
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
        List<OnlineShopCO> voList = OnlineShopConverter.poToCoListObjects(onlineShopRepository.listOnlineShops(onlineShopCatalogVersionList,
                tenantId));
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
            onlineShopResult.setActiveFlag(onlineShop.getActiveFlag());
            onlineShopResult.set_tls(onlineShop.get_tls());
            onlineShopResult = this.updateOnlineShop(onlineShopResult);
        }
        return OnlineShopConverter.poToCoObject(onlineShopResult);
    }

    @Override
    public List<OnlineShopCO> batchUpdateShopStatus(List<OnlineShopDTO> onlineShopDTOList) {
        if (CollectionUtils.isEmpty(onlineShopDTOList)) {
            return new ArrayList<>();
        }
        List<String> shopCodeList = new ArrayList<>(onlineShopDTOList.size());
        Long tenantId = 0L;
        // 记录 网店编码对应网店状态
        Map<String, Integer> shopStatus = new HashMap<>(onlineShopDTOList.size());
        for (OnlineShopDTO onlineShopDTO : onlineShopDTOList) {
            shopCodeList.add(onlineShopDTO.getOnlineShopCode());
            tenantId = onlineShopDTO.getTenantId();
            shopStatus.put(onlineShopDTO.getOnlineShopCode(), onlineShopDTO.getActiveFlag());

        }
        // 查询网店信息
        List<OnlineShop> result = onlineShopRepository.selectByCondition(Condition.builder(OnlineShop.class).andWhere(Sqls.custom()
                .andEqualTo(OnlineShop.FIELD_TENANT_ID, tenantId)
                .andIn(OnlineShop.FIELD_ONLINE_SHOP_CODE, shopCodeList)).build());
        if (CollectionUtils.isEmpty(result)) {
            return new ArrayList<>();
        }
        // 更新网店状态
        List<OnlineShop> updateShops = new ArrayList<>(result.size());
        for (OnlineShop onlineShop : result) {
            OnlineShop update = new OnlineShop();
            int activeFlag = shopStatus.getOrDefault(onlineShop.getOnlineShopCode(), onlineShop.getActiveFlag());
            update.setOnlineShopId(onlineShop.getOnlineShopId());
            update.setObjectVersionNumber(onlineShop.getObjectVersionNumber());
            update.setActiveFlag(activeFlag);
            onlineShop.setActiveFlag(activeFlag);
            updateShops.add(update);
        }
        transactionalHelper.transactionOperation(() -> {
            onlineShopRepository.batchUpdateByPrimaryKeySelective(updateShops);
            onlineShopRedis.batchUpdateRedis(result, result.get(0).getTenantId());
        });
        return OnlineShopConverter.poToCoListObjects(result);
    }

    @Override
    public List<OnlineShopCO> queryOnlineShops(Long tenantId, OnlineShopQueryInnerDTO onlineShopQueryInnerDTO) {
        return onlineShopRepository.queryOnlineShops(onlineShopQueryInnerDTO, tenantId);
    }

    @Override
    public void syncMerchantInfo(MerchantInfoBO merchantInfo) {
        // 1. 查询网店是否已存在
        OnlineShop existShop = queryShopByCode(merchantInfo.getOnlineShopCode());
        // 2.如果网店已存在，则更新网店信息
        if (Objects.nonNull(existShop)) {
            // 只需要更新网店信息，其他信息保持不变
            updateShopByMerchant(existShop, merchantInfo);
            return;
        }
        // 3. 构建平台信息
        Platform platform = platformService.buildPlatform(merchantInfo);
        // 4. 构建服务点信息
        Pos pos = posService.buildAndVerifyPos(merchantInfo);
        // 5. 构建网店信息
        OnlineShop onlineShop = buildAndVerifyOnlineShop(merchantInfo);
        // 6. 构建目录信息
        Catalog catalog = buildCatalog(onlineShop);
        // 7. 构建目录版本信息
        CatalogVersion catalogVersion = buildCatalogVersion(onlineShop);
        // 8. 构建仓库信息
        Warehouse warehouse = warehouseService.buildAndVerifyWarehouse(merchantInfo);
        // 9. 保存信息（db+Redis）
        transactionalHelper.transactionOperation(() -> {
            // a. 保存平台db
            platformService.save(platform);
            // b. 保存服务点db
            posService.savePosDB(pos);
            // c. 保存目录
            catalogRepository.insertSelective(catalog);
            // d. 保存目录版本db
            catalogVersion.setCatalogId(catalog.getCatalogId());
            catalogVersionRepository.insertSelective(catalogVersion);
            // e. 保存网店信息db
            onlineShopRepository.insertSelective(onlineShop);
            // f. 保存仓库信息db
            warehouse.setPosId(pos.getPosId());
            warehouse.setPosCode(pos.getPosCode());
            warehouseRepository.insertSelective(warehouse);
            // g. 构建并保存网店关联仓库信息db
            OnlineShopRelWarehouse shopRelWh = buildShopRelWh(onlineShop, warehouse);
            onlineShopRelWarehouseRepository.insertSelective(shopRelWh);
            // h. 同步Redis
            onlineShopRedis.syncMerchantMetaInfo(onlineShop, warehouse, shopRelWh);
        });
        // 10. 网店关联默认站点队列
        pushDefaultSiteRelShop(onlineShop);
        // 11. 网店关联支付配置队列
        pushPayConfigRelShop(merchantInfo.getOnlineShopCode());
    }

    /**
     * 推送站点关联网店（默认站点）
     *
     * @param onlineShop 网店编码
     */
    protected void pushDefaultSiteRelShop(OnlineShop onlineShop) {
        String defaultSiteCodeParam = SystemParameterConstants.Parameter.DEFAULT_SITE_CODE;
        if (MetadataConstants.OnlineShopType.STORE.equals(onlineShop.getOnlineShopType())) {
            defaultSiteCodeParam = SystemParameterConstants.Parameter.DEFAULT_SITE_CODE_STORE;
        }
        // 查询默认站点
        SystemParameterCO defaultSite = sysParamService.getSystemParameter(defaultSiteCodeParam,
                BaseConstants.DEFAULT_TENANT_ID);
        if (Objects.isNull(defaultSite) || StringUtils.isBlank(defaultSite.getDefaultValue())) {
            return;
        }

        SiteRelShopBO siteRelShop = new SiteRelShopBO();
        siteRelShop.setSiteCode(defaultSite.getDefaultValue());
        siteRelShop.setOnlineShopCodes(Collections.singletonList(onlineShop.getOnlineShopCode()));
        o2CmsProducer.pushSiteRelShopQueue(siteRelShop);
    }

    public void pushPayConfigRelShop(String onlineShopCode){
        EcpInteractionContext context = new EcpInteractionContext();
        context.setOnlineShopCode(onlineShopCode);
        context.setTenantId(O2CoreConstants.tenantId);
        orderB2cManagementClient.sendQueueMsg(context, EcpOrderClientConstants.CaseCode.BBC_MERCHANT_PAY_CONFIG_REL);
    }

    /**
     * 根据商家信息更新网店信息
     *
     * @param onlineShop   网店
     * @param merchantInfo 商家信息
     */
    protected void updateShopByMerchant(OnlineShop onlineShop, MerchantInfoBO merchantInfo) {
        if (StringUtils.isNotBlank(merchantInfo.getOnlineShopName())) {
            onlineShop.setOnlineShopName(merchantInfo.getOnlineShopName());
        }
        if (StringUtils.isNotBlank(merchantInfo.getLogoUrl())) {
            onlineShop.setLogoUrl(merchantInfo.getLogoUrl());
        }
        if (StringUtils.isNotBlank(merchantInfo.getShopMediaUrl())) {
            onlineShop.setShopMediaUrl(merchantInfo.getShopMediaUrl());
        }
        if (null != merchantInfo.getSelfSalesFlag()) {
            onlineShop.setSelfSalesFlag(merchantInfo.getSelfSalesFlag());
        }
        if (null != merchantInfo.getActiveFlag()) {
            onlineShop.setActiveFlag(merchantInfo.getActiveFlag());
        }
        if (MapUtils.isNotEmpty(merchantInfo.getOnlineShopNameTls())) {
            Map<String, Map<String, String>> tls = Maps.newHashMap();
            tls.put(OnlineShop.FIELD_ONLINE_SHOP_NAME, merchantInfo.getOnlineShopNameTls());
            onlineShop.set_tls(tls);
        }
        updateOnlineShop(onlineShop);
    }

    /**
     * 根据编码查询网店
     *
     * @param onlineShopCode 网店编码
     * @return 网店信息
     */
    protected OnlineShop queryShopByCode(String onlineShopCode) {
        List<OnlineShop> onlineShops = onlineShopRepository.selectByCondition(Condition.builder(OnlineShop.class).andWhere(
                Sqls.custom().andEqualTo(OnlineShop.FIELD_ONLINE_SHOP_CODE, onlineShopCode)
        ).build());
        if (CollectionUtils.isEmpty(onlineShops)) {
            return null;
        }
        return onlineShops.get(0);
    }

    /**
     * 构建网店关联仓库
     *
     * @param onlineShop 网店
     * @param warehouse  仓库
     * @return 网店关联仓库
     */
    protected OnlineShopRelWarehouse buildShopRelWh(OnlineShop onlineShop, Warehouse warehouse) {
        OnlineShopRelWarehouse shopRelWh = new OnlineShopRelWarehouse();
        shopRelWh.setOnlineShopId(onlineShop.getOnlineShopId());
        shopRelWh.setWarehouseId(warehouse.getWarehouseId());
        shopRelWh.setActiveFlag(BaseConstants.Flag.YES);
        shopRelWh.setTenantId(onlineShop.getTenantId());
        return shopRelWh;
    }

}
