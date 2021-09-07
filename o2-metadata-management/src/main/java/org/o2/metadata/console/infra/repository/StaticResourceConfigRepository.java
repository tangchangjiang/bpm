package org.o2.metadata.console.infra.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.api.dto.StaticResourceConfigDTO;
import org.o2.metadata.console.infra.entity.StaticResourceConfig;

import java.util.List;

/**
 * 静态资源配置资源库
 *
 * @author wenjie.liu@hand-china.com 2021-09-06 10:47:44
 */
public interface StaticResourceConfigRepository extends BaseRepository<StaticResourceConfig> {

    /**
     * 列表展示静态资源配置
     *
     * @param staticResourceConfigDTO 静态资源配置入参
     * @return 静态资源配置对象集合
     */
    List<StaticResourceConfig> listStaticResourceConfig(StaticResourceConfigDTO staticResourceConfigDTO);

}
