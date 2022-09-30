package org.o2.metadata.infra.redis;


import org.o2.metadata.infra.entity.Pos;
import org.o2.metadata.infra.entity.Warehouse;
import org.o2.metadata.infra.entity.WarehouseLimit;

import java.util.List;
import java.util.Map;

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

    /**
     * 查询仓库已自提量
     * @param warehouseCodes 仓库编码
     * @param tenantId 租户ID
     * @return 仓库已自提量
     */
    Map<String, WarehouseLimit> listWarehouseLimit(List<String> warehouseCodes, Long tenantId);

    /**
     * 查询仓库信息
     * @param posCodes 仓库编码
     * @param tenantId 租户ID
     * @return 仓库
     */
    List<Pos> listWarehousesByPosCode(List<String> posCodes, Long tenantId);
}
