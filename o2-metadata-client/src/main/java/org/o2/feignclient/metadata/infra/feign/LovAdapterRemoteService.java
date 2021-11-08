package org.o2.feignclient.metadata.infra.feign;

import io.swagger.annotations.ApiParam;
import org.o2.feignclient.metadata.domain.dto.RegionQueryLovInnerDTO;
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
     * 查询值集中指定值的 描述信息（meaning）
     *
     * @param organizationId 租户id
     * @param lovCode  值集code
     * @param lovValue 值集value
     * @return String
     */
    @GetMapping("/{organizationId}/lov/query-lov-value-meaning")
    ResponseEntity<String> queryLovValueMeaning(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                @RequestParam String lovCode,
                                                @RequestParam String lovValue);

    /**
     * 通过编码查询货币(批量)
     * @param organizationId 租户ID
     * @param currencyCodes 货币编码
     * @return 返回信息MAP
     */
    @GetMapping("/{organizationId}/lov/currency-by-codes")
    ResponseEntity<String> findCurrencyByCodes(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                               @RequestParam(value = "currencyCodes", required = false) List<String> currencyCodes);


    /**
     * 查询地区值集
     * @param innerDTO 查询条件
     * @param organizationId 租户ID
     * @return 值集集合
     */
    @GetMapping("/{organizationId}/lov/query-region-lov")
    ResponseEntity<String> queryRegion(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                       RegionQueryLovInnerDTO innerDTO);

    /**
     * 通过编码查询单位(批量)
     * @param organizationId 租户ID
     * @param uomCodes 单位编码
     * @return 单位信息MAP
     */
    @GetMapping("/{organizationId}/lov/uom-by-codes")
    ResponseEntity<String> findUomByCodes(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                          @RequestParam(value = "uomCodes", required = false) List<String> uomCodes);
}
