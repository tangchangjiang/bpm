package org.o2.metadata.console.infra.repository.impl;

import lombok.RequiredArgsConstructor;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.console.api.dto.PlatformInfMappingDTO;
import org.o2.metadata.console.infra.entity.PlatformInfMapping;
import org.o2.metadata.console.infra.mapper.PlatformInfMappingMapper;
import org.o2.metadata.console.infra.repository.PlatformInfMappingRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 平台信息匹配表 资源库实现
 *
 * @author zhilin.ren@hand-china.com 2021-08-02 11:11:28
 */
@Component
@RequiredArgsConstructor
public class PlatformInfMappingRepositoryImpl extends BaseRepositoryImpl<PlatformInfMapping> implements PlatformInfMappingRepository {

    private final PlatformInfMappingMapper platformInfMappingMapper;
    @Override
    public List<PlatformInfMapping> listInfMapping(PlatformInfMapping platformInfMapping) {
        return platformInfMappingMapper.listInfMapping(platformInfMapping);
    }

    @Override
    public PlatformInfMapping selectById(Long id) {
        return platformInfMappingMapper.selectById(id);
    }

    @Override
    public PlatformInfMapping selectOneMapping(PlatformInfMappingDTO platformInfMapping) {
        return platformInfMappingMapper.selectOneMapping(platformInfMapping);
    }
}
