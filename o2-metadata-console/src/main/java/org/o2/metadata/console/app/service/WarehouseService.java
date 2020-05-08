package org.o2.metadata.console.app.service;

import org.o2.metadata.core.domain.entity.Warehouse;

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

}
