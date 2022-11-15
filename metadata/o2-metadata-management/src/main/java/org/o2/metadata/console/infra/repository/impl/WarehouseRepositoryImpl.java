package org.o2.metadata.console.infra.repository.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.console.api.co.WarehouseCO;
import org.o2.metadata.console.api.co.WarehouseRelAddressCO;
import org.o2.metadata.console.api.dto.*;
import org.o2.metadata.console.api.vo.WarehouseRelPosVO;
import org.o2.metadata.console.app.bo.WarehouseCacheBO;
import org.o2.metadata.console.app.service.LovAdapterService;
import org.o2.metadata.console.infra.entity.Carrier;
import org.o2.metadata.console.infra.entity.Region;
import org.o2.metadata.console.infra.entity.Warehouse;
import org.o2.metadata.console.infra.repository.WarehouseRepository;
import org.o2.metadata.console.infra.mapper.WarehouseMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 仓库  资源库实现
 *
 * @author yuying.shi@hand-china.com 2020/3/2
 */
@Component
public class WarehouseRepositoryImpl extends BaseRepositoryImpl<Warehouse> implements WarehouseRepository {

    private WarehouseMapper warehouseMapper;
    private final LovAdapterService lovAdapterService;

    public WarehouseRepositoryImpl(final WarehouseMapper warehouseMapper, LovAdapterService lovAdapterService) {
        this.warehouseMapper = warehouseMapper;
        this.lovAdapterService = lovAdapterService;
    }

    @Override
    public List<Warehouse> listUnbindWarehouseList(Long shopId, String warehouseCode, String warehouseName,String warehouseStatusCode, Long tenantId) {
        return warehouseMapper.listUnbindWarehouseList(shopId, warehouseCode, warehouseName,  warehouseStatusCode,tenantId);
    }

    @Override
    public List<Warehouse> listWarehouseByCondition(Warehouse warehouse) {
        return warehouseMapper.listWarehouseByCondition(warehouse);
    }

    @Override
    public List<Warehouse> listWarehouses(WarehouseQueryInnerDTO innerDTO, Long tenantId) {
        return warehouseMapper.listWarehouses(innerDTO,tenantId);
    }

    @Override
    public List<Warehouse> queryAllWarehouseByTenantId(Long tenantId) {
        return warehouseMapper.queryAllWarehouseByTenantId(tenantId);
    }

    @Override
    public List<Warehouse> listActiveWarehouseByShopCode(String onlineShopCode, Long organizationId) {
        return warehouseMapper.listActiveWarehouseByShopCode(onlineShopCode,organizationId);
    }

    @Override
    public List<WarehouseCacheBO> listWarehouseByCode(List<String> warehouseCodes, Long tenantId) {
        return warehouseMapper.listWarehouseByCode(warehouseCodes,tenantId);
    }

    @Override
    public List<Carrier> listCarriers(WarehouseRelCarrierQueryDTO queryDTO) {
        return warehouseMapper.listCarriers(queryDTO);
    }

    @Override
    public List<Warehouse> listWarehouseAddr(WarehouseAddrQueryDTO queryDTO) {
        return warehouseMapper.listWarehouseAddr(queryDTO);
    }

    @Override
    public List<WarehouseCO> pageWarehouses(WarehousePageQueryInnerDTO innerDTO) {
        return warehouseMapper.pageWarehouses(innerDTO);
    }

    @Override
    public List<WarehouseRelAddressCO> selectAllDeliveryWarehouse(Long tenantId) {
        return warehouseMapper.selectAllDeliveryWarehouse(tenantId);
    }

    @Override
    public List<WarehouseRelPosVO> listWarehouseRelPos(WarehouseRelPosDTO warehouseRelPosDTO) {

        List<WarehouseRelPosVO> relPosVOS = warehouseMapper.listWarehouseRelPos(warehouseRelPosDTO);
        // 匹配地址信息
        RegionQueryLovInnerDTO lovInnerDTO = new RegionQueryLovInnerDTO();
        lovInnerDTO.setTenantId(warehouseRelPosDTO.getTenantId());
        List<Region> regions = lovAdapterService.queryRegion(warehouseRelPosDTO.getTenantId(), lovInnerDTO);
        if (CollectionUtils.isNotEmpty(regions)) {
            Map<String, String> regionMap = regions.stream().collect(Collectors.toMap(Region::getRegionCode, Region::getRegionName, (a, b) -> a));
            Map<String, String> countryMap = regions.stream().collect(Collectors.toMap(Region::getCountryCode, Region::getCountryName, (a, b) -> a));
            relPosVOS.forEach(v -> {
                v.setCountryName(countryMap.getOrDefault(v.getCountryCode(), v.getCountryCode()));
                v.setRegionName(regionMap.getOrDefault(v.getRegionCode(), v.getRegionCode()));
                v.setCityName(regionMap.getOrDefault(v.getCityCode(), v.getCityCode()));
                v.setDistrictName(regionMap.getOrDefault(v.getDistrictCode(), v.getDistrictCode()));
            });
        }
        return relPosVOS;
    }

    @Override
    public List<Warehouse> listWarehousesByPosCode(List<String> posCodes, Long tenantId) {
        return warehouseMapper.listWarehousesByPosCode(posCodes,tenantId);
    }
}
