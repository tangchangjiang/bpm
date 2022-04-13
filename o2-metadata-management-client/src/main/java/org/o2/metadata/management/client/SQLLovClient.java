package org.o2.metadata.management.client;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.metadata.management.client.infra.feign.LovAdapterRemoteService;

import java.util.List;
import java.util.Map;

/**
 *
 * SQL值集查询
 *
 * @author yipeng.zhu@hand-china.com 2021-09-22
 **/
public class SQLLovClient {
    private LovAdapterRemoteService lovAdapterRemoteService;

    public SQLLovClient(LovAdapterRemoteService lovAdapterRemoteService) {
        this.lovAdapterRemoteService = lovAdapterRemoteService;
    }

    /**
     * 批量查询指定值集内容
     * @param tenantId 租户ID
     * @param lovCode 值集编码
     * @param queryLovValueMap * queryLovValueMap is <valueCode,value>
     *                         * eg <countryCode,'CN'>
     * @return List<Map < String, Object>>
     */
    public List<Map<String, Object>> queryLovValueMeaning(Long tenantId, String lovCode, Map<String, String> queryLovValueMap) {
        return ResponseUtils.getResponse(lovAdapterRemoteService.queryLovValueMeaning(tenantId, lovCode, queryLovValueMap), new TypeReference<List<Map<String, Object>>>() {
        });
    }

    /**
     * 分页查询指定值集
     *
     * @param tenantId 租户ID
     * @param lovCode 值集编码
     * @param page 页码
     * @param size  大小
     * @param queryLovValueMap * queryLovValueMap is <valueCode,value>
     *                         * eg <countryCode,'CN'>
     * @return List<Map < String, Object>>
     */
    public List<Map<String, Object>> queryLovValueMeaning(Long tenantId, String lovCode, Map<String, String> queryLovValueMap,int page,int size) {
        return ResponseUtils.getResponse(lovAdapterRemoteService.queryLovPage(tenantId, lovCode,page,size, queryLovValueMap), new TypeReference<List<Map<String, Object>>>() {
        });
    }

}
