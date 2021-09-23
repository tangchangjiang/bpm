package org.o2.metadata.console.infra.lovadapter.repository;

import java.util.List;
import java.util.Map;

/**
 *
 *  通用值集查询方法
 *
 * @author yipeng.zhu@hand-china.com 2021-09-23
 **/
public interface LovGeneralQueryRepository {

    /**
     * 批量查询指定值集内容
     *
     * @param tenantId 租户ID
     * @param lovCode 值编码
     * @param queryLovValueMap * queryLovValueMap is <valueCode,value>
     *                         * eg <countryCode,'CN'>
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> queryLovValueMeaning(Long tenantId,
                                                   String lovCode,
                                                   Map<String, String> queryLovValueMap);

    /**
     * 批量查询指定值集内容
     *
     * @param tenantId 租户ID
     * @param lovCode 值集编码
     * @param page 页码
     * @param size 大小
     * @param queryLovValueMap * queryLovValueMap is <valueCode,value>
     *                         * eg <countryCode,'CN'>
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> queryLovValueMeaning(Long tenantId,
                                                   String lovCode,
                                                   Integer page,
                                                   Integer size,
                                                   Map<String, String> queryLovValueMap);
}
