package org.o2.metadata.infra.convertor;

import org.apache.commons.collections4.CollectionUtils;
import org.o2.metadata.api.co.WarehousePickupLimitCO;
import org.o2.metadata.infra.entity.WarehouseLimit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chao.yang05@hand-china.com 2022/4/18
 */
public class WarehouseLimitConverter {
    private WarehouseLimitConverter() {
        // 转换类，不需要创建对象
    }

    /**
     * do 转 co
     * @param warehouseLimit do
     * @return co
     */
    public static WarehousePickupLimitCO doToCoObject(WarehouseLimit warehouseLimit) {
        if (warehouseLimit == null) {
            return null;
        }
        WarehousePickupLimitCO warehousePickupLimitCO = new WarehousePickupLimitCO();
        warehousePickupLimitCO.setPickUpQuantity(warehouseLimit.getPickUpQuantity());
        warehousePickupLimitCO.setLimitFlag(warehouseLimit.getLimitFlag());
        return warehousePickupLimitCO;
    }

    public static List<WarehousePickupLimitCO> doToCoObjects(List<WarehouseLimit> warehouseLimitList) {
        List<WarehousePickupLimitCO> limitCOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(warehouseLimitList)) {
            return limitCOList;
        }
        for (WarehouseLimit warehouseLimit : warehouseLimitList) {
            WarehousePickupLimitCO warehousePickupLimitCO = new WarehousePickupLimitCO();
            warehousePickupLimitCO.setPickUpQuantity(warehouseLimit.getPickUpQuantity());
            warehousePickupLimitCO.setLimitFlag(warehouseLimit.getLimitFlag());
            limitCOList.add(warehousePickupLimitCO);
        }
        return limitCOList;
    }
}
