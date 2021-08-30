package org.o2.feignclient.metadata.infra.feign;

import io.swagger.annotations.ApiParam;
import org.o2.feignclient.metadata.infra.constants.O2Service;
import org.o2.feignclient.metadata.infra.feign.fallback.LovAdapterRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 *
 * 值集查询
 *
 * @author yipeng.zhu@hand-china.com 2021-08-30
 **/
@FeignClient(
        value = O2Service.Metadata.NAME,
        path = "/v1",
        fallback = LovAdapterRemoteServiceImpl.class
)
public interface LovAdapterRemoteService {

    /**
     * 通过编码查询货币(批量)
     * @param organizationId 租户ID
     * @param currencyCodes 货币编码
     * @return 返回信息MAP
     */
    @GetMapping("/{organizationId}/lov//currency-by-codes")
    ResponseEntity<String> findCurrencyByCodes(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                               @RequestParam List<String> currencyCodes);

    /**
     * 通过编码查询单位(批量)
     * @param organizationId 租户ID
     * @param uomCodes 单位编码
     * @return 单位信息MAP
     */
    @GetMapping("/{organizationId}/lov/uom-by-codes")
    ResponseEntity<String> findUomByCodes(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                          @RequestParam List<String> uomCodes);

    /**
     * 通过编码查询单位类型(批量)
     * @param organizationId 租户ID
     * @param uomTypeCodes 单位类型编码
     * @return 单位类型信息MAP
     */
    @GetMapping("/{organizationId}/lov/uomType-by-codes")
    ResponseEntity<String> findUomTypeByCodes(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                              @RequestParam List<String> uomTypeCodes);
}
