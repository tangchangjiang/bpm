package org.o2.metadata.app.service;

import org.o2.metadata.api.vo.WarehouseVO;
import org.o2.metadata.infra.entity.Warehouse;

import java.util.Map;
import java.util.Set;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
public interface WarehouseService {

    WarehouseVO getWarehouse(String warehouseCode, Long organizationId);
}
