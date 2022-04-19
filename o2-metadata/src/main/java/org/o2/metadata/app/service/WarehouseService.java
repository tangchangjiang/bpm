package org.o2.metadata.app.service;

import org.o2.metadata.api.co.WarehouseCO;
import org.o2.metadata.infra.entity.WarehouseLimit;

import java.util.List;
import java.util.Map;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
public interface WarehouseService {
    /**
     * 获取仓库信息
     * @param warehouseCodes 仓库编码
     * @param organizationId 租户ID
     * @return 仓库
     */
    List<WarehouseCO> listWarehouses(List<String> warehouseCodes, Long organizationId);

    /**
     * 仓库快递配送接单量增量更新
     *
     * @param tenantId       租户ID
     * @param warehouseCode  仓库编码
     * @param increment      快递配送接单量增量
     */
    void updateExpressValue(String warehouseCode, String increment, Long tenantId);

    /**
     * 查询仓库已自提量
     *
     * @param warehouseCodes 仓库code
     * @param tenantId 租户Id
     * @return 已自提量
     */
    Map<String, WarehouseLimit> listWarehousePickupLimit(List<String> warehouseCodes, Long tenantId);
}
