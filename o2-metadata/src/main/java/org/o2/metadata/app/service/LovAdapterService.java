package org.o2.metadata.app.service;


import org.o2.metadata.api.co.CurrencyCO;
import org.o2.metadata.api.co.LovValuesCO;
import org.o2.metadata.api.co.RoleCO;
import org.o2.metadata.api.dto.RegionQueryLovInnerDTO;
import org.o2.metadata.app.bo.UomBO;
import org.o2.metadata.infra.entity.Region;

import java.util.List;
import java.util.Map;

/**
 *
 * 值集查询
 *
 * @author yipeng.zhu@hand-china.com 2021-08-30
 **/
public interface LovAdapterService {


    /**
     * 查询值集中指定值的 描述信息（meaning）
     *
     * @param tenantId 租户id
     * @param lovCode  值集code
     * @param lovValue 值集value
     * @return String
     */
    String queryLovValueMeaning(Long tenantId, String lovCode, String lovValue);

    /**
     * 查询独立值
     * @param tenantId 租户id
     * @param lovCodes  值集code
     * @return co
     */
    List<LovValuesCO>queryIdpLov(Long tenantId, List<String> lovCodes);


    /**
     * 通过编码查询货币(批量)
     * @param tenantId 租户ID
     * @param currencyCodes 货币编码
     * @return 返回信息MAP
     */
    Map<String, CurrencyCO> findCurrencyByCodes(Long tenantId, List<String> currencyCodes);

    /**
     * 通过编码查询单位(批量)
     * @param tenantId 租户ID
     * @param uomCodes 单位编码
     * @return 单位信息MAP
     */
    Map<String, UomBO> findUomByCodes(Long tenantId, List<String> uomCodes);

    /**
     * 查询地区值集
     * @param tenantId 租户ID
     * @param innerDTO 查询条件
     * @return  list
     */
    List<Region> queryRegion(Long tenantId, RegionQueryLovInnerDTO innerDTO);

    /**
     * 通过编码查询角色(批量)
     * @param organizationId 租户ID
     * @param roleCodes 角色编码
     * @return 角色
     */
    Map<String, RoleCO> findRoleByCodes(Long organizationId, List<String> roleCodes);
}
