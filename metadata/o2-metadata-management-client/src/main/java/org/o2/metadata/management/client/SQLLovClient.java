package org.o2.metadata.management.client;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.metadata.management.client.domain.dto.QueryLovValueMeaningDTO;
import org.o2.metadata.management.client.infra.feign.LovAdapterRemoteService;

/**
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
     *
     * @param tenantId         租户ID
     * @param lovCode          值集编码
     * @param queryLovValueMap * queryLovValueMap is <valueCode,value>
     *                         * eg <countryCode,'CN'>
     * @return List<Map < String, Object>>
     */
    public List<Map<String, Object>> queryLovValueMeaning(Long tenantId, String lovCode, Map<String, String> queryLovValueMap) {
        return ResponseUtils.getResponse(lovAdapterRemoteService.queryLovValueMeaning(tenantId, lovCode, queryLovValueMap),
                new TypeReference<List<Map<String, Object>>>() {
                });
    }

    public List<Map<String, Object>> queryLovValueMeaningPage(Long tenantId, String lovCode, Map<String, String> queryLovValueMap, Integer page,
                                                              Integer size) {
        return ResponseUtils.getResponse(lovAdapterRemoteService.queryLovValueMeaningPage(tenantId, lovCode, queryLovValueMap, page, size),
                new TypeReference<List<Map<String, Object>>>() {
                });
    }

    /**
     * POST方式分页查询。兼容请求参数很多的场景。并且底层不使用缓存，查询结果是实时的
     *
     * @param tenantId 租户id
     * @param queryDTO 查询DTO
     * @return 结果
     */
    public List<Map<String, Object>> queryLovValueMeaningPageByPost(Long tenantId, QueryLovValueMeaningDTO queryDTO) {
        return ResponseUtils.getResponse(lovAdapterRemoteService.queryLovValueMeaningPageByPost(tenantId, queryDTO),
                new TypeReference<List<Map<String, Object>>>() {
                });
    }

    /**
     * 分页查询指定值集
     *
     * @param tenantId         租户ID
     * @param lovCode          值集编码
     * @param page             页码
     * @param size             大小
     * @param queryLovValueMap * queryLovValueMap is <valueCode,value>
     *                         * eg <countryCode,'CN'>
     * @return List<Map < String, Object>>
     */
    public List<Map<String, Object>> queryLovValueMeaning(Long tenantId, String lovCode, Map<String, String> queryLovValueMap, Integer page,
                                                          Integer size) {
        return ResponseUtils.getResponse(lovAdapterRemoteService.queryLovPage(tenantId, lovCode, page, size, queryLovValueMap),
                new TypeReference<List<Map<String, Object>>>() {
        });
    }

}
