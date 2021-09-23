package org.o2.feignclient;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.feignclient.metadata.domain.co.PageCO;
import org.o2.feignclient.metadata.infra.feign.LovAdapterRemoteService;

import java.util.List;
import java.util.Map;

/**
 *
 * 值集适配查询
 *
 * @author yipeng.zhu@hand-china.com 2021-09-23
 **/
public class AdapterLovClient {

    private LovAdapterRemoteService lovAdapterRemoteService;

    public AdapterLovClient(LovAdapterRemoteService lovAdapterRemoteService) {
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
     * 批量查询指定值集内容
     *
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
     * 分页查询URL值集
     *
     * @param tenantId 租户ID
     * @param lovCode 值集编码
     * @param page 页码
     * @param size  大小
     * @param queryParams * queryLovValueMap is <valueCode,value>
     *                         * eg <countryCode,'CN'>
     * @return List<Map < String, Object>>
     */
    public PageCO<Map<String, Object>> queryLovMapPage(Long tenantId, String lovCode, Integer page, Integer size, Map<String, String> queryParams) {
        return ResponseUtils.getResponse(lovAdapterRemoteService.queryLovPage(tenantId, lovCode, page,size, queryParams), new TypeReference<PageCO<Map<String, Object>>>() {
        });
    }
}
