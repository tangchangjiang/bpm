package org.o2.metadata.console.infra.convertor;

import org.o2.metadata.console.api.vo.OnlineShopRelWarehouseVO;
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
public class OnlineShopRelWarehouseConvertor {
    /**
     * po 转 do
     * @param warehouse 仓库
     * @return do
     */
    public static OnlineShopRelWarehouseDO poToDoObject(OnlineShopRelWarehouse warehouse) {
        if (warehouse == null) {
            return null;
        }
        OnlineShopRelWarehouseDO onlineShopRelWarehouseDO = new OnlineShopRelWarehouseDO();
        onlineShopRelWarehouseDO.setOnlineShopRelWarehouseId(warehouse.getOnlineShopRelWarehouseId());
        onlineShopRelWarehouseDO.setOnlineShopId(warehouse.getOnlineShopId());
        onlineShopRelWarehouseDO.setPosId(warehouse.getPosId());
        onlineShopRelWarehouseDO.setWarehouseId(warehouse.getWarehouseId());
        onlineShopRelWarehouseDO.setActiveFlag(warehouse.getActiveFlag());
        onlineShopRelWarehouseDO.setBusinessActiveFlag(warehouse.getBusinessActiveFlag());
        onlineShopRelWarehouseDO.setTenantId(warehouse.getTenantId());
        onlineShopRelWarehouseDO.setWarehouseCode(warehouse.getWarehouseCode());
        return onlineShopRelWarehouseDO;
    }
    /**
     * do 转 vo
     * @param  warehouseDO 网店关联仓库
     * @return  vo
     */
    public static OnlineShopRelWarehouseVO doToVoObject(OnlineShopRelWarehouseDO warehouseDO) {

        if (warehouseDO == null) {
            return null;
        }
        OnlineShopRelWarehouseVO onlineShopRelWarehouseVO = new OnlineShopRelWarehouseVO();
        onlineShopRelWarehouseVO.setOnlineShopRelWarehouseId(warehouseDO.getOnlineShopRelWarehouseId());
        onlineShopRelWarehouseVO.setOnlineShopId(warehouseDO.getOnlineShopId());
        onlineShopRelWarehouseVO.setPosId(warehouseDO.getPosId());
        onlineShopRelWarehouseVO.setWarehouseId(warehouseDO.getWarehouseId());
        onlineShopRelWarehouseVO.setActiveFlag(warehouseDO.getActiveFlag());
        onlineShopRelWarehouseVO.setBusinessActiveFlag(warehouseDO.getBusinessActiveFlag());
        onlineShopRelWarehouseVO.setTenantId(warehouseDO.getTenantId());
        onlineShopRelWarehouseVO.setWarehouseCode(warehouseDO.getWarehouseCode());
        return onlineShopRelWarehouseVO;
    }
    /**
     * DO 转 VO
     * @param onlineShopRelWarehouseDOList 网店关联仓库集合
     * @return  list
     */
    public static List<OnlineShopRelWarehouseVO> doToVoListObjects(List<OnlineShopRelWarehouseDO> onlineShopRelWarehouseDOList) {
        List<OnlineShopRelWarehouseVO> onlineShopRelWarehouseVOList = new ArrayList<>();
        if (onlineShopRelWarehouseDOList == null) {
            return onlineShopRelWarehouseVOList;
        }
        for (OnlineShopRelWarehouseDO onlineShopRelWarehouseDO : onlineShopRelWarehouseDOList) {
            onlineShopRelWarehouseVOList.add(doToVoObject(onlineShopRelWarehouseDO));
        }
        return onlineShopRelWarehouseVOList;
    }

    /**
     * PO 转 DO
     * @param onlineShopRelWarehouseList 网店关联仓库集合
     * @return  list
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
