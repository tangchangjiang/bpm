package org.o2.metadata.console.infra.convertor;

import org.o2.metadata.console.api.co.WarehouseCO;
import org.o2.metadata.console.api.vo.WarehouseVO;
import org.o2.metadata.console.infra.entity.Warehouse;
import org.o2.metadata.domain.warehouse.domain.WarehouseDO;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 仓库
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
public class WarehouseConverter {
    private WarehouseConverter() {
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

   /**
    * do 转 CO
    * @param warehouse 仓库
    * @return CO
    */
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
        co.setExpressLimitValue(warehouse.getExpressLimitValue());
        co.setPickUpLimitValue(warehouse.getPickUpLimitValue());
        co.setStoreReturnFlag(warehouse.getStoreReturnFlag());
        return co;
    }

    private static WarehouseVO poToVoObject(Warehouse warehouse) {

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
        warehouseVO.setWarehouseStatus(warehouse.getWarehouseStatus());
        return warehouseVO;
    }

    /**
     * PO 转 vO
     * @param warehouses 仓库
     * @return  list
     */
    public static List<WarehouseVO> poToVoListObjects(List<Warehouse> warehouses) {
        List<WarehouseVO> warehouseVOList = new ArrayList<>();
        if (warehouses == null) {
            return warehouseVOList;
        }
        for (Warehouse warehouse : warehouses) {
            warehouseVOList.add(poToVoObject(warehouse));
        }
        return warehouseVOList;
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
     * DO 转 CO
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

    /**
     * PO 转 CO
     * @param warehouses 仓库
     * @return  list
     */
    public static List<WarehouseCO> poToCoListObjects(List<Warehouse> warehouses) {
        List<WarehouseCO> cos = new ArrayList<>();
        if (warehouses == null) {
            return cos;
        }
        for (Warehouse warehouse : warehouses) {
            cos.add(poToCoObject(warehouse));
        }
        return cos;
    }

    /**
     * PO 转 CO
     * @param warehouse 仓库
     * @return  co
     */
    private static WarehouseCO poToCoObject(Warehouse warehouse) {

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
        co.setPosName(warehouse.getPosName());
        co.setExpressLimitValue(warehouse.getExpressLimitValue());
        co.setPickUpLimitValue(warehouse.getPickUpLimitValue());
        co.setStoreReturnFlag(warehouse.getStoreReturnFlag());
        co.setOnlineShopCode(warehouse.getOnlineShopCode());
        co.setPlatformCode(warehouse.getPlatformCode());
        co.setPlatformName(warehouse.getPlatformName());
        return co;
    }
}
