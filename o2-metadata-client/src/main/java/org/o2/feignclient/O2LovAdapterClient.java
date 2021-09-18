package org.o2.feignclient;

import org.hzero.core.util.ResponseUtils;
import org.o2.feignclient.metadata.infra.feign.LovAdapterRemoteService;


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
