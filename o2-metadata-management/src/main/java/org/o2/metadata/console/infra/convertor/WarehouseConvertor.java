package org.o2.metadata.console.infra.convertor;

import org.o2.metadata.console.infra.entity.Warehouse;
import org.o2.metadata.domain.warehouse.domain.WarehouseDO;

/**
 *
 * 仓库
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
public class WarehouseConvertor {
    public static WarehouseDO poToDoObject(Warehouse warehouse) {

        if (warehouse == null) {
            return null;
        }
        WarehouseDO warehouseDO = new WarehouseDO();
        warehouseDO.setWarehouseId(warehouse.getWarehouseId());
        warehouseDO.setPosId(warehouse.getPosId());
        warehouseDO.setWarehouseCode(warehouse.getWarehouseCode());
        warehouseDO.setWarehouseName(warehouse.getWarehouseName());
        warehouseDO.setWarehouseStatusCode(warehouse.getWarehouseStatusCode());
        warehouseDO.setWarehouseTypeCode(warehouse.getWarehouseTypeCode());
        warehouseDO.setPickUpQuantity(warehouse.getPickUpQuantity());
        warehouseDO.setExpressedQuantity(warehouse.getExpressedQuantity());
        warehouseDO.setPickedUpFlag(warehouse.getPickedUpFlag());
        warehouseDO.setExpressedFlag(warehouse.getExpressedFlag());
        warehouseDO.setScore(warehouse.getScore());
        warehouseDO.setActivedDateFrom(warehouse.getActivedDateFrom());
        warehouseDO.setActivedDateTo(warehouse.getActivedDateTo());
        warehouseDO.setInvOrganizationCode(warehouse.getInvOrganizationCode());
        warehouseDO.setTenantId(warehouse.getTenantId());
        warehouseDO.setActiveFlag(warehouse.getActiveFlag());
        warehouseDO.setPosCode(warehouse.getPosCode());
        warehouseDO.setPosName(warehouse.getPosName());
        warehouseDO.setWarehouseStatusMeaning(warehouse.getWarehouseStatusMeaning());
        warehouseDO.setWarehouseTypeMeaning(warehouse.getWarehouseTypeMeaning());
        warehouseDO.setExpressLimitValue(warehouse.getExpressLimitValue());
        warehouseDO.setPickUpLimitValue(warehouse.getPickUpLimitValue());
        return warehouseDO;
    }
}
