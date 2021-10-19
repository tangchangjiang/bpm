package org.o2.metadata.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.o2.metadata.api.co.CurrencyCO;
import org.o2.metadata.app.service.LovAdapterService;
import org.o2.metadata.infra.lovadapter.repository.BaseLovQueryRepository;
import org.o2.metadata.infra.lovadapter.repository.IdpLovQueryRepository;
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
@Slf4j
public class LovAdapterServiceImpl implements LovAdapterService {
    private IdpLovQueryRepository idpLovQueryRepository;
    private BaseLovQueryRepository baseLovQueryRepository;

    public LovAdapterServiceImpl(IdpLovQueryRepository idpLovQueryRepository, BaseLovQueryRepository baseLovQueryRepository) {
        this.idpLovQueryRepository = idpLovQueryRepository;
        this.baseLovQueryRepository = baseLovQueryRepository;
    }


    @Override
    public String queryLovValueMeaning(Long tenantId, String lovCode, String lovValue) {
        return idpLovQueryRepository.queryLovValueMeaning(tenantId,lovCode,lovValue);
    }
    @Override
    public Map<String, CurrencyCO> findCurrencyByCodes(Long tenantId, List<String> currencyCodes) {
        return baseLovQueryRepository.findCurrencyByCodes(tenantId, currencyCodes);
    }
}
