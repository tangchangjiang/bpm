package org.o2.metadata.client.infra.feign;

import io.swagger.annotations.ApiParam;
import org.o2.core.common.O2Service;
import org.o2.metadata.client.infra.feign.fallback.WarehouseRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
@FeignClient(
        value = O2Service.Metadata.NAME,
        path = "/v1",
        fallback = WarehouseRemoteServiceImpl.class
)
public interface WarehouseRemoteService {


    /**
     * 保存
     *
     * @param organizationId  租户ID
     * @param warehouseCode 仓库
     * @param hashMap  map
     * @return string
     */
    @PostMapping({"/{organizationId}/warehouse-internal/saveWarehouse"})
    ResponseEntity<String> saveWarehouse(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                         @RequestParam(value = "warehouseCode", required = true) String warehouseCode,
                                         @RequestParam(value = "hashMap", required = false) Map<String, Object> hashMap);

    /**
     * 更新
     * @param organizationId 租户ID
     * @param warehouseCode 仓库编码
     * @param hashMap map
     * @return ResponseEntity
     */
    @PostMapping({"/{organizationId}/warehouse-internal/updateWarehouse"})
    ResponseEntity<String> updateWarehouse(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                           @RequestParam(value = "warehouseCode", required = true) String warehouseCode,
                                           @RequestParam(value = "hashMap", required = false) Map<String, Object> hashMap);


    /**
     * 从redis查询系统仓库
     *
     * @param warehouseCodes 仓库编码
     * @param organizationId 租户ID
     * @return ResponseEntity<String>
     */
    @GetMapping("/{organizationId}/warehouse-internal/list")
    ResponseEntity<String>  listWarehouses(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                           @RequestParam(value = "warehouseCodes") List<String> warehouseCodes);
    /**
     * 仓库快递配送接单量增量更新
     * @param organizationId 租户ID
     * @param warehouseCode 仓库编码
     * @param increment 增长量
     * @return ResponseEntity
     */
    @PostMapping({"/{organizationId}/warehouse-internal/updateExpressValue"})
    ResponseEntity<String> updateExpressValue(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                              @RequestParam(value = "warehouseCode") String warehouseCode,
                                              @RequestParam(value = "increment") String increment);

    /**
     * 仓库已自提量查询
     *
     * @param organizationId 租户Id
     * @param warehouseCodes 仓库编码
     * @return list
     */
    @GetMapping("/{organizationId}/warehouse-internal/limit-list")
    ResponseEntity<String> listWarehousePickupLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                    @RequestParam List<String> warehouseCodes);

    /**
     * 批量查询服务点对应仓库
     * @param organizationId 租户id
     * @param posCodes 服务点
     * @return 仓库
     */
    @PostMapping("/{organizationId}/warehouse-internal/list-pos")
    ResponseEntity<String> listWarehousesByPosCode(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                   @RequestParam(value = "posCodes") List<String> posCodes);
}
