package org.o2.metadata.console.infra.lovadapter.repository;

import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.platform.lov.dto.LovValueDTO;

import java.util.List;
import java.util.Map;

/**
 * hzero lov查询工具
 *
 * @Date 2021年08月23日
 * @Author youlong.peng
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
     * 查询独立值集中指定值的 描述信息
     *
     * @param tenantId 租户id
     * @param lovCode  值集code
     * @param lovValue 值集value
     * @return String
     */
    String queryLovValueMeaning(Long tenantId,
                                String lovCode,
                                String lovValue);

    /**
     * 批量查询指定值集内容
     *
     * @param tenantId         租户ID
     * @param lovCode          值编码
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
     * @param tenantId         租户ID
     * @param lovCode          值集编码
     * @param page             页码
     * @param size             大小
     * @param queryLovValueMap * queryLovValueMap is <valueCode,value>
     *                         * eg <countryCode,'CN'>
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> queryLovValueMeaning(Long tenantId,
                                                   String lovCode,
                                                   Integer page,
                                                   Integer size,
                                                   Map<String, String> queryLovValueMap);

    /**
     * 分页查询url值集
     *
     * @param queryParam  查询参数
     * @param pageRequest 分页
     * @param lovCode     值集编码
     * @param tenantId    租户ID
     * @return page
     */
    String queryLovPage(Map<String, String> queryParam, PageRequest pageRequest, String lovCode, Long tenantId);

    /**
     * 获取缓存key
     *
     * @param tenantId   租户ID
     * @param queryParam 查询参数
     * @return str
     */
    String getQueryParamStr(Long tenantId, Map<String, String> queryParam);
}
