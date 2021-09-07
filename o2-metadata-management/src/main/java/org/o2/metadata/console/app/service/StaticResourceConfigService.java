package org.o2.metadata.console.app.service;

import org.o2.metadata.console.api.dto.StaticResourceConfigDTO;
import org.o2.metadata.console.infra.entity.StaticResourceConfig;

import java.util.List;

/**
 * 静态资源配置应用服务
 *
 * @author wenjie.liu@hand-china.com 2021-09-06 10:47:44
 */
public interface StaticResourceConfigService {

    /**
     * 保存静态资源配置
     *
     * @param staticResourceConfig 静态资源配置对象
     * @return 静态资源配置对象
     */
    StaticResourceConfig save(StaticResourceConfig staticResourceConfig);

    /**
     * 列表展示静态资源配置
     *
     * @param staticResourceConfigDTO 静态资源配置入参
     * @return 静态资源配置对象集合
     */
    List<StaticResourceConfig> listStaticResourceConfig(StaticResourceConfigDTO staticResourceConfigDTO);
}
