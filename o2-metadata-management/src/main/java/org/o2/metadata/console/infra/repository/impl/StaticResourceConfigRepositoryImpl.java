package org.o2.metadata.console.infra.repository.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.console.api.dto.StaticResourceConfigDTO;
import org.o2.metadata.console.infra.entity.StaticResourceConfig;
import org.o2.metadata.console.infra.mapper.StaticResourceConfigMapper;
import org.o2.metadata.console.infra.repository.StaticResourceConfigRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 静态资源配置 资源库实现
 *
 * @author wenjie.liu@hand-china.com 2021-09-06 10:47:44
 */
@Component
public class StaticResourceConfigRepositoryImpl extends BaseRepositoryImpl<StaticResourceConfig> implements StaticResourceConfigRepository {

    private final StaticResourceConfigMapper staticResourceConfigMapper;

    public StaticResourceConfigRepositoryImpl(final StaticResourceConfigMapper staticResourceConfigMapper) {
        this.staticResourceConfigMapper = staticResourceConfigMapper;
    }

    @Override
    public List<StaticResourceConfig> listStaticResourceConfig(StaticResourceConfigDTO staticResourceConfigDTO) {
        if (StringUtils.isNotBlank(staticResourceConfigDTO.getResourceCode())) {
            String resouceCode = staticResourceConfigDTO.getResourceCode();
            staticResourceConfigDTO.setResourceCode(resouceCode.toUpperCase());
        }
        return staticResourceConfigMapper.listStaticResourceConfig(staticResourceConfigDTO);
    }
}
