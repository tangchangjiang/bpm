package org.o2.metadata.infra.lovadapter.repository.impl;

import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.base.AopProxy;
import org.o2.cache.util.CollectionCacheHelper;
import org.o2.metadata.api.co.LovValuesCO;
import org.o2.metadata.infra.constants.O2LovConstants;
import org.o2.metadata.infra.lovadapter.repository.HzeroLovQueryRepository;
import org.o2.metadata.infra.lovadapter.repository.IdpLovQueryRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
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
    public String queryLovValueMeaning(Long tenantId, String lovCode, String lovValue) {
        List<LovValuesCO> result =  queryIdpLov(tenantId, Collections.singletonList(lovCode));
        if (result.isEmpty()) {
            return "";
        }
        List<LovValueDTO> valueList = result.get(0).getLovValueList();
            for (LovValueDTO dto : valueList) {
                if (dto.getValue().equals(lovValue)) {
                    return dto.getMeaning();
                }

        }
       return "";
    }

    @Override
    public List<LovValuesCO> queryIdpLov(Long tenantId, List<String> lovCodes) {
        Collection<LovValuesCO> result = CollectionCacheHelper.getCache2Collection(O2LovConstants.IdpLov.CACHE_NAME, O2LovConstants.IdpLov.KEY_PREFIX + tenantId, lovCodes, codes -> queryLovList(tenantId, codes), LovValuesCO::getLovCode);
        return new ArrayList<>(result);
    }

    /**
     * 批量查询独立值集
     *
     * @param tenantId 租户ID
     * @param lovCodes 值集集合
     * @return  Collection
     */
    private Collection<LovValuesCO> queryLovList(Long tenantId, Collection<String> lovCodes) {
        List<LovValuesCO> list = new ArrayList<>(lovCodes.size());
        for (String lovCode : lovCodes) {
            List<LovValueDTO> lov = hzeroLovQueryRepository.queryLovValue(tenantId, lovCode);
            LovValuesCO co = new LovValuesCO();
            co.setLovCode(lovCode);
            if (lov.isEmpty()) {
                co.setLovValueList(new ArrayList<>());
            }
            co.setLovValueList(lov);
            list.add(co);
        }
        return list;
    }
}
