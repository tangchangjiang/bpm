package org.o2.metadata.app.service.impl;

import org.o2.metadata.api.vo.WarehouseVO;
import org.o2.metadata.app.service.WarehouseService;
import org.o2.metadata.domain.warehouse.service.WarehouseDomainService;
import org.o2.metadata.infra.convertor.WarehouseConvertor;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseDomainService warehouseDomainService;

    public WarehouseServiceImpl(WarehouseDomainService warehouseDomainService) {
        this.warehouseDomainService = warehouseDomainService;
    }
    @Override
    public List<WarehouseVO> listWarehouses(List<String> warehouseCodes, Long tenantId) {
        return WarehouseConvertor.doToVoListObjects(warehouseDomainService.listWarehouses(warehouseCodes,tenantId));
    }
}
