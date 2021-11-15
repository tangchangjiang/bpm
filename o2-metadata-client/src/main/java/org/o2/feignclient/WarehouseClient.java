package org.o2.feignclient;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.feignclient.metadata.domain.co.WarehouseCO;
import org.o2.feignclient.metadata.infra.feign.WarehouseRemoteService;

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

    /**
     * 仓库快递配送接单量增量更新
     *
     * @param organizationId 租户ID
     * @param warehouseCode  仓库编码
     * @param increment      快递配送接单量增量
     */
    public Boolean updateExpressValue(final Long organizationId, final String warehouseCode, final String increment) {
        return ResponseUtils.isFailed(warehouseRemoteService.updateExpressValue(organizationId, warehouseCode, increment));
    }

}
