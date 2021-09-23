package org.o2.metadata.console.infra.lovadapter.repository.impl;

import org.hzero.core.base.AopProxy;
import org.o2.metadata.console.infra.lovadapter.repository.HzeroLovQueryRepository;
import org.o2.metadata.console.infra.lovadapter.repository.LovGeneralQueryRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *
 * 通用方法
 *
 * @author yipeng.zhu@hand-china.com 2021-09-23
 **/
@Repository
public class LovGeneralQueryRepositoryImpl implements LovGeneralQueryRepository, AopProxy<LovGeneralQueryRepositoryImpl> {

    private HzeroLovQueryRepository hzeroLovQueryRepository;

    public LovGeneralQueryRepositoryImpl(HzeroLovQueryRepository hzeroLovQueryRepository) {
        this.hzeroLovQueryRepository = hzeroLovQueryRepository;
    }

    @Override

    public List<Map<String, Object>> queryLovValueMeaning(Long tenantId, String lovCode, Map<String, String> queryLovValueMap) {
        StringBuilder cacheKey = new StringBuilder(lovCode).append("_");
        String queryStr = hzeroLovQueryRepository.getQueryParamStr(tenantId,queryLovValueMap);
        cacheKey.append(queryStr);
        return self().queryLovValueMeaning(tenantId,lovCode,null,null,queryLovValueMap,cacheKey.toString());
    }

    @Override
    public List<Map<String, Object>> queryLovValueMeaning(Long tenantId, String lovCode, Integer page, Integer size, Map<String, String> queryLovValueMap) {
        StringBuilder cacheKey = new StringBuilder(lovCode).append("_");
        cacheKey.append(page).append("_");
        cacheKey.append(size).append("_");
        String queryStr = hzeroLovQueryRepository.getQueryParamStr(tenantId,queryLovValueMap);
        cacheKey.append(queryStr);
        return self().queryLovValueMeaning(tenantId,lovCode,page, size,queryLovValueMap,cacheKey.toString());
    }
    @Cacheable(value = "O2_LOV", key = "'general'+'_'+#cacheKey")
    public List<Map<String, Object>> queryLovValueMeaning(Long tenantId, String lovCode, Integer page, Integer size, Map<String, String> queryLovValueMap,String cacheKey) {
        return hzeroLovQueryRepository.queryLovValueMeaning(tenantId,lovCode,page, size, queryLovValueMap);
    }
}
