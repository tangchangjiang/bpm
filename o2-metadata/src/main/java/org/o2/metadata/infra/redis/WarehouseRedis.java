package org.o2.metadata.infra.redis;


import org.o2.metadata.infra.entity.Warehouse;

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
    List<Warehouse> listWarehouses(List<String> warehouseCodes, Long tenantId);
}
