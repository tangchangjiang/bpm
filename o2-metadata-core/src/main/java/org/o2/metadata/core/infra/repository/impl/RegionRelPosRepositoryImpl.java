package org.o2.metadata.core.infra.repository.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.core.domain.entity.RegionRelPos;
import org.o2.metadata.core.domain.repository.RegionRelPosRepository;
import org.o2.metadata.core.infra.mapper.RegionRelPosMapper;
import org.springframework.stereotype.Component;

/**
 * 默认服务点配置 资源库实现
 *
 * @author wei.cai@hand-china.com 2020-01-09 15:41:36
 */
@Component
public class RegionRelPosRepositoryImpl extends BaseRepositoryImpl<RegionRelPos> implements RegionRelPosRepository {

    private RegionRelPosMapper regionRelPosMapper;

    public RegionRelPosRepositoryImpl(RegionRelPosMapper regionRelPosMapper) {
        this.regionRelPosMapper = regionRelPosMapper;
    }

    @Override
    public Page<RegionRelPos> listByCondition(PageRequest pageRequest, RegionRelPos regionRelPos) {
        return PageHelper.doPage(pageRequest,() -> regionRelPosMapper.selectByStoreId(regionRelPos));
    }
}
