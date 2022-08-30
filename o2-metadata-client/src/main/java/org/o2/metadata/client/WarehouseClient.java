package org.o2.metadata.client;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.metadata.client.domain.co.WarehouseCO;
import org.o2.metadata.client.domain.co.WarehousePickupLimitCO;
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
     * @return key:仓库编码 value:仓库
     */
    public Map<String, WarehouseCO> listWarehouses(List<String> warehouseCodes, Long tenantId){
        return ResponseUtils.getResponse(warehouseRemoteService.listWarehouses(tenantId, warehouseCodes), new TypeReference<Map<String, WarehouseCO>>() {
        });
    }

    /**
     * 仓库已自提量查询
     *
     * @param warehouseCodes 仓库code
     * @param tenantId 租户Id
     * @return key:仓库编码 value:仓库已自提量
     */
    public Map<String, WarehousePickupLimitCO> listWarehousePickupLimit(List<String> warehouseCodes, Long tenantId) {
        return ResponseUtils.getResponse(warehouseRemoteService.listWarehousePickupLimit(tenantId, warehouseCodes), new TypeReference<Map<String, WarehousePickupLimitCO>>(){});
    }

    /**
     * 批量查询服务点对应仓库
     * @param posCodes 服务点
     * @param tenantId 租户id
     * @return key:仓库编码 value:仓库
     */
    public Map<String, List<WarehouseCO>> listWarehousesByPosCode(List<String> posCodes, Long tenantId){
        return ResponseUtils.getResponse(warehouseRemoteService.listWarehousesByPosCode(tenantId, posCodes), new TypeReference<Map<String, List<WarehouseCO>>>() {
        });
    }
}
