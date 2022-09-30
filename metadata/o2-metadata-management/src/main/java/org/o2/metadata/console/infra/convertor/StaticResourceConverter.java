package org.o2.metadata.console.infra.convertor;

import org.o2.metadata.console.api.dto.StaticResourceSaveDTO;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.entity.StaticResource;

import java.util.Optional;

/**
 * 静态资源转换器
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/07/30 12:53
 */
public class StaticResourceConverter {

    private StaticResourceConverter() {

    }

    /**
     * staticResourceSaveDTO -> staticResource
     *
     * @param staticResourceSaveDTO staticResourceSaveDTO
     * @return staticResource
     */
    public static StaticResource toStaticResource(StaticResourceSaveDTO staticResourceSaveDTO) {
        if (staticResourceSaveDTO == null) {
            return null;
        }
        StaticResource staticResource = new StaticResource();
        staticResource.setResourceCode(staticResourceSaveDTO.getResourceCode());
        staticResource.setResourceUrl(staticResourceSaveDTO.getResourceUrl());
        staticResource.setResourceHost(staticResourceSaveDTO.getResourceHost());
        staticResource.setResourceLevel(staticResourceSaveDTO.getResourceLevel());
        staticResource.setResourceOwner(staticResourceSaveDTO.getResourceOwner());
        staticResource.setEnableFlag(Optional.ofNullable(staticResourceSaveDTO.getEnableFlag()).orElse(MetadataConstants.StaticResourceConstants.ENABLE_FLAG));
        staticResource.setDescription(staticResourceSaveDTO.getDescription());
        staticResource.setTenantId(staticResourceSaveDTO.getTenantId());
        staticResource.setLang(staticResourceSaveDTO.getLang());
        return staticResource;
    }
}
