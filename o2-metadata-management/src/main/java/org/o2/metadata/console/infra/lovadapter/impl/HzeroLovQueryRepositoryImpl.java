package org.o2.metadata.console.infra.lovadapter.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;

import org.o2.metadata.console.infra.constant.O2LovConstants;
import org.o2.metadata.console.infra.lovadapter.HzeroLovQueryRepository;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Slf4j
@EnableAspectJAutoProxy( proxyTargetClass = true , exposeProxy = true )
public class HzeroLovQueryRepositoryImpl implements HzeroLovQueryRepository {

    private LovAdapter lovAdapter;

    public HzeroLovQueryRepositoryImpl(LovAdapter lovAdapter) {
        this.lovAdapter = lovAdapter;
    }

    @Override
    @Cacheable(value = "O2_LOV", key = "#tenantId+'_'+#lovCode")
    public List<LovValueDTO> queryLovValue(Long tenantId,
                                           String lovCode) {
        return lovAdapter.queryLovValue(lovCode, tenantId);
    }

    @Override
    public String queryLovValueMeaning(Long tenantId,
                                       String lovCode,
                                       String lovValue) {
        StringBuilder cacheKey = new StringBuilder(tenantId.toString()).append("_").append(lovCode).append("_").append(lovValue);
        HzeroLovQueryRepositoryImpl currentProxy = (HzeroLovQueryRepositoryImpl) AopContext.currentProxy();
        List<Map<String, Object>> lovList = currentProxy.queryLovValueMeaning(tenantId, lovCode, null, cacheKey.toString());
        if (CollectionUtils.isEmpty(lovList) || MapUtils.isEmpty(lovList.get(0))) {
            return "";
        } else {
            String lovMeaning = "";
            for (Map<String, Object> lov : lovList) {
                if (lovValue.equals(lov.get(O2LovConstants.LovProperties.lovValue))) {
                    lovMeaning = (String) lov.get(O2LovConstants.LovProperties.lovMeaning);
                    break;
                }
            }
            return lovMeaning;
        }
    }

    @Override
    public List<Map<String, Object>> queryLovValueMeaning(Long tenantId,
                                                          String lovCode,
                                                          Map<String, String> queryLovValueMap) {
        StringBuilder cacheKey = new StringBuilder(tenantId.toString()).append("_").append(lovCode).append("_");
        for (Map.Entry<String, String> enty : queryLovValueMap.entrySet()) {
            if (StringUtils.isNotEmpty(enty.getValue())){
                cacheKey.append(enty.getKey()).append("_").append(enty.getValue()).append("_");
            }
        }
        HzeroLovQueryRepositoryImpl currentProxy = (HzeroLovQueryRepositoryImpl) AopContext.currentProxy();
        return currentProxy.queryLovValueMeaning(tenantId, lovCode, queryLovValueMap, cacheKey.toString());
    }

    @Cacheable(value = "O2_LOV", key = "#cacheKey")
    public List<Map<String, Object>> queryLovValueMeaning(Long tenantId,
                                                          String lovCode,
                                                          Map<String, String> queryLovValueMap,
                                                          String cacheKey) {
        return lovAdapter.queryLovData(lovCode, tenantId, null, null, null, queryLovValueMap);
    }
}
