package org.o2.metadata.console.infra.repository.impl;

import lombok.RequiredArgsConstructor;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.console.api.dto.InfMappingDTO;
import org.o2.metadata.console.api.dto.PlatformInfMappingDTO;
import org.o2.metadata.console.api.dto.PlatformQueryInnerDTO;
import org.o2.metadata.console.infra.entity.PlatformInfoMapping;
import org.o2.metadata.console.infra.mapper.PlatformInfoMappingMapper;
import org.o2.metadata.console.infra.repository.PlatformInfoMappingRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 平台信息匹配表 资源库实现
 *
 * @author zhilin.ren@hand-china.com 2021-08-02 11:11:28
 */
@Component
@RequiredArgsConstructor
public class PlatformInfoMappingRepositoryImpl extends BaseRepositoryImpl<PlatformInfoMapping> implements PlatformInfoMappingRepository {

    private final PlatformInfoMappingMapper platformInfoMappingMapper;
    @Override
    public List<PlatformInfoMapping> listInfMapping(InfMappingDTO platformInfMapping) {
        return platformInfoMappingMapper.listInfMapping(platformInfMapping);
    }

    @Override
    public PlatformInfoMapping selectById(Long id) {
        return platformInfoMappingMapper.selectById(id);
    }

    @Override
    public PlatformInfoMapping selectOneMapping(PlatformInfMappingDTO platformInfMapping) {
        return platformInfoMappingMapper.selectOneMapping(platformInfMapping);
    }

    @Override
    public List<PlatformInfoMapping> selectCondition(PlatformQueryInnerDTO queryInnerDTO) {
        return platformInfoMappingMapper.selectCondition(queryInnerDTO);
    }
}
