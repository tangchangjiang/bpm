package org.o2.metadata.client;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.metadata.client.domain.co.LovValuesCO;
import org.o2.metadata.client.infra.feign.LovAdapterRemoteService;

import java.util.List;

/**
 *
 * IDPLovClient
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


    /**
     * 通过值集编码查询值集信息
     * @param tenantId 租户ID
     * @param lovCodes 编码集合
     * @return LovValuesCO
     */
    public List<LovValuesCO> queryLov(Long tenantId, List<String> lovCodes) {
        return ResponseUtils.getResponse(lovAdapterRemoteService.queryLov(tenantId, lovCodes), new TypeReference<List<LovValuesCO>>(){});

    }
}
