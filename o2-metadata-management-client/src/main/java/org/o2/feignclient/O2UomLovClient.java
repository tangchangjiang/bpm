package org.o2.feignclient;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.feignclient.metadata.domain.co.UomCO;
import org.o2.feignclient.metadata.infra.feign.LovAdapterRemoteService;

import java.util.List;
import java.util.Map;

/**
 *
 * 单位值集查询
 *
 * @author yipeng.zhu@hand-china.com 2021-09-22
 **/
public class O2UomLovClient {

    private LovAdapterRemoteService lovAdapterRemoteService;

    public O2UomLovClient(LovAdapterRemoteService lovAdapterRemoteService) {
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

}
