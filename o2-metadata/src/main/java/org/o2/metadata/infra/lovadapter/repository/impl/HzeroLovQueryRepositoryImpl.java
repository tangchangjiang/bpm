package org.o2.metadata.infra.lovadapter.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.o2.metadata.infra.lovadapter.repository.HzeroLovQueryRepository;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Repository;

import java.util.List;
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
    public List<LovValueDTO> queryLovValue(Long tenantId, String lovCode) {
        return lovAdapter.queryLovValue(lovCode, tenantId);
    }
}
