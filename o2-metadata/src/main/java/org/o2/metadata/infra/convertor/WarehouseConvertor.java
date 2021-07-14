package org.o2.metadata.infra.convertor;

import org.o2.metadata.api.vo.WarehouseVO;
import org.o2.metadata.domain.warehouse.domain.WarehouseDO;
import org.o2.metadata.infra.entity.Warehouse;

/**
 *
 * 仓库
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
public class WarehouseConvertor {
    /**
     * po 转 do
     * @param warehouse po
     * @return do
     */
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

    public static WarehouseVO doToVoObject(WarehouseDO warehouse) {

        if (warehouse == null) {
            return null;
        }
        WarehouseVO warehouseVO = new WarehouseVO();
        warehouseVO.setWarehouseId(warehouse.getWarehouseId());
        warehouseVO.setPosId(warehouse.getPosId());
        warehouseVO.setWarehouseCode(warehouse.getWarehouseCode());
        warehouseVO.setWarehouseName(warehouse.getWarehouseName());
        warehouseVO.setWarehouseStatusCode(warehouse.getWarehouseStatusCode());
        warehouseVO.setWarehouseTypeCode(warehouse.getWarehouseTypeCode());
        warehouseVO.setPickUpQuantity(warehouse.getPickUpQuantity());
        warehouseVO.setExpressedQuantity(warehouse.getExpressedQuantity());
        warehouseVO.setPickedUpFlag(warehouse.getPickedUpFlag());
        warehouseVO.setExpressedFlag(warehouse.getExpressedFlag());
        warehouseVO.setScore(warehouse.getScore());
        warehouseVO.setActivedDateFrom(warehouse.getActivedDateFrom());
        warehouseVO.setActivedDateTo(warehouse.getActivedDateTo());
        warehouseVO.setInvOrganizationCode(warehouse.getInvOrganizationCode());
        warehouseVO.setTenantId(warehouse.getTenantId());
        warehouseVO.setActiveFlag(warehouse.getActiveFlag());
        warehouseVO.setPosCode(warehouse.getPosCode());
        warehouseVO.setPosName(warehouse.getPosName());
        warehouseVO.setWarehouseStatusMeaning(warehouse.getWarehouseStatusMeaning());
        warehouseVO.setWarehouseTypeMeaning(warehouse.getWarehouseTypeMeaning());
        warehouseVO.setExpressLimitValue(warehouse.getExpressLimitValue());
        warehouseVO.setPickUpLimitValue(warehouse.getPickUpLimitValue());
        return warehouseVO;
    }
}
