package org.o2.metadata.console.infra.mapper;

import org.o2.metadata.console.api.dto.StaticResourceConfigDTO;
import org.o2.metadata.console.api.vo.StaticResourceConfigVO;
import org.o2.metadata.console.infra.entity.StaticResourceConfig;
import io.choerodon.mybatis.common.BaseMapper;

import java.util.List;

/**
 * 静态资源配置Mapper
 *
 * @author wenjie.liu@hand-china.com 2021-09-06 10:47:44
 */
public interface StaticResourceConfigMapper extends BaseMapper<StaticResourceConfig> {

    /**
     * 列表展示静态资源配置
     *
     * @param staticResourceConfigDTO 静态资源配置入参
     * @return 静态资源配置对象集合
     */
    List<StaticResourceConfigVO> listStaticResourceConfig(StaticResourceConfigDTO staticResourceConfigDTO);

}
