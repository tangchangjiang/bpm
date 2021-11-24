package org.o2.metadata.app.service.impl;

import org.o2.metadata.api.co.WarehouseCO;
import org.o2.metadata.app.service.WarehouseService;
import org.o2.metadata.domain.warehouse.repository.WarehouseDomainRepository;
import org.o2.metadata.infra.convertor.WarehouseConverter;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseDomainRepository warehouseDomainRepository;

    public WarehouseServiceImpl(WarehouseDomainRepository warehouseDomainRepository) {
        this.warehouseDomainRepository = warehouseDomainRepository;
    }
    @Override
    public List<WarehouseCO> listWarehouses(List<String> warehouseCodes, Long tenantId) {
        return WarehouseConverter.doToCoListObjects(warehouseDomainRepository.listWarehouses(warehouseCodes,tenantId));
    }

    @Override
    public void updateExpressValue(String warehouseCode, String increment, Long tenantId) {
        warehouseDomainRepository.updateExpressValue(warehouseCode,increment,tenantId);
    }

}
