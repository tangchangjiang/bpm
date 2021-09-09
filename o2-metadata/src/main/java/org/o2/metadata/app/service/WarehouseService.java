package org.o2.metadata.app.service;

import org.o2.metadata.api.vo.WarehouseCO;

import java.util.List;

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
}
