package org.o2.metadata.infra.lovadapter.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.o2.metadata.infra.lovadapter.repository.HzeroLovQueryRepository;
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

    private final LovAdapter lovAdapter;
    public HzeroLovQueryRepositoryImpl(LovAdapter lovAdapter) {
        this.lovAdapter = lovAdapter;
    }


    @Override
    public List<LovValueDTO> queryLovValue(Long tenantId, String lovCode) {
        return lovAdapter.queryLovValue(lovCode, tenantId);
    }


    @Override
    public List<Map<String, Object>> queryLovValueMeaning(Long tenantId,
                                                          String lovCode,
                                                          Map<String, String> queryLovValueMap) {
        return lovAdapter.queryLovData(lovCode, tenantId, null, null, null, queryLovValueMap);
    }

}
