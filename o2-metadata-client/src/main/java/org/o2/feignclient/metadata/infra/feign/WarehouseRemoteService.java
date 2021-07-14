package org.o2.feignclient.metadata.infra.feign;

import io.swagger.annotations.ApiParam;
import org.o2.feignclient.metadata.domain.dto.FreightDTO;
import org.o2.feignclient.metadata.infra.constants.O2Service;
import org.o2.feignclient.metadata.infra.feign.fallback.WarehouseRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
@FeignClient(
        value = O2Service.metadata.NAME,
        path = "/v1",
        fallback = WarehouseRemoteServiceImpl.class
)
public interface WarehouseRemoteService {


    /**
     * 保存
     *
     * @param organizationId
     * @param warehouseCode
     * @param hashMap
     * @return
     */
    @PostMapping({"/{organizationId}/internal/saveWarehouse"})
    ResponseEntity<String> saveWarehouse(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                         @RequestParam(value = "warehouseCode", required = true) String warehouseCode,
                                         @RequestParam(value = "hashMap", required = false) Map<String, Object> hashMap);

    /**
     * 更新
     * @param organizationId
     * @param warehouseCode
     * @param hashMap
     * @return ResponseEntity
     */
    @PostMapping({"/{organizationId}/internal/updateWarehouse"})
    ResponseEntity<String> updateWarehouse(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                           @RequestParam(value = "warehouseCode", required = true) String warehouseCode,
                                           @RequestParam(value = "hashMap", required = false) Map<String, Object> hashMap);


    /**
     * 从redis查询系统仓库
     *
     * @param warehouseCode 仓库编码
     * @param organizationId 租户ID
     * @return ResponseEntity<String>
     */
    @GetMapping("/{organizationId}/internal/{warehouseCode}")
    ResponseEntity<String>  getWarehouse(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId, @PathVariable(value = "warehouseCode") @ApiParam(value = "参数code", required = true)String warehouseCode);

}
