package org.o2.feignclient;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.feignclient.metadata.domain.co.LovValueCO;
import org.o2.feignclient.metadata.infra.feign.LovAdapterRemoteService;

import java.util.List;

/**
 *
 * 独立值集查询
 *
 * @author yipeng.zhu@hand-china.com 2021-09-22
 **/
public class IDPLovClient {
    private LovAdapterRemoteService lovAdapterRemoteService;

    public IDPLovClient(LovAdapterRemoteService lovAdapterRemoteService) {
        this.lovAdapterRemoteService = lovAdapterRemoteService;
    }

    /**
     * 独立值集
     *
     * @param lovCode  值集code
     * @param tenantId 租户id
     * @return List<LovValueDTO>
     */
    public List<LovValueCO> queryLovValue(Long tenantId, String lovCode) {
        return ResponseUtils.getResponse(lovAdapterRemoteService.queryLovValue(tenantId, lovCode), new TypeReference<List<LovValueCO>>() {
        });
    }
}
