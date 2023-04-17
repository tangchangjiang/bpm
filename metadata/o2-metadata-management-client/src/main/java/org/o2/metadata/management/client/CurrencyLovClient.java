package org.o2.metadata.management.client;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.metadata.management.client.domain.co.CurrencyCO;
import org.o2.metadata.management.client.infra.feign.LovAdapterRemoteService;

import java.util.List;
import java.util.Map;

/**
 * 货币值集查询
 *
 * @author yipeng.zhu@hand-china.com 2021-09-22
 **/
public class CurrencyLovClient {

    private LovAdapterRemoteService lovAdapterRemoteService;

    public CurrencyLovClient(LovAdapterRemoteService lovAdapterRemoteService) {
        this.lovAdapterRemoteService = lovAdapterRemoteService;
    }

    /**
     * 通过编码查询货币(批量)
     *
     * @param tenantId      租户ID
     * @param currencyCodes 货币编码
     * @return 返回信息MAP
     */
    public Map<String, CurrencyCO> findCurrencyByCodes(Long tenantId, List<String> currencyCodes) {
        return ResponseUtils.getResponse(lovAdapterRemoteService.findCurrencyByCodes(tenantId, currencyCodes), new TypeReference<Map<String,
                CurrencyCO>>() {
        });
    }

    /**
     * 多租户批量查询货币
     *
     * @param currencyCodes 货币编码
     * @return map tenantId:currencyCode:CurrencyCO
     */
    public Map<Long, Map<String, CurrencyCO>> findCurrencyByCodesBatchTenant(Map<String, List<String>> currencyCodes) {
        return ResponseUtils.getResponse(lovAdapterRemoteService.findCurrencyByCodesBatchTenant(currencyCodes), new TypeReference<Map<Long, Map<String, CurrencyCO>>>() {
        });
    }
}
