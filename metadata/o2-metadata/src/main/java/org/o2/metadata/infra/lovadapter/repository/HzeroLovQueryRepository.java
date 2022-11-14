package org.o2.metadata.infra.lovadapter.repository;

import org.hzero.boot.platform.lov.dto.LovValueDTO;

import java.util.List;
import java.util.Map;

/**
 * hzero lov查询工具
 *
 * @Date 2021年08月23日
 * @Author yipeng.zhu@hand-china.com
 */
public interface HzeroLovQueryRepository {


    /**
     * 独立值集详细信息
     *
     * @param lovCode  值集code
     * @param tenantId 租户id
     * @return List<LovValueDTO>
     */
    List<LovValueDTO> queryLovValue(Long tenantId,
                                    String lovCode);



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
}
