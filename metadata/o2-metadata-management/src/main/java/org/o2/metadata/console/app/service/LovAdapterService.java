package org.o2.metadata.console.app.service;

import java.util.List;
import java.util.Map;

import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.o2.metadata.console.api.co.PageCO;
import org.o2.metadata.console.api.dto.RegionQueryLovInnerDTO;
import org.o2.metadata.console.app.bo.CurrencyBO;
import org.o2.metadata.console.app.bo.UomBO;
import org.o2.metadata.console.app.bo.UomTypeBO;
import org.o2.metadata.console.infra.entity.Region;
import org.springframework.http.ResponseEntity;

import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 值集查询
 *
 * @author yipeng.zhu@hand-china.com 2021-08-30
 **/
public interface LovAdapterService {
    /**
     * 通过编码查询货币(批量)
     *
     * @param tenantId      租户ID
     * @param currencyCodes 货币编码
     * @return 返回信息MAP
     */
    Map<String, CurrencyBO> findCurrencyByCodes(Long tenantId, List<String> currencyCodes);

    /**
     * 通过编码查询单位(批量)
     *
     * @param tenantId 租户ID
     * @param uomCodes 单位编码
     * @return 单位信息MAP
     */
    Map<String, UomBO> findUomByCodes(Long tenantId, List<String> uomCodes);

    /**
     * 通过编码查询单位类型(批量)
     *
     * @param tenantId     租户ID
     * @param uomTypeCodes 单位类型编码
     * @return 单位类型信息MAP
     */
    Map<String, UomTypeBO> findUomTypeByCodes(Long tenantId, List<String> uomTypeCodes);

    /**
     * 查询地区值集
     *
     * @param tenantId 租户ID
     * @param innerDTO 查询条件
     * @return list
     */
    List<Region> queryRegion(Long tenantId, RegionQueryLovInnerDTO innerDTO);

    /**
     * 分页查询地区值集
     *
     * @param tenantId 租户ID
     * @param page     page 页码
     * @param size     大小
     * @param innerDTO 查询参数
     * @return page
     */
    PageCO<Region> queryRegionPage(Long tenantId, Integer page, Integer size, RegionQueryLovInnerDTO innerDTO);

    /**
     * 根据编码以及租户ID批量查集值
     *
     * @param queryMap 查询条件
     * @param tenantId 租户ID
     * @return 值集集合
     */
    ResponseEntity<Map<String, List<LovValueDTO>>> batchQueryLovInfo(Map<String, String> queryMap, Long tenantId);

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
     * 查询独立值集详细信息
     *
     * @param lovCode  值集code
     * @param tenantId 租户id
     * @return List<LovValueDTO>
     */
    List<LovValueDTO> queryLovValue(Long tenantId, String lovCode);

    /**
     * 查询独立值集中指定值的 描述信息（meaning）
     *
     * @param tenantId 租户id
     * @param lovCode  值集code
     * @param lovValue 值集value
     * @return String
     */
    String queryLovValueMeaning(Long tenantId, String lovCode, String lovValue);

    /**
     * 批量查询指定值集内容
     *
     * @param tenantId         租户ID
     * @param lovCode          值集编码
     * @param queryLovValueMap * queryLovValueMap is <valueCode,value>
     *                         * eg <countryCode,'CN'>
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> queryLovValueMeaning(Long tenantId,
                                                   String lovCode,
                                                   Map<String, String> queryLovValueMap);

    /**
     * 分页查询指定值集内容
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
     * 分页查询指定值集内容
     *
     * @param tenantId         租户ID
     * @param lovCode          值集编码
     * @param page             页码
     * @param size             大小
     * @param queryLovValueMap * queryLovValueMap is <valueCode,value>
     *                         * eg <countryCode,'CN'>
     * @param useCache         是否使用缓存
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> queryLovValueMeaning(Long tenantId,
                                                   String lovCode,
                                                   Integer page,
                                                   Integer size,
                                                   Map<String, String> queryLovValueMap,
                                                   boolean useCache);
}
