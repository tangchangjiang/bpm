package org.o2.metadata.console.infra.lovadapter.repository.impl;

import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.base.AopProxy;
import org.o2.metadata.console.infra.lovadapter.repository.HzeroLovQueryRepository;
import org.o2.metadata.console.infra.lovadapter.repository.IdpLovQueryRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * 独立值集查询
 *
 * @author yipeng.zhu@hand-china.com 2021-09-23
 **/
@Repository
public class IdpLovQueryRepositoryImpl implements IdpLovQueryRepository, AopProxy<IdpLovQueryRepositoryImpl> {
    private HzeroLovQueryRepository hzeroLovQueryRepository;

    public IdpLovQueryRepositoryImpl(HzeroLovQueryRepository hzeroLovQueryRepository) {
        this.hzeroLovQueryRepository = hzeroLovQueryRepository;
    }

    @Override
    @Cacheable(value = "O2_LOV", key = "'idp'+'_'+#tenantId+'_'+#lovCode")
    public List<LovValueDTO> queryLovValue(Long tenantId, String lovCode) {
        return hzeroLovQueryRepository.queryLovValue(tenantId,lovCode);
    }

    @Override
    public String queryLovValueMeaning(Long tenantId, String lovCode, String lovValue) {
        List<LovValueDTO> list = self().queryLovValue(tenantId,lovCode);
        if (!list.isEmpty()) {
            for (LovValueDTO dto : list) {
                if (dto.getValue().equals(lovValue)){
                    return dto.getMeaning();
                }
            }
        }
        return "";
    }
}
