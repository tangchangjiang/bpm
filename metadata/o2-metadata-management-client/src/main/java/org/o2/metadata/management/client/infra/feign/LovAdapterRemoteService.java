package org.o2.metadata.management.client.infra.feign;

import io.swagger.annotations.ApiParam;
import org.o2.core.common.O2Service;
import org.o2.metadata.management.client.domain.dto.LovQueryInnerDTO;
import org.o2.metadata.management.client.domain.dto.QueryLovValueMeaningDTO;
import org.o2.metadata.management.client.domain.dto.RegionQueryLovInnerDTO;
import org.o2.metadata.management.client.infra.feign.fallback.LovAdapterRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 *
 * 值集查询
 *
 * @author yipeng.zhu@hand-china.com 2021-08-30
 **/
@FeignClient(
        value = O2Service.MetadataManagement.NAME,
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
    @GetMapping("/{organizationId}/lov-internal/currency-by-codes")
    ResponseEntity<String> findCurrencyByCodes(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                               @RequestParam(value = "currencyCodes", required = false) List<String> currencyCodes);

    /**
     * 多租户批量查询货币
     *
     * @param currencyCodes 货币编码
     * @return map tenantId:currencyCode:CurrencyCO
     */
    @PostMapping("/lov-internal/currency-by-codes-batch-tenant")
    ResponseEntity<String> findCurrencyByCodesBatchTenant(@RequestBody Map<Long, List<String>> currencyCodes);

    /**
     * 通过编码查询单位(批量)
     * @param organizationId 租户ID
     * @param uomCodes 单位编码
     * @return 单位信息MAP
     */
    @GetMapping("/{organizationId}/lov-internal/uom-by-codes")
    ResponseEntity<String> findUomByCodes(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                          @RequestParam(value = "uomCodes", required = false) List<String> uomCodes);

    /**
     * 通过编码查询单位(批量-多租户)
     *
     * @param uomCodesMap 单位编码map
     * @return 单位信息MAP
     */
    @PostMapping("/lov-internal/uom-by-codes-batch-tenant")
    ResponseEntity<String> findUomByCodesBatchTenant(Map<Long, List<String>> uomCodesMap);

    /**
     * 通过编码查询单位类型(批量)
     * @param organizationId 租户ID
     * @param uomTypeCodes 单位类型编码
     * @return 单位类型信息MAP
     */
    @GetMapping("/{organizationId}/lov-internal/uomType-by-codes")
    ResponseEntity<String> findUomTypeByCodes(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                              @RequestParam(value = "uomTypeCodes", required = false) List<String> uomTypeCodes);

    /**
     * 查询独立值集详细信息
     *
     * @param lovCode  值集code
     * @param organizationId 租户id
     * @return str
     */
    @GetMapping("/{organizationId}/lov-internal/query-lov-value")
    ResponseEntity<String> queryLovValue(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                         @RequestParam String lovCode);

    /**
     * 查询独立值集详细信息(多租户)
     *
     * @param lovCodeMap 值集code map
     * @return 值集
     */
    @PostMapping("/lov-internal/query-lov-value-batch-tenant")
    ResponseEntity<String> queryLovValueBatchTenant(Map<Long, String> lovCodeMap);

    /**
     * 查询值集中指定值的 描述信息（meaning）
     *
     * @param organizationId 租户id
     * @param lovCode  值集code
     * @param lovValue 值集value
     * @return String
     */
    @GetMapping("/{organizationId}/lov-internal/query-lov-value-meaning")
    ResponseEntity<String> queryLovValueMeaning(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                @RequestParam String lovCode,
                                                @RequestParam String lovValue);

    /**
     * 批量查询指定值集内容
     *
     * @param organizationId 租户id
     * @param lovCode 值集code
     * @param queryLovValueMap * queryLovValueMap is <valueCode,value>
     *                         * eg <countryCode,'CN'>
     * @return List<Map < String, Object>>
     */
    @GetMapping("/{organizationId}/lov-internal/batch-query-lov-value-meaning")
    ResponseEntity<String> queryLovValueMeaning(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                @RequestParam String lovCode,
                                                @RequestParam Map<String, String> queryLovValueMap);

    /**
     * 批量查询指定值集内容
     *
     * @param organizationId 租户id
     * @param lovCode 值集code
     * @param queryLovValueMap * queryLovValueMap is <valueCode,value>
     *                         * eg <countryCode,'CN'>
     * @return List<Map < String, Object>>
     */
    @GetMapping("/{organizationId}/lov-internal/batch-page-query-lov-value-meaning")
    ResponseEntity<String> queryLovValueMeaningPage(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                    @RequestParam String lovCode,
                                                    @RequestParam Map<String, String> queryLovValueMap,
                                                    @RequestParam(required = false) Integer page,
                                                    @RequestParam(required = false) Integer size);


    @PostMapping("/{organizationId}/lov-internal/batch-page-query-lov-value-meaning")
    ResponseEntity<String> queryLovValueMeaningPageByPost(@PathVariable(value = "organizationId")
                                                          @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                          @RequestBody QueryLovValueMeaningDTO queryDTO);

    /**
     * 分页查询指定值集
     *
     * @param organizationId 租户ID
     * @param lovCode 值集编码
     * @param page 页码
     * @param size  大小
     * @param queryLovValueMap * queryLovValueMap is <valueCode,value>
     *                         * eg <countryCode,'CN'>
     * @return List<Map < String, Object>>
     */
    @GetMapping("/{organizationId}/lov-internal/page-query-lov-value")
    ResponseEntity<String> queryLovPage(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                        @RequestParam String lovCode,
                                        @RequestParam(required = false) Integer page,
                                        @RequestParam(required = false) Integer size,
                                        @RequestParam(required = false) Map<String, String> queryLovValueMap);

    /**
     * 查询地区值集
     * @param innerDTO 查询条件
     * @param organizationId 租户ID
     * @return 值集集合
     */
    @PostMapping("/{organizationId}/lov-internal/query-region-lov")
    ResponseEntity<String> queryRegion(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                       @RequestBody RegionQueryLovInnerDTO innerDTO);

    /**
     * 分页查询地区值集
     * @param organizationId 租户ID
     * @param page page 页码
     * @param size 大小
     * @param innerDTO 查询参数
     * @return page
     */
    @PostMapping("/{organizationId}/lov-internal/page-query-region-lov")
    ResponseEntity<String> queryRegionPage(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                           @RequestParam Integer page,
                                           @RequestParam Integer size,
                                           @RequestBody RegionQueryLovInnerDTO innerDTO);

    /**
     * 独立查询值集详细信息-批量
     * @param organizationId 租户ID
     * @param lovQueryInnerDTO 值集内部查询DTO
     * @return 值集
     */
    @PostMapping("/{organizationId}/lov-internal/batch-query-lov-value")
    ResponseEntity<String> batchQueryLovValueByLang(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                    @RequestBody LovQueryInnerDTO lovQueryInnerDTO);
}
