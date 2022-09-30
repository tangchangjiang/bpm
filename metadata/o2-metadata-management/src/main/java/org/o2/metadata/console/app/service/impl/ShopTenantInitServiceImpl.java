package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.core.O2CoreConstants;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.console.app.bo.TenantInitBO;
import org.o2.metadata.console.app.service.CacheJobService;
import org.o2.metadata.console.app.service.ShopTenantInitService;
import org.o2.metadata.console.infra.constant.CarrierConstants;
import org.o2.metadata.console.infra.constant.OnlineShopConstants;
import org.o2.metadata.console.infra.constant.TenantInitConstants;
import org.o2.metadata.console.infra.constant.WarehouseConstants;
import org.o2.metadata.console.infra.entity.*;
import org.o2.metadata.console.infra.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;

/**
 * 网店租户初始化
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/17 15:46
 */
@Slf4j
@Service
public class ShopTenantInitServiceImpl implements ShopTenantInitService {

    private final OnlineShopRepository onlineShopRepository;
    private final PlatformRepository platformRepository;
    private final PlatformInfoMappingRepository platformInfoMappingRepository;
    private final CatalogRepository catalogRepository;
    private final CatalogVersionRepository catalogVersionRepository;
    private final OnlineShopRelWarehouseRepository onlineShopRelWarehouseRepository;
    private final PosRepository posRepository;
    private final PosAddressRepository posAddressRepository;
    private final PosRelCarrierRepository posRelCarrierRepository;
    private final WarehouseRepository warehouseRepository;
    private final CarrierRepository carrierRepository;
    private final CarrierMappingRepository carrierMappingRepository;
    private final RedisCacheClient redisCacheClient;
    private final CacheJobService cacheJobService;


    public ShopTenantInitServiceImpl(OnlineShopRepository onlineShopRepository,
                                     PlatformRepository platformRepository,
                                     PlatformInfoMappingRepository platformInfoMappingRepository,
                                     CatalogRepository catalogRepository,
                                     CatalogVersionRepository catalogVersionRepository,
                                     OnlineShopRelWarehouseRepository onlineShopRelWarehouseRepository,
                                     PosRepository posRepository, PosAddressRepository posAddressRepository,
                                     PosRelCarrierRepository posRelCarrierRepository,
                                     WarehouseRepository warehouseRepository, CarrierRepository carrierRepository,
                                     CarrierMappingRepository carrierMappingRepository,
                                     RedisCacheClient redisCacheClient,
                                     CacheJobService cacheJobService) {
        this.onlineShopRepository = onlineShopRepository;
        this.platformRepository = platformRepository;
        this.platformInfoMappingRepository = platformInfoMappingRepository;
        this.catalogRepository = catalogRepository;
        this.catalogVersionRepository = catalogVersionRepository;
        this.onlineShopRelWarehouseRepository = onlineShopRelWarehouseRepository;
        this.posRepository = posRepository;
        this.posAddressRepository = posAddressRepository;
        this.posRelCarrierRepository = posRelCarrierRepository;
        this.warehouseRepository = warehouseRepository;
        this.carrierRepository = carrierRepository;
        this.carrierMappingRepository = carrierMappingRepository;
        this.cacheJobService = cacheJobService;
        this.redisCacheClient = redisCacheClient;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tenantInitializeBusiness(TenantInitBO bo) {
        long sourceTenantId = bo.getSourceTenantId();
        long targetTenantId = bo.getTargetTenantId();
        OnlineShop sourceOnlineShop = selectOnlineShop(sourceTenantId, bo.getOnlineShopCode());
        if (null == sourceOnlineShop) {
            log.info("source online shop is null");
            return;
        }
        // 1.处理网店
        handleOnlineShop(sourceTenantId, targetTenantId, bo.getOnlineShopCode());
        // 2.处理平台定义
        handlePlatform(sourceTenantId, targetTenantId, sourceOnlineShop.getPlatformCode());
        // 3.处理目录
        handleCatalog(sourceTenantId, targetTenantId, sourceOnlineShop.getCatalogCode(), sourceOnlineShop.getCatalogVersionCode());
        // 3.处理主数据目录
        if (!TenantInitConstants.InitCatalog.MASTER.equals(sourceOnlineShop.getCatalogCode())) {
            handleCatalog(sourceTenantId, targetTenantId,TenantInitConstants.InitCatalog.MASTER, TenantInitConstants.InitCatalog.MASTER);
        }
        // 4
        // 4.处理服务点
        List<String> warehouseCode = Arrays.asList(bo.getWarehouseCode().split(BaseConstants.Symbol.COMMA));
        handlePos(sourceTenantId, targetTenantId, warehouseCode);
        // 5.处理仓库
        handleWarehouse(sourceTenantId, targetTenantId, warehouseCode);
        // 6.处理网店关联仓库
        handleOnlineRelWare(sourceTenantId, targetTenantId, warehouseCode, bo.getOnlineShopCode());
        // 7. 处理承运商
        List<String> carrierCodes = Arrays.asList(bo.getCarrierCode().split(BaseConstants.Symbol.COMMA));
        handleCarrier(sourceTenantId, targetTenantId, carrierCodes);
        // 8.处理服务点关联承运商
        handlePosRel(sourceTenantId, targetTenantId, warehouseCode, carrierCodes);
        // 9. 处理缓存数据
        handleRedis(targetTenantId, bo.getOnlineShopCode());
    }

    /**
     * 查询网店
     *
     * @param tenantId       租户ID
     * @param onlineShopCode 网店编码
     * @return 网店
     */
    private OnlineShop selectOnlineShop(Long tenantId, String onlineShopCode) {
        List<OnlineShop> list = onlineShopRepository.selectByCondition(Condition.builder(OnlineShop.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(OnlineShop.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(OnlineShop.FIELD_ONLINE_SHOP_CODE, onlineShopCode))
                .build());
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 查询平台定义
     *
     * @param tenantId     租户ID
     * @param platformCode 平台编码
     * @return 网店
     */
    private Platform selectPlatform(Long tenantId, String platformCode) {
        List<Platform> list = platformRepository.selectByCondition(Condition.builder(Platform.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(Platform.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(Platform.FIELD_PLATFORM_CODE, platformCode))
                .build());
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 处理网店
     *
     * @param sourceTenantId 来源租户
     * @param targetTenantId 目标租户
     * @param onlineShopCode 网店编码
     */
    private void handleOnlineShop(Long sourceTenantId, Long targetTenantId, String onlineShopCode) {
        log.info("initializeOnlineShop start, tenantId[{}]", targetTenantId);
        // 1. 查询源租户下网店
        OnlineShop sourceOnlineShop = selectOnlineShop(sourceTenantId, onlineShopCode);
        if (null == sourceOnlineShop) {
            log.info("source platformOnlineShops is empty.");
            return;
        }
        // 2. 查询目标租户网店
        OnlineShop targetOnlineShop = selectOnlineShop(targetTenantId, onlineShopCode);
        if (null != targetOnlineShop) {
            onlineShopRepository.delete(targetOnlineShop);
            // 删除网店关联仓库
            OnlineShopRelWarehouse relWarehouse = new OnlineShopRelWarehouse();
            relWarehouse.setTenantId(targetTenantId);
            relWarehouse.setOnlineShopId(targetOnlineShop.getOnlineShopId());
            relWarehouse.setOnlineShopCode(onlineShopCode);
            onlineShopRelWarehouseRepository.delete(relWarehouse);
        }
        // 3. 新建网店
        sourceOnlineShop.setTenantId(targetTenantId);
        sourceOnlineShop.setOnlineShopId(null);
        onlineShopRepository.insert(sourceOnlineShop);
        log.info("initializeOnlineShop end, tenantId[{}]", targetTenantId);

    }

    /**
     * 处理平台定义数据
     *
     * @param sourceTenantId 源租户ID
     * @param targetTenantId 目标租户ID
     * @param platformCode   平台编码
     */
    private void handlePlatform(Long sourceTenantId, Long targetTenantId, String platformCode) {
        log.info("initializePlatform start, tenantId[{}]", targetTenantId);
        Platform source = selectPlatform(sourceTenantId, platformCode);
        boolean flag = O2CoreConstants.PlatformFrom.PLATFORM_FROM_LIST.contains(platformCode);
        if (null == source) {
            log.info("source platform is empty.");
            return;
        }
        // 基础数据初始化过
        if (flag) {
            return;
        }
        // 目标租户下平台定义
        Platform target = selectPlatform(targetTenantId, platformCode);
        if (null != target) {
            // 删除目标租户已有的数据平台定义
            platformRepository.deleteByPrimaryKey(target);
            // 删除目标租户已有的数据平台定义匹配
            List<PlatformInfoMapping> targetMappings = platformInfoMappingRepository.selectByCondition(Condition.builder(PlatformInfoMapping.class)
                    .andWhere(Sqls.custom().andEqualTo(PlatformInfoMapping.FIELD_TENANT_ID, targetTenantId)
                            .andEqualTo(PlatformInfoMapping.FIELD_PLATFORM_CODE, platformCode)).build());
            platformInfoMappingRepository.batchDeleteByPrimaryKey(targetMappings);
        }

        // 新建平台定义
        source.setTenantId(targetTenantId);
        source.setPlatformId(null);
        platformRepository.insert(source);
        // 新建平台定义匹配
        List<PlatformInfoMapping> sourceMappings = platformInfoMappingRepository.selectByCondition(Condition.builder(PlatformInfoMapping.class)
                .andWhere(Sqls.custom().andEqualTo(PlatformInfoMapping.FIELD_TENANT_ID, sourceTenantId)
                        .andEqualTo(PlatformInfoMapping.FIELD_PLATFORM_CODE, platformCode)).build());
        for (PlatformInfoMapping sourceMapping : sourceMappings) {
            sourceMapping.setTenantId(targetTenantId);
            sourceMapping.setPlatformInfMappingId(null);
        }
        platformInfoMappingRepository.batchDeleteByPrimaryKey(sourceMappings);
        log.info("initializePlatform end, tenantId[{}]", targetTenantId);

    }

    /**
     * 处理目录数据
     *
     * @param sourceTenantId     源租户ID
     * @param targetTenantId     目标租户ID
     * @param catalogCode        目录编码
     * @param catalogVersionCode 目录版本编码
     */
    private void handleCatalog(Long sourceTenantId, Long targetTenantId, String catalogCode, String catalogVersionCode) {
        log.info("initializeCatalog start, tenantId[{}]", targetTenantId);
        Catalog query = new Catalog();
        query.setTenantId(sourceTenantId);
        query.setCatalogCode(catalogCode);
        // 查询源租户目录
        Catalog sourceCatalog = catalogRepository.selectOne(query);
        if (null == sourceCatalog) {
            log.info("source catalog is empty.");
            return;
        }
        // 查询源租户目录版本
        CatalogVersion queryVersion = new CatalogVersion();
        queryVersion.setCatalogId(sourceCatalog.getCatalogId());
        queryVersion.setTenantId(sourceTenantId);
        queryVersion.setCatalogVersionCode(catalogVersionCode);
        CatalogVersion sourceCatalogVersion = catalogVersionRepository.selectOne(queryVersion);
        if (null == sourceCatalogVersion) {
            log.info("source catalog version is empty.");
            return;
        }
        // 查询目标租户目录
        query.setTenantId(targetTenantId);
        Catalog targetCatalog = catalogRepository.selectOne(query);
        // 查询目标租户目录版本
        queryVersion.setTenantId(targetTenantId);
        CatalogVersion targetCatalogVersion = catalogVersionRepository.selectOne(queryVersion);

        // 删除目标租户 目标版本和目录
        if (null != targetCatalog) {
            catalogRepository.deleteByPrimaryKey(targetCatalog);
        }
        if (null != targetCatalogVersion) {
            catalogVersionRepository.deleteByPrimaryKey(targetCatalogVersion);
        }
        // 新建目标租户 目录 和目标版本
        sourceCatalog.setTenantId(targetTenantId);
        sourceCatalog.setCatalogId(null);
        catalogRepository.insert(sourceCatalog);
        sourceCatalogVersion.setTenantId(targetTenantId);
        sourceCatalogVersion.setCatalogId(sourceCatalog.getCatalogId());
        sourceCatalogVersion.setCatalogVersionId(null);
        catalogVersionRepository.insert(sourceCatalogVersion);
        log.info("initializeCatalog end, tenantId[{}]", targetTenantId);

    }

    /**
     * 处理服务点
     *
     * @param sourceTenantId 来源租户ID
     * @param targetTenantId 目标租户ID
     * @param warehouseCodes 仓库编码
     */
    private void handlePos(Long sourceTenantId, Long targetTenantId, List<String> warehouseCodes) {
        log.info("initializePos start, tenantId[{}]", targetTenantId);

        // 通过仓库获取服务点编码
        Warehouse query = new Warehouse();
        query.setTenantId(sourceTenantId);
        query.setWarehouseCodes(warehouseCodes);
        List<Warehouse> list = warehouseRepository.listWarehouseByCondition(query);
        List<String> posCodes = new ArrayList<>();
        for (Warehouse warehouse : list) {
            posCodes.add(warehouse.getPosCode());
        }

        // 查询源租户服务点
        List<Pos> sourcePos = posRepository.selectByCondition(Condition.builder(Pos.class)
                .andWhere(Sqls.custom().andEqualTo(Pos.FIELD_TENANT_ID, sourceTenantId)
                        .andIn(Pos.FIELD_POS_CODE, posCodes)).build());
        if (CollectionUtils.isEmpty(sourcePos)) {
            log.info("source pos is empty.");
            return;
        }
        // 源租户 服务点和地址关联集合
        Map<Pos, PosAddress> sourcePosRelAddressMap = new HashMap<>();
        // 数据少 可以循环请求数据库
        for (Pos pos : sourcePos) {
            long addressId = pos.getAddressId();
            // 查询源租户 服务点地址
            PosAddress address = posAddressRepository.selectByPrimaryKey(addressId);
            sourcePosRelAddressMap.put(pos, address);
        }
        // 查询目标租户 服务点
        List<Pos> targetPos = posRepository.selectByCondition(Condition.builder(Pos.class)
                .andWhere(Sqls.custom().andEqualTo(Pos.FIELD_TENANT_ID, targetTenantId)
                        .andIn(Pos.FIELD_POS_CODE, posCodes)).build());
        if (CollectionUtils.isNotEmpty(targetPos)) {
            // 目标租户 服务点ID集合
            List<Long> targetPosIds = new ArrayList<>();
            // 目标租户 服务点地址集合
            List<PosAddress> targetPosAddress = new ArrayList<>();
            for (Pos pos : targetPos) {
                targetPosIds.add(pos.getPosId());
                PosAddress address = new PosAddress();
                address.setPosAddressId(pos.getAddressId());
                targetPosAddress.add(address);
            }
            //删除目标服务点 服务点地址 服务点关联承运商数据
            posRepository.batchDeleteByPrimaryKey(targetPos);
            posAddressRepository.batchDeleteByPrimaryKey(targetPosAddress);
            // 先查询服务关联承运商 在通过主键删除
            List<PosRelCarrier> relCarriers = posRelCarrierRepository.selectByCondition(Condition.builder(PosRelCarrier.class)
                    .andWhere(Sqls.custom().andEqualTo(PosRelCarrier.FIELD_TENANT_ID, targetTenantId)
                            .andIn(PosRelCarrier.FIELD_POS_ID, targetPosIds)).build());
            posRelCarrierRepository.batchDeleteByPrimaryKey(relCarriers);
        }

        // 新建服务点 服务点地址
        for (Map.Entry<Pos, PosAddress> entry : sourcePosRelAddressMap.entrySet()) {
            Pos k = entry.getKey();
            PosAddress v = entry.getValue();
            v.setTenantId(targetTenantId);
            v.setPosAddressId(null);
            posAddressRepository.insert(v);

            k.setTenantId(targetTenantId);
            k.setPosId(null);
            k.setAddressId(v.getPosAddressId());
            posRepository.insert(k);
        }
        log.info("initializePos end, tenantId[{}]", targetTenantId);
    }

    /**
     * 处理仓库
     *
     * @param sourceTenantId 来源租户ID
     * @param targetTenantId 目标租户ID
     * @param warehouseCodes 仓库编码
     */
    private void handleWarehouse(Long sourceTenantId, Long targetTenantId, List<String> warehouseCodes) {
        log.info("initializeWarehouse start, tenantId[{}]", targetTenantId);
        // 查询 源仓库
        Warehouse query = new Warehouse();
        query.setTenantId(sourceTenantId);
        query.setWarehouseCodes(warehouseCodes);
        List<Warehouse> warehouses = warehouseRepository.listWarehouseByCondition(query);
        if (CollectionUtils.isEmpty(warehouses)) {
            log.info("source warehouse  is empty.");
            return;
        }
        Map<Long, String> sourceWareRelPosMap = new HashMap<>();
        for (Warehouse warehouse : warehouses) {
            sourceWareRelPosMap.put(warehouse.getWarehouseId(), warehouse.getPosCode());
        }
        // 查询 目标仓库
        List<Warehouse> targetWarehouse = warehouseRepository.selectByCondition(Condition.builder(Warehouse.class)
                .andWhere(Sqls.custom().andEqualTo(Warehouse.FIELD_TENANT_ID, targetTenantId)
                        .andIn(Warehouse.FIELD_WAREHOUSE_CODE, warehouseCodes)).build());
        warehouseRepository.batchDeleteByPrimaryKey(targetWarehouse);
        // 新建
        List<Warehouse> sourceWarehouses = new ArrayList<>();
        for (Map.Entry<Long, String> entry : sourceWareRelPosMap.entrySet()) {
            long warehouseId = entry.getKey();
            String postCode = entry.getValue();
            // 关联目标库服务点
            List<Pos> target = posRepository.selectByCondition(Condition.builder(Pos.class)
                    .andWhere(Sqls.custom().andEqualTo(Warehouse.FIELD_TENANT_ID, targetTenantId)
                            .andEqualTo(Pos.FIELD_POS_CODE, postCode)).build());

            Warehouse source = warehouseRepository.selectByPrimaryKey(warehouseId);
            source.setTenantId(targetTenantId);
            source.setPosId(target.get(0).getPosId());
            source.setWarehouseId(null);
            sourceWarehouses.add(source);
        }
        warehouseRepository.batchInsert(sourceWarehouses);
        log.info("initializeWarehouse end, tenantId[{}]", targetTenantId);
    }

    /**
     * 处理网店关联仓库
     *
     * @param sourceTenantId 来源租户ID
     * @param targetTenantId 目标租户ID
     * @param warehouseCodes 仓库编码
     * @param onlineShopCode 网店编码
     */
    private void handleOnlineRelWare(Long sourceTenantId, Long targetTenantId, List<String> warehouseCodes, String onlineShopCode) {
        // 查询源网店关联仓库
        log.info("initializeOnlineRelWare start, tenantId[{}]", targetTenantId);
        OnlineShopRelWarehouse query = new OnlineShopRelWarehouse();
        query.setOnlineShopCodes(Collections.singletonList(onlineShopCode));
        query.setTenantId(sourceTenantId);
        List<OnlineShopRelWarehouse> list = onlineShopRelWarehouseRepository.listByCondition(query);
        List<OnlineShopRelWarehouse> sourceList = list.stream().filter(e -> warehouseCodes.contains(e.getWarehouseCode())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(sourceList)) {
            log.info("source online rel house is null");
            return;
        }
        for (OnlineShopRelWarehouse relWarehouse : sourceList) {
            OnlineShop onlineShop = selectOnlineShop(targetTenantId, onlineShopCode);
            if (null == onlineShop) {
                continue;
            }
            List<Warehouse> targetWarehouse = warehouseRepository.selectByCondition(Condition.builder(Warehouse.class)
                    .andWhere(Sqls.custom().andEqualTo(Warehouse.FIELD_TENANT_ID, targetTenantId)
                            .andEqualTo(Warehouse.FIELD_WAREHOUSE_CODE, relWarehouse.getWarehouseCode())).build());
            relWarehouse.setTenantId(targetTenantId);
            relWarehouse.setWarehouseId(targetWarehouse.get(0).getWarehouseId());
            relWarehouse.setOnlineShopId(onlineShop.getOnlineShopId());
            relWarehouse.setOnlineShopRelWarehouseId(null);
        }
        onlineShopRelWarehouseRepository.batchInsert(sourceList);
        log.info("initializeOnlineRelWare end, tenantId[{}]", targetTenantId);
    }

    /**
     * 处理承运商
     *
     * @param sourceTenantId 租户id
     * @param targetTenantId 目标ID
     * @param carrierCodes   承运商编码
     */
    private void handleCarrier(Long sourceTenantId, Long targetTenantId, List<String> carrierCodes) {
        // 查询 源承运商
        log.info("initializeCarrier start, tenantId[{}]", targetTenantId);
        List<Carrier> sourceCarrier = carrierRepository.selectByCondition(Condition.builder(Carrier.class)
                .andWhere(Sqls.custom().andEqualTo(Carrier.FIELD_TENANT_ID, sourceTenantId)
                        .andIn(Carrier.FIELD_CARRIER_CODE, carrierCodes)).build());
        // 源租户 承运商匹配关系
        Map<Carrier, List<CarrierMapping>> sourceCarrierMappingMap = new HashMap<>();
        for (Carrier carrier : sourceCarrier) {
            List<CarrierMapping> sourceCarrierMapping = carrierMappingRepository.selectByCondition(Condition.builder(CarrierMapping.class)
                    .andWhere(Sqls.custom().andEqualTo(CarrierMapping.FIELD_TENANT_ID, sourceTenantId)
                            .andEqualTo(CarrierMapping.FIELD_CARRIER_ID, carrier.getCarrierId())).build());
            sourceCarrierMappingMap.put(carrier, sourceCarrierMapping);
        }

        // 查询 目标承运商
        List<Carrier> targetCarrier = carrierRepository.selectByCondition(Condition.builder(Carrier.class)
                .andWhere(Sqls.custom().andEqualTo(Carrier.FIELD_TENANT_ID, targetTenantId)
                        .andIn(Carrier.FIELD_CARRIER_CODE, carrierCodes)).build());
        if (CollectionUtils.isNotEmpty(targetCarrier)) {
            List<Long> targetCarrierIds = new ArrayList<>();
            for (Carrier carrier : targetCarrier) {
                targetCarrierIds.add(carrier.getCarrierId());
            }
            // 查询 目标承运商匹配
            List<CarrierMapping> targetCarrierMapping = carrierMappingRepository.selectByCondition(Condition.builder(CarrierMapping.class)
                    .andWhere(Sqls.custom().andEqualTo(CarrierMapping.FIELD_TENANT_ID, targetTenantId)
                            .andIn(CarrierMapping.FIELD_CARRIER_ID, targetCarrierIds)).build());
            //删除目标数据
            carrierRepository.batchDeleteByPrimaryKey(targetCarrier);
            carrierMappingRepository.batchDeleteByPrimaryKey(targetCarrierMapping);
        }
        // 新建
        for (Map.Entry<Carrier, List<CarrierMapping>> entry : sourceCarrierMappingMap.entrySet()) {
            Carrier carrier = entry.getKey();
            carrier.setCarrierId(null);
            carrier.setTenantId(targetTenantId);
            carrierRepository.insert(carrier);
            List<CarrierMapping> carrierMappings = entry.getValue();
            for (CarrierMapping carrierMapping : carrierMappings) {
                carrierMapping.setTenantId(targetTenantId);
                carrierMapping.setCarrierMappingId(null);
                carrierMapping.setCarrierId(carrier.getCarrierId());
            }
            carrierMappingRepository.batchInsert(carrierMappings);
            log.info("initializeCarrier end, tenantId[{}]", targetTenantId);
        }
    }

    /**
     * 处理承运商
     *
     * @param sourceTenantId 租户id
     * @param targetTenantId 目标ID
     * @param warehouseCodes 仓库编码
     * @param carrierCodes   承运商编码
     */
    private void handlePosRel(Long sourceTenantId, Long targetTenantId, List<String> warehouseCodes, List<String> carrierCodes) {
        // 通过仓库获取服务点编码
        log.info("initializePosRel start, tenantId[{}]", targetTenantId);
        Warehouse warehouse = new Warehouse();
        warehouse.setTenantId(sourceTenantId);
        warehouse.setWarehouseCodes(warehouseCodes);
        List<Warehouse> list = warehouseRepository.listWarehouseByCondition(warehouse);
        List<String> posCodes = new ArrayList<>();
        for (Warehouse warehouse1 : list) {
            posCodes.add(warehouse1.getPosCode());
        }
        // 查询源租户 服务点关联承运商
        PosRelCarrier posRelCarrier = new PosRelCarrier();
        posRelCarrier.setCarrierCodes(carrierCodes);
        posRelCarrier.setPosCodes(posCodes);
        List<PosRelCarrier> sourceRelCarriers = posRelCarrierRepository.listCarrierWithPos(posRelCarrier);
        // 查询目标租户 承运商
        List<Carrier> targetCarrier = carrierRepository.selectByCondition(Condition.builder(Carrier.class).where(Sqls.custom()
                .andEqualTo(Carrier.FIELD_TENANT_ID, targetTenantId)
                .andIn(Carrier.FIELD_CARRIER_CODE, carrierCodes)).build());
        Map<String, Long> targetCarrierMap = new HashMap<>(4);
        for (Carrier carrier : targetCarrier) {
            targetCarrierMap.put(carrier.getCarrierCode(), carrier.getCarrierId());
        }
        // 查询目标 服务点
        List<Pos> targetPos = posRepository.selectByCondition(Condition.builder(Pos.class).where(Sqls.custom()
                .andEqualTo(Pos.FIELD_TENANT_ID, targetTenantId)
                .andIn(Pos.FIELD_POS_CODE, posCodes)).build());
        Map<String, Long> targetPosMap = new HashMap<>(4);
        for (Pos pos : targetPos) {
            targetPosMap.put(pos.getPosCode(), pos.getPosId());
        }
        for (PosRelCarrier sourceRelCarrier : sourceRelCarriers) {
            String posCode = sourceRelCarrier.getPosCode();
            String carrierCode = sourceRelCarrier.getCarrierCode();
            sourceRelCarrier.setCarrierId(targetCarrierMap.get(carrierCode));
            sourceRelCarrier.setPosId(targetPosMap.get(posCode));
            sourceRelCarrier.setPosRelCarrierId(null);
            sourceRelCarrier.setTenantId(targetTenantId);
        }
        posRelCarrierRepository.batchInsert(sourceRelCarriers);
        log.info("initializePosRel end, tenantId[{}]", targetTenantId);
    }

    /**
     * 处理缓存数据
     *
     * @param targetTenantId 目标租户ID
     * @param onlineShopCode 网店编码
     */
    private void handleRedis(Long targetTenantId, String onlineShopCode) {
        String carrierKey = CarrierConstants.Redis.getCarrierKey(targetTenantId);
        redisCacheClient.delete(carrierKey);
        cacheJobService.refreshCarrier(targetTenantId);
        cacheJobService.refreshOnlineShop(targetTenantId);
        String onlineShopRelWarehouseKey = OnlineShopConstants.Redis.getOnlineShopRelWarehouseKey(onlineShopCode, targetTenantId);
        redisCacheClient.delete(onlineShopRelWarehouseKey);
        cacheJobService.refreshOnlineShopRelWarehouse(targetTenantId);
        String warehouseKey = WarehouseConstants.WarehouseCache.warehouseCacheKey(targetTenantId);
        redisCacheClient.delete(warehouseKey);
        cacheJobService.refreshWarehouse(targetTenantId);

    }
}
