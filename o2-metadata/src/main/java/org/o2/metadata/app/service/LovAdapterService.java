package org.o2.metadata.app.service;


import org.o2.metadata.api.co.CurrencyCO;

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
     * 通过编码查询货币(批量)
     * @param tenantId 租户ID
     * @param currencyCodes 货币编码
     * @return 返回信息MAP
     */
    Map<String, CurrencyCO> findCurrencyByCodes(Long tenantId, List<String> currencyCodes);
}
