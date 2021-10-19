package org.o2.metadata.infra.lovadapter.repository;

import org.o2.metadata.api.co.CurrencyCO;


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
     * @param tenantId 租户ID
     * @param currencyCodes 货币编码
     * @return 返回信息MAP
     */
    Map<String, CurrencyCO> findCurrencyByCodes(Long tenantId, List<String> currencyCodes);




}
