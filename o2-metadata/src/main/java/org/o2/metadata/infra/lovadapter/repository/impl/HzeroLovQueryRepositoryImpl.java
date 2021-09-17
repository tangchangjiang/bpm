package org.o2.metadata.infra.lovadapter.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.o2.metadata.infra.constants.O2LovConstants;
import org.o2.metadata.infra.lovadapter.repository.HzeroLovQueryRepository;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
/**
 *
 * lov
 *
 * @author yipeng.zhu@hand-china.com 2021-09-14
 **/
@Repository
@Slf4j
@EnableAspectJAutoProxy( proxyTargetClass = true , exposeProxy = true )
public class HzeroLovQueryRepositoryImpl implements HzeroLovQueryRepository {

    private LovAdapter lovAdapter;
    public HzeroLovQueryRepositoryImpl(LovAdapter lovAdapter) {
        this.lovAdapter = lovAdapter;
    }


    @Override
    public String queryLovValueMeaning(Long tenantId,
                                       String lovCode,
                                       String lovValue) {
        StringBuilder cacheKey = new StringBuilder(tenantId.toString()).append("_").append(lovCode).append("_").append(lovValue);
        HzeroLovQueryRepositoryImpl currentProxy = (HzeroLovQueryRepositoryImpl) AopContext.currentProxy();
        List<Map<String, Object>> lovList = currentProxy.queryLovValueMeaning(tenantId, lovCode, null,null,null, cacheKey.toString());
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


    @Cacheable(value = "O2_LOV", key = "#cacheKey")
    public List<Map<String, Object>> queryLovValueMeaning(Long tenantId,
                                                          String lovCode,
                                                          Integer page,
                                                          Integer size,
                                                          Map<String, String> queryLovValueMap,
                                                          String cacheKey) {
        return lovAdapter.queryLovData(lovCode, tenantId, null, page, size, queryLovValueMap);
    }
}
