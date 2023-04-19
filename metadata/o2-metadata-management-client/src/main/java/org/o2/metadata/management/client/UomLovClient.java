package org.o2.metadata.management.client;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.metadata.management.client.domain.co.UomCO;
import org.o2.metadata.management.client.infra.feign.LovAdapterRemoteService;

import java.util.List;
import java.util.Map;

/**
 *
 * 单位值集查询
 *
 * @author yipeng.zhu@hand-china.com 2021-09-22
 **/
public class UomLovClient {

    private LovAdapterRemoteService lovAdapterRemoteService;

    public UomLovClient(LovAdapterRemoteService lovAdapterRemoteService) {
        this.lovAdapterRemoteService = lovAdapterRemoteService;
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
     * 通过编码查询单位(批量-多租户)
     *
     * @param uomCodesMap 单位编码 tenantId:list
     * @return 单位信息MAP  tenantId:uomCode:UomCO
     */
    public Map<Long, Map<String, UomCO>> findUomByCodesBatchTenant(Map<Long, List<String>> uomCodesMap) {
        return ResponseUtils.getResponse(lovAdapterRemoteService.findUomByCodesBatchTenant(uomCodesMap), new TypeReference<Map<Long, Map<String, UomCO>>>() {
        });
    }

}
