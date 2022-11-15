package org.o2.metadata.console.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.console.infra.entity.RegionArea;
import org.o2.metadata.console.infra.mapper.RegionAreaMapper;
import org.o2.metadata.console.infra.repository.RegionAreaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 大区定义 资源库实现
 *
 * @author houlin.cheng@hand-china.com 2020-08-07 16:51:37
 */
@Component
public class RegionAreaRepositoryImpl extends BaseRepositoryImpl<RegionArea> implements RegionAreaRepository {
    private final RegionAreaMapper regionAreaMapper;

    public RegionAreaRepositoryImpl(RegionAreaMapper regionAreaMapper) {
        this.regionAreaMapper = regionAreaMapper;
    }

    @Override
    public List<RegionArea> batchSelectByCode(List<String> regionCodes, Long tenantId) {
        return regionAreaMapper.batchSelectByCode(regionCodes, tenantId);
    }
}
