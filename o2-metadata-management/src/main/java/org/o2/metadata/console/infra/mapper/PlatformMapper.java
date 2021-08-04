package org.o2.metadata.console.infra.mapper;

import org.o2.metadata.console.api.dto.PlatformDTO;
import org.o2.metadata.console.infra.entity.Platform;
import io.choerodon.mybatis.common.BaseMapper;

import java.util.List;

/**
 * 平台定义表Mapper
 *
 * @author zhilin.ren@hand-china.com 2021-08-02 11:11:28
 */
public interface PlatformMapper extends BaseMapper<Platform> {
    /**
     * 平台查询
     * @param platform 查询条件
     * @return List<Platform> 结果
     */
    List<Platform> listPlatform(PlatformDTO platform);
}
