package org.o2.metadata.console.infra.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.api.dto.PlatformDTO;
import org.o2.metadata.console.infra.entity.Platform;

import java.util.List;

/**
 * 平台定义表资源库
 *
 * @author zhilin.ren@hand-china.com 2021-08-02 11:11:28
 */
public interface PlatformRepository extends BaseRepository<Platform> {
    /**
     * 根据条件查询平台定义信息
     * @param platform 查询条件
     * @return  List<Platform> 查询结果
     */
    List<Platform> listPlatform(PlatformDTO platform);
}
