package org.o2.metadata.console.infra.mapper;

import org.o2.metadata.console.infra.entity.Platform;
import io.choerodon.mybatis.common.BaseMapper;

import java.util.List;

/**
 * 平台定义表Mapper
 *
 * @author zhilin.ren@hand-china.com 2021-08-02 11:11:28
 */
public interface PlatformMapper extends BaseMapper<Platform> {
    List<Platform> listPlatform(Platform platform);
}
