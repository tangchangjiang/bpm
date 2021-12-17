package org.o2.metadata.client;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.metadata.client.domain.co.UomTypeCO;
import org.o2.metadata.client.infra.feign.LovAdapterRemoteService;

import java.util.List;
import java.util.Map;

/**
 *
 * 单位类型值集
 *
 * @author yipeng.zhu@hand-china.com 2021-09-22
 **/
public class UomTypeLovClient {
    private LovAdapterRemoteService lovAdapterRemoteService;

    public UomTypeLovClient(LovAdapterRemoteService lovAdapterRemoteService) {
        this.lovAdapterRemoteService = lovAdapterRemoteService;
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
}
