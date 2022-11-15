package org.o2.metadata.console.infra.lovadapter.repository.impl;

import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.base.AopProxy;
import org.o2.cache.util.CacheHelper;
import org.o2.metadata.console.infra.constant.MetadataCacheConstants;
import org.o2.metadata.console.infra.lovadapter.repository.HzeroLovQueryRepository;
import org.o2.metadata.console.infra.lovadapter.repository.IdpLovQueryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 独立值集查询
 *
 * @author yipeng.zhu@hand-china.com 2021-09-23
 **/
@Repository
public class IdpLovQueryRepositoryImpl implements IdpLovQueryRepository, AopProxy<IdpLovQueryRepositoryImpl> {

    private final HzeroLovQueryRepository hzeroLovQueryRepository;

    public IdpLovQueryRepositoryImpl(HzeroLovQueryRepository hzeroLovQueryRepository) {
        this.hzeroLovQueryRepository = hzeroLovQueryRepository;
    }

    @Override
    public List<LovValueDTO> queryLovValue(Long tenantId, String lovCode) {
        return CacheHelper.getCache(
                MetadataCacheConstants.CacheName.O2_LOV,
                MetadataCacheConstants.KeyPrefix.getIdpPrefix(tenantId, lovCode),
                tenantId, lovCode,
                hzeroLovQueryRepository::queryLovValue,
                false
        );
    }

    @Override
    public String queryLovValueMeaning(Long tenantId, String lovCode, String lovValue) {
        List<LovValueDTO> list = this.queryLovValue(tenantId, lovCode);
        if (!list.isEmpty()) {
            for (LovValueDTO dto : list) {
                if (dto.getValue().equals(lovValue)) {
                    return dto.getMeaning();
                }
            }
        }
        return "";
    }
}
