package org.o2.feignclient.metadata.infra.feign;

import io.swagger.annotations.ApiParam;
import org.o2.feignclient.metadata.infra.constants.O2Service;
import org.o2.feignclient.metadata.infra.feign.fallback.WarehouseRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * 仓库
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
@FeignClient(
        value = O2Service.metadata.NAME,
        path = "/v1",
        fallback = WarehouseRemoteServiceImpl.class
)
public interface WarehouseRemoteService {
    /**
     * 从redis查询系统仓库
     *
     * @param warehouseCode 仓库编码
     * @param organizationId 租户ID
     * @return ResponseEntity<String>
     */
    @GetMapping("/{organizationId}/warehouse-internal/{warehouseCode}")
    ResponseEntity<String>  getWarehouse(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId, @PathVariable(value = "warehouseCode") @ApiParam(value = "参数code", required = true)String warehouseCode);
}
