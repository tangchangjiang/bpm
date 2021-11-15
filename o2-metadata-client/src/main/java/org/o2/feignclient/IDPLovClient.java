package org.o2.feignclient;

import org.hzero.core.util.ResponseUtils;
import org.o2.feignclient.metadata.infra.feign.LovAdapterRemoteService;

/**
 *
 * 独立值集
 *
 * @author yipeng.zhu@hand-china.com 2021-11-15
 **/
public class IDPLovClient {
    private LovAdapterRemoteService lovAdapterRemoteService;

    public IDPLovClient(LovAdapterRemoteService lovAdapterRemoteService) {
        this.lovAdapterRemoteService = lovAdapterRemoteService;
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
}
