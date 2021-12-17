package org.o2.metadata.client;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.metadata.client.domain.co.WarehouseCO;
import org.o2.metadata.client.infra.feign.WarehouseRemoteService;

import java.util.List;
import java.util.Map;

/**
 *
 * 仓库
 *
 * @author yipeng.zhu@hand-china.com 2021-11-15
 **/
public class WarehouseClient {
    private final WarehouseRemoteService warehouseRemoteService;

    public WarehouseClient(WarehouseRemoteService warehouseRemoteService) {
        this.warehouseRemoteService = warehouseRemoteService;
    }

    /**
     * 从redis查询系统仓库
     *
     * @param warehouseCodes 仓库编码
     * @param tenantId 租户ID
     */
    public Map<String, WarehouseCO> listWarehouses(List<String> warehouseCodes, Long tenantId){
        return ResponseUtils.getResponse(warehouseRemoteService.listWarehouses(tenantId, warehouseCodes), new TypeReference<Map<String, WarehouseCO>>() {
        });
    }

}
