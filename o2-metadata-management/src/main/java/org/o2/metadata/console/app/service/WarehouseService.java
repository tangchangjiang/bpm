package org.o2.metadata.console.app.service;

import org.o2.metadata.console.domain.entity.Warehouse;

import java.util.List;

/**
 * @author NieYong
 * @Title WarehouseService
 * @Description
 * @date 2020/3/4 13:29
 **/

public interface WarehouseService {

    Integer create(Long tenantId, Warehouse warehouse);

    List<Warehouse> createBatch(Long tenantId, List<Warehouse> warehouses);

    Integer update(Warehouse warehouse);

    List<Warehouse> updateBatch(Long tenantId, List<Warehouse> warehouses);
    /**
     * 批量操作仓库
     * @date 2020-05-22
     * @param tenantId 租户ID
     * @param warehouses  仓库
     * @return  list 仓库集合
     */
    List<Warehouse> batchHandle(Long tenantId, List<Warehouse> warehouses);

}
