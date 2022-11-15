package org.o2.metadata.console.infra.lovadapter.repository;

import org.o2.metadata.console.app.bo.CurrencyBO;
import org.o2.metadata.console.app.bo.UomBO;
import org.o2.metadata.console.app.bo.UomTypeBO;

import java.util.List;
import java.util.Map;

/**
 * 基础LOVService,具有业务属性 基本单位
 *
 * @author wei.cai@hand-china.com 2021/8/24
 */
public interface BaseLovQueryRepository {

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

}
