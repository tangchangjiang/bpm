package org.o2.metadata.console.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.console.infra.entity.Warehouse;
import org.o2.metadata.console.domain.repository.WarehouseRepository;
import org.o2.metadata.console.infra.mapper.WarehouseMapper;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 仓库  资源库实现
 *
 * @author yuying.shi@hand-china.com 2020/3/2
 */
@Component
public class WarehouseRepositoryImpl extends BaseRepositoryImpl<Warehouse> implements WarehouseRepository {

    private WarehouseMapper warehouseMapper;

    public WarehouseRepositoryImpl(final WarehouseMapper warehouseMapper) {
        this.warehouseMapper = warehouseMapper;
    }

//    @Override
//    public Warehouse getWarehouseWithCarrierNameById(Long tenantId, Long posId) {
//        return warehouseMapper.getWarehouseWithCarrierNameById(tenantId,posId);
//    }

    @Override
    public List<Warehouse> listUnbindWarehouseList(Long shopId, String warehouseCode, String warehouseName, Long tenantId) {
        return warehouseMapper.listUnbindWarehouseList(shopId, warehouseCode, warehouseName, tenantId);
    }

    @Override
    public List<Warehouse> listWarehouseByCondition(Warehouse warehouse) {
        return warehouseMapper.listWarehouseByCondition(warehouse);
    }

    @Override
    public List<Warehouse> queryAllWarehouseByTenantId(Long tenantId) {
        return warehouseMapper.queryAllWarehouseByTenantId(tenantId);
    }
}
