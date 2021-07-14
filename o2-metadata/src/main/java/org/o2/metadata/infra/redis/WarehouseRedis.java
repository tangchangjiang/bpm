package org.o2.metadata.infra.redis;


import org.o2.metadata.infra.entity.Warehouse;

/**
 *
 * 仓库
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
public interface WarehouseRedis {

    /**
     * 查询仓库信息
     * @param warehouseCode 仓库编码
     * @param tenantId 租户ID
     * @return 仓库
     */
    Warehouse getWarehouse(String warehouseCode, Long tenantId);
}
