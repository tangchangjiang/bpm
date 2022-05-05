package org.o2.metadata.app.service.impl;

import org.o2.metadata.api.co.WarehouseCO;
import org.o2.metadata.api.co.WarehousePickupLimitCO;
import org.o2.metadata.app.service.WarehouseService;
import org.o2.metadata.domain.warehouse.repository.WarehouseDomainRepository;
import org.o2.metadata.infra.convertor.WarehouseConverter;
import org.o2.metadata.infra.convertor.WarehouseLimitConverter;
import org.o2.metadata.infra.entity.WarehouseLimit;
import org.o2.metadata.infra.redis.WarehouseRedis;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseDomainRepository warehouseDomainRepository;
    private final WarehouseRedis warehouseRedis;

    public WarehouseServiceImpl(WarehouseDomainRepository warehouseDomainRepository,
                                WarehouseRedis warehouseRedis) {
        this.warehouseDomainRepository = warehouseDomainRepository;
        this.warehouseRedis = warehouseRedis;
    }
    @Override
    public List<WarehouseCO> listWarehouses(List<String> warehouseCodes, Long tenantId) {
        return WarehouseConverter.doToCoListObjects(warehouseDomainRepository.listWarehouses(warehouseCodes,tenantId));
    }

    @Override
    public void updateExpressValue(String warehouseCode, String increment, Long tenantId) {
        warehouseDomainRepository.updateExpressValue(warehouseCode,increment,tenantId);
    }

    @Override
    public Map<String, WarehouseLimit> listWarehousePickupLimit(List<String> warehouseCodes, Long tenantId) {
        Map<String, WarehouseLimit> warehouseLimitMap = warehouseRedis.listWarehouseLimit(warehouseCodes, tenantId);
        return warehouseRedis.listWarehouseLimit(warehouseCodes, tenantId);
    }

    @Override
    public List<WarehouseCO> listWarehousesByPosCode(List<String> posCodes, Long tenantId) {
        return WarehouseConverter.toWarehouse(warehouseRedis.listWarehousesByPosCode(posCodes,tenantId));
    }

}
