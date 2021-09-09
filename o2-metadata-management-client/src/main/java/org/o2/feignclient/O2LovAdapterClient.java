package org.o2.feignclient;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.feignclient.metadata.domain.co.CurrencyCO;
import org.o2.feignclient.metadata.domain.co.LovValueCO;
import org.o2.feignclient.metadata.domain.co.UomCO;
import org.o2.feignclient.metadata.domain.co.UomTypeCO;
import org.o2.feignclient.metadata.infra.feign.LovAdapterRemoteService;

import java.util.List;
import java.util.Map;

/**
 *
 * 值集查询
 *
 * @author yipeng.zhu@hand-china.com 2021-08-30
 **/
public class O2LovAdapterClient {
    private LovAdapterRemoteService lovAdapterRemoteService;

    public O2LovAdapterClient(LovAdapterRemoteService lovAdapterRemoteService) {
        this.lovAdapterRemoteService = lovAdapterRemoteService;
    }

    /**
     * 通过编码查询货币(批量)
     * @param tenantId 租户ID
     * @param currencyCodes 货币编码
     * @return 返回信息MAP
     */
    public Map<String, CurrencyCO> findCurrencyByCodes(Long tenantId, List<String> currencyCodes) {
        return ResponseUtils.getResponse(lovAdapterRemoteService.findCurrencyByCodes(tenantId, currencyCodes), new TypeReference<Map<String, CurrencyCO>>() {
        });
    }

    /**
     * 通过编码查询单位(批量)
     * @param tenantId 租户ID
     * @param uomCodes 单位编码
     * @return 单位信息MAP
     */
    public Map<String, UomCO> findUomByCodes(Long tenantId, List<String> uomCodes) {
        return ResponseUtils.getResponse(lovAdapterRemoteService.findUomByCodes(tenantId, uomCodes), new TypeReference<Map<String, UomCO>>() {
        });
    }

    /**
     * 通过编码查询单位类型(批量)
     * @param tenantId 租户ID
     * @param uomTypeCodes 单位类型编码
     * @return 单位类型信息MAP
     */
    public Map<String, UomTypeCO> findUomTypeByCodes(Long tenantId, List<String> uomTypeCodes) {
        return ResponseUtils.getResponse(lovAdapterRemoteService.findUomTypeByCodes(tenantId, uomTypeCodes), new TypeReference<Map<String, UomTypeCO>>() {
        });
    }

    /**
     * 查询值集详细信息
     *
     * @param lovCode  值集code
     * @param tenantId 租户id
     * @return List<LovValueDTO>
     */
    public List<LovValueCO> queryLovValue(Long tenantId, String lovCode) {
        return ResponseUtils.getResponse(lovAdapterRemoteService.queryLovValue(tenantId, lovCode), new TypeReference<List<LovValueCO>>() {
        });
    }

    /**
     * 查询值集中指定值的 描述信息（meaning）
     *
     * @param tenantId 租户id
     * @param lovCode  值集code
     * @param lovValue 值集value
     * @return String
     */
    public String queryLovValueMeaning(Long tenantId, String lovCode, String lovValue) {
        return ResponseUtils.getResponse(lovAdapterRemoteService.queryLovValueMeaning(tenantId, lovCode, lovValue), String.class);
    }

    /**
     * 批量查询指定值集内容
     *
     * @param tenantId
     * @param lovCode
     * @param queryLovValueMap * queryLovValueMap is <valueCode,value>
     *                         * eg <countryCode,'CN'>
     * @return List<Map < String, Object>>
     */
    public List<Map<String, Object>> queryLovValueMeaning(Long tenantId, String lovCode, Map<String, String> queryLovValueMap) {
        return ResponseUtils.getResponse(lovAdapterRemoteService.queryLovValueMeaning(tenantId, lovCode, queryLovValueMap), new TypeReference<List<Map<String, Object>>>() {
        });
    }

}
