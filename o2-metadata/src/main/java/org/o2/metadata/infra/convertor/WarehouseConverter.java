package org.o2.metadata.infra.convertor;

import org.o2.metadata.api.co.WarehouseCO;
import org.o2.metadata.domain.warehouse.domain.WarehouseDO;
import org.o2.metadata.infra.entity.Warehouse;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 仓库
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
public class WarehouseConverter {
    private WarehouseConverter(){
    }
    /**
     * po 转 do
     * @param warehouse po
     * @return do
     */
    private static WarehouseDO poToDoObject(Warehouse warehouse) {

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

    private static WarehouseCO doToCoObject(WarehouseDO warehouse) {

        if (warehouse == null) {
            return null;
        }
        WarehouseCO co = new WarehouseCO();
        co.setWarehouseId(warehouse.getWarehouseId());
        co.setPosId(warehouse.getPosId());
        co.setWarehouseCode(warehouse.getWarehouseCode());
        co.setWarehouseName(warehouse.getWarehouseName());
        co.setWarehouseStatusCode(warehouse.getWarehouseStatusCode());
        co.setWarehouseTypeCode(warehouse.getWarehouseTypeCode());
        co.setPickUpQuantity(warehouse.getPickUpQuantity());
        co.setExpressedQuantity(warehouse.getExpressedQuantity());
        co.setPickedUpFlag(warehouse.getPickedUpFlag());
        co.setExpressedFlag(warehouse.getExpressedFlag());
        co.setScore(warehouse.getScore());
        co.setActivedDateFrom(warehouse.getActivedDateFrom());
        co.setActivedDateTo(warehouse.getActivedDateTo());
        co.setInvOrganizationCode(warehouse.getInvOrganizationCode());
        co.setTenantId(warehouse.getTenantId());
        co.setActiveFlag(warehouse.getActiveFlag());
        co.setPosCode(warehouse.getPosCode());
        co.setPosName(warehouse.getPosName());
        co.setWarehouseStatusMeaning(warehouse.getWarehouseStatusMeaning());
        co.setWarehouseTypeMeaning(warehouse.getWarehouseTypeMeaning());
        co.setExpressLimitValue(warehouse.getExpressLimitValue());
        co.setPickUpLimitValue(warehouse.getPickUpLimitValue());
        return co;
    }
    /**
     * PO 转 DO
     * @param warehouses 仓库
     * @return  list
     */
    public static List<WarehouseDO> poToDoListObjects(List<Warehouse> warehouses) {
        List<WarehouseDO> warehouseDOList = new ArrayList<>();
        if (warehouses == null) {
            return warehouseDOList;
        }
        for (Warehouse warehouse : warehouses) {
            warehouseDOList.add(poToDoObject(warehouse));
        }
        return warehouseDOList;
    }
    /**
     * DO 转 VO
     * @param warehouses 仓库
     * @return  list
     */
    public static List<WarehouseCO> doToCoListObjects(List<WarehouseDO> warehouses) {
        List<WarehouseCO> warehouseVOList = new ArrayList<>();
        if (warehouses == null) {
            return warehouseVOList;
        }
        for (WarehouseDO warehouse : warehouses) {
            warehouseVOList.add(doToCoObject(warehouse));
        }
        return warehouseVOList;
    }
}
