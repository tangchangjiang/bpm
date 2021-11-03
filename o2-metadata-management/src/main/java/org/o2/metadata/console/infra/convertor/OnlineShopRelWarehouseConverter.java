package org.o2.metadata.console.infra.convertor;

import org.o2.metadata.console.api.co.OnlineShopRelWarehouseCO;
import org.o2.metadata.console.infra.entity.OnlineShopRelWarehouse;
import org.o2.metadata.domain.onlineshop.domain.OnlineShopRelWarehouseDO;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 网店关联仓库
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
public class OnlineShopRelWarehouseConverter {

    private OnlineShopRelWarehouseConverter() {
    }

    /**
     * po 转 do
     * @param warehouse 仓库
     * @return do
     */
    private static OnlineShopRelWarehouseDO poToDoObject(OnlineShopRelWarehouse warehouse) {
        if (warehouse == null) {
            return null;
        }
        OnlineShopRelWarehouseDO onlineShopRelWarehouseDO = new OnlineShopRelWarehouseDO();
        onlineShopRelWarehouseDO.setOnlineShopRelWarehouseId(warehouse.getOnlineShopRelWarehouseId());
        onlineShopRelWarehouseDO.setOnlineShopId(warehouse.getOnlineShopId());
        onlineShopRelWarehouseDO.setWarehouseId(warehouse.getWarehouseId());
        onlineShopRelWarehouseDO.setActiveFlag(warehouse.getActiveFlag());
        onlineShopRelWarehouseDO.setBusinessActiveFlag(warehouse.getBusinessActiveFlag());
        onlineShopRelWarehouseDO.setTenantId(warehouse.getTenantId());
        onlineShopRelWarehouseDO.setWarehouseCode(warehouse.getWarehouseCode());
        return onlineShopRelWarehouseDO;
    }

    /**
     * do 转 Co
     * @param  warehouseDO 网店关联仓库
     * @return CO
     */
    private static OnlineShopRelWarehouseCO doToCoObject(OnlineShopRelWarehouseDO warehouseDO) {

        if (warehouseDO == null) {
            return null;
        }
        OnlineShopRelWarehouseCO co = new OnlineShopRelWarehouseCO();
        co.setOnlineShopRelWarehouseId(warehouseDO.getOnlineShopRelWarehouseId());
        co.setOnlineShopId(warehouseDO.getOnlineShopId());
        co.setPosId(warehouseDO.getPosId());
        co.setWarehouseId(warehouseDO.getWarehouseId());
        co.setActiveFlag(warehouseDO.getActiveFlag());
        co.setBusinessActiveFlag(warehouseDO.getBusinessActiveFlag());
        co.setTenantId(warehouseDO.getTenantId());
        co.setWarehouseCode(warehouseDO.getWarehouseCode());
        return co;
    }

    /**
     * DO 转 CO
     * @param onlineShopRelWarehouseDOList 网店关联仓库集合
     * @return list
     */
    public static List<OnlineShopRelWarehouseCO> doToCoListObjects(List<OnlineShopRelWarehouseDO> onlineShopRelWarehouseDOList) {
        List<OnlineShopRelWarehouseCO> cos = new ArrayList<>();
        if (onlineShopRelWarehouseDOList == null) {
            return cos;
        }
        for (OnlineShopRelWarehouseDO onlineShopRelWarehouseDO : onlineShopRelWarehouseDOList) {
            cos.add(doToCoObject(onlineShopRelWarehouseDO));
        }
        return cos;
    }

    /**
     * PO 转 DO
     * @param onlineShopRelWarehouseList 网店关联仓库集合
     * @return list
     */
    public static List<OnlineShopRelWarehouseDO> poToDoListObjects(List<OnlineShopRelWarehouse> onlineShopRelWarehouseList) {
        List<OnlineShopRelWarehouseDO> onlineShopRelWarehouseDOList = new ArrayList<>();
        if (onlineShopRelWarehouseList == null) {
            return onlineShopRelWarehouseDOList;
        }
        for (OnlineShopRelWarehouse onlineShopRelWarehouse : onlineShopRelWarehouseList) {
            onlineShopRelWarehouseDOList.add(poToDoObject(onlineShopRelWarehouse));
        }
        return onlineShopRelWarehouseDOList;
    }

}
