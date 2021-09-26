package org.o2.metadata.console.infra.redis;

import org.o2.metadata.console.infra.entity.Warehouse;

import java.util.List;

/**
 *
 * 仓库
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
public interface WarehouseRedis {

    /**
     * 查询仓库信息
     * @param warehouseCodes 仓库编码
     * @param tenantId 租户ID
     * @return 仓库
     */
    List<Warehouse> listWarehouses(List<String> warehouseCodes, Long tenantId );

    /**
     * 批量更新
     * @param warehouseCodes 仓库编码
     * @param tenantId 租户ID
     */
    void batchUpdateWarehouse(List<String> warehouseCodes, Long tenantId);

    /**
     * 保存仓库快递配送接单量限制
     *
     * @param tenantId        租户ID
     * @param warehouseCode   仓库编码
     * @param expressQuantity 快递配送接单量限制
     * @return -1 失败 1成功
     */
    Long updateExpressQuantity(String warehouseCode, String expressQuantity, Long tenantId);

    /**
     * 仓库自提接单量增量更新
     *
     * @param tenantId      租户ID
     * @param warehouseCode 仓库编码
     * @param pickUpQuantity     自提单量增量
     * @return 1 成功 -1 失败
     */
    Long updatePickUpValue(String warehouseCode, String pickUpQuantity, Long tenantId);



}
