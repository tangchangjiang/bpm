package org.o2.metadata.console.app.service.impl;

import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.o2.lov.app.service.BaseLovQueryService;
import org.o2.lov.app.service.PublicLovQueryService;
import org.o2.lov.domain.bo.CurrencyBO;
import org.o2.lov.domain.bo.UomBO;
import org.o2.lov.domain.bo.UomTypeBO;
import org.o2.metadata.console.app.service.LovAdapterService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *
 * 值集查询
 *
 * @author yipeng.zhu@hand-china.com 2021-08-30
 **/
@Service
public class LovAdapterServiceImpl implements LovAdapterService {
    private BaseLovQueryService baseLovQueryService;
    private PublicLovQueryService publicLovQueryService;

    public LovAdapterServiceImpl(BaseLovQueryService baseLovQueryService,
                                 PublicLovQueryService publicLovQueryService) {
        this.baseLovQueryService = baseLovQueryService;
        this.publicLovQueryService = publicLovQueryService;
    }


    @Override
    public Map<String, CurrencyBO> findCurrencyByCodes(Long tenantId, List<String> currencyCodes) {
        return baseLovQueryService.findCurrencyByCodes(tenantId,currencyCodes);
    }

    @Override
    public Map<String, UomBO> findUomByCodes(Long tenantId, List<String> uomCodes) {
        return baseLovQueryService.findUomByCodes(tenantId,uomCodes);
    }

    @Override
    public Map<String, UomTypeBO> findUomTypeByCodes(Long tenantId, List<String> uomTypeCodes) {
        return baseLovQueryService.findUomTypeByCodes(tenantId,uomTypeCodes);
    }

    @Override
    public ResponseEntity<Map<String, List<LovValueDTO>>> batchQueryLovInfo(Map<String, String> queryMap, Long tenantId) {
        return publicLovQueryService.batchQueryLovInfo(queryMap,tenantId);
    }
}
