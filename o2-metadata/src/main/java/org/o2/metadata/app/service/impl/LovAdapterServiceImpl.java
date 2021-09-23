package org.o2.metadata.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.o2.metadata.app.service.LovAdapterService;
import org.o2.metadata.infra.lovadapter.repository.HzeroLovQueryRepository;
import org.springframework.stereotype.Service;


/**
 *
 * 值集查询
 *
 * @author yipeng.zhu@hand-china.com 2021-08-30
 **/
@Service
@Slf4j
public class LovAdapterServiceImpl implements LovAdapterService {
    private HzeroLovQueryRepository hzeroLovQueryRepository;


    public LovAdapterServiceImpl(HzeroLovQueryRepository hzeroLovQueryRepository) {
        this.hzeroLovQueryRepository = hzeroLovQueryRepository;
    }
    @Override
    public String queryLovValueMeaning(Long tenantId, String lovCode, String lovValue) {
        return hzeroLovQueryRepository.queryLovValueMeaning(tenantId,lovCode,lovValue);
    }

}
