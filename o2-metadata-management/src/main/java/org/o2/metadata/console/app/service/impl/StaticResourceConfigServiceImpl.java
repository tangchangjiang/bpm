package org.o2.metadata.console.app.service.impl;

import org.o2.metadata.console.api.dto.StaticResourceConfigDTO;
import org.o2.metadata.console.infra.entity.StaticResourceConfig;
import org.o2.metadata.console.infra.repository.StaticResourceConfigRepository;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;
import org.o2.metadata.console.app.service.StaticResourceConfigService;
import java.util.List;

import org.hzero.mybatis.helper.UniqueHelper;

/**
 * 静态资源配置应用服务默认实现
 *
 * @author wenjie.liu@hand-china.com 2021-09-06 10:47:44
 */
@Service
public class StaticResourceConfigServiceImpl implements StaticResourceConfigService {

    private final StaticResourceConfigRepository staticResourceConfigRepository;

    public StaticResourceConfigServiceImpl(final StaticResourceConfigRepository staticResourceConfigRepository){
        this.staticResourceConfigRepository = staticResourceConfigRepository;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public StaticResourceConfig save(StaticResourceConfig staticResourceConfig) {
        //保存静态资源配置
        UniqueHelper.valid(staticResourceConfig,StaticResourceConfig.O2MD_STATIC_RESOURCE_CONFIG_U1);
        if (staticResourceConfig.getResourceConfigId() == null) {
            staticResourceConfigRepository.insertSelective(staticResourceConfig);
        } else {
            staticResourceConfigRepository.updateOptional(staticResourceConfig,
                    StaticResourceConfig.FIELD_RESOURCE_LEVEL,
                    StaticResourceConfig.FIELD_DESCRIPTION
            );
        }

        return staticResourceConfig;
    }

    @Override
    public List<StaticResourceConfig> listStaticResourceConfig(StaticResourceConfigDTO staticResourceConfigDTO) {
        return staticResourceConfigRepository.listStaticResourceConfig(staticResourceConfigDTO);
    }
}
