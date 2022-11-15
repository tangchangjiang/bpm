package org.o2.metadata.console.infra.repository.impl;

import lombok.RequiredArgsConstructor;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.console.api.dto.PlatformDTO;
import org.o2.metadata.console.infra.entity.Platform;
import org.o2.metadata.console.infra.mapper.PlatformMapper;
import org.o2.metadata.console.infra.repository.PlatformRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 平台定义表 资源库实现
 *
 * @author zhilin.ren@hand-china.com 2021-08-02 11:11:28
 */
@Component
@RequiredArgsConstructor
public class PlatformRepositoryImpl extends BaseRepositoryImpl<Platform> implements PlatformRepository {

    private final PlatformMapper platformMapper;

    @Override
    public List<Platform> listPlatform(PlatformDTO platform) {
        return platformMapper.listPlatform(platform);
    }
}
