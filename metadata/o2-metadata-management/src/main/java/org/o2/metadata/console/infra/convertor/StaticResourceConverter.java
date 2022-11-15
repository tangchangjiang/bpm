package org.o2.metadata.console.infra.convertor;

import org.o2.cms.management.client.domain.co.StaticResourceConfigCO;
import org.o2.metadata.domain.staticresource.domain.StaticResourceConfigDO;
import org.o2.metadata.domain.staticresource.domain.StaticResourceSaveDO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 静态资源转换器
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/07/30 12:53
 */
public class StaticResourceConverter {

    private StaticResourceConverter() {

    }

    public static org.o2.cms.management.client.domain.dto.StaticResourceSaveDTO toB2CStaticResourceSaveDTO(StaticResourceSaveDO staticResourceSaveDO) {

        if (staticResourceSaveDO == null) {
            return null;
        }
        org.o2.cms.management.client.domain.dto.StaticResourceSaveDTO staticResourceSaveDTO = new org.o2.cms.management.client.domain.dto.StaticResourceSaveDTO();
        staticResourceSaveDTO.setResourceCode(staticResourceSaveDO.getResourceCode());
        staticResourceSaveDTO.setResourceUrl(staticResourceSaveDO.getResourceUrl());
        staticResourceSaveDTO.setResourceHost(staticResourceSaveDO.getResourceHost());
        staticResourceSaveDTO.setResourceLevel(staticResourceSaveDO.getResourceLevel());
        staticResourceSaveDTO.setResourceOwner(staticResourceSaveDO.getResourceOwner());
        staticResourceSaveDTO.setEnableFlag(staticResourceSaveDO.getEnableFlag());
        staticResourceSaveDTO.setDescription(staticResourceSaveDO.getDescription());
        staticResourceSaveDTO.setLang(staticResourceSaveDO.getLang());
        staticResourceSaveDTO.setTenantId(staticResourceSaveDO.getTenantId());
        return staticResourceSaveDTO;
    }

    public static List<org.o2.cms.management.client.domain.dto.StaticResourceSaveDTO> toB2CStaticResourceSaveDTOList(List<StaticResourceSaveDO> staticResourceSaveDOList) {

        if (staticResourceSaveDOList == null) {
            return Collections.emptyList();
        }
        List<org.o2.cms.management.client.domain.dto.StaticResourceSaveDTO> staticResourceSaveDTOList = new ArrayList<>();
        for (StaticResourceSaveDO staticResourceSaveDO : staticResourceSaveDOList) {
            staticResourceSaveDTOList.add(toB2CStaticResourceSaveDTO(staticResourceSaveDO));
        }
        return staticResourceSaveDTOList;
    }

    public static StaticResourceConfigDO toStaticResourceConfigDO(StaticResourceConfigCO staticResourceConfigCO) {

        if (staticResourceConfigCO == null) {
            return null;
        }
        StaticResourceConfigDO staticResourceConfigDO = new StaticResourceConfigDO();
        staticResourceConfigDO.setResourceCode(staticResourceConfigCO.getResourceCode());
        staticResourceConfigDO.setResourceLevel(staticResourceConfigCO.getResourceLevel());
        staticResourceConfigDO.setJsonKey(staticResourceConfigCO.getJsonKey());
        staticResourceConfigDO.setTenantId(staticResourceConfigCO.getTenantId());
        staticResourceConfigDO.setDescription(staticResourceConfigCO.getDescription());
        staticResourceConfigDO.setDifferentLangFlag(staticResourceConfigCO.getDifferentLangFlag());
        staticResourceConfigDO.setUploadFolder(staticResourceConfigCO.getUploadFolder());
        staticResourceConfigDO.setSourceModuleCode(staticResourceConfigCO.getSourceModuleCode());
        staticResourceConfigDO.setSourceProgram(staticResourceConfigCO.getSourceProgram());
        staticResourceConfigDO.setResourcePathList(staticResourceConfigCO.getResourcePathList());
        return staticResourceConfigDO;
    }

    public static org.o2.cms.management.client.b2b.domain.dto.StaticResourceSaveDTO toB2BStaticResourceSaveDTO(StaticResourceSaveDO staticResourceSaveDO) {

        if (staticResourceSaveDO == null) {
            return null;
        }
        org.o2.cms.management.client.b2b.domain.dto.StaticResourceSaveDTO staticResourceSaveDTO = new org.o2.cms.management.client.b2b.domain.dto.StaticResourceSaveDTO();
        staticResourceSaveDTO.setResourceCode(staticResourceSaveDO.getResourceCode());
        staticResourceSaveDTO.setResourceUrl(staticResourceSaveDO.getResourceUrl());
        staticResourceSaveDTO.setResourceHost(staticResourceSaveDO.getResourceHost());
        staticResourceSaveDTO.setResourceLevel(staticResourceSaveDO.getResourceLevel());
        staticResourceSaveDTO.setResourceOwner(staticResourceSaveDO.getResourceOwner());
        staticResourceSaveDTO.setEnableFlag(staticResourceSaveDO.getEnableFlag());
        staticResourceSaveDTO.setDescription(staticResourceSaveDO.getDescription());
        staticResourceSaveDTO.setLang(staticResourceSaveDO.getLang());
        staticResourceSaveDTO.setTenantId(staticResourceSaveDO.getTenantId());
        return staticResourceSaveDTO;
    }

    public static List<org.o2.cms.management.client.b2b.domain.dto.StaticResourceSaveDTO> toB2BStaticResourceSaveDTOList(List<StaticResourceSaveDO> staticResourceSaveDOList) {

        if (staticResourceSaveDOList == null) {
            return Collections.emptyList();
        }
        List<org.o2.cms.management.client.b2b.domain.dto.StaticResourceSaveDTO> staticResourceSaveDTOList = new ArrayList<>();
        for (StaticResourceSaveDO staticResourceSaveDO : staticResourceSaveDOList) {
            staticResourceSaveDTOList.add(toB2BStaticResourceSaveDTO(staticResourceSaveDO));
        }
        return staticResourceSaveDTOList;
    }

    public static StaticResourceConfigDO toStaticResourceConfigDO(org.o2.cms.management.client.b2b.domain.co.StaticResourceConfigCO staticResourceConfigCO) {

        if (staticResourceConfigCO == null) {
            return null;
        }
        StaticResourceConfigDO staticResourceConfigDO = new StaticResourceConfigDO();
        staticResourceConfigDO.setResourceCode(staticResourceConfigCO.getResourceCode());
        staticResourceConfigDO.setResourceLevel(staticResourceConfigCO.getResourceLevel());
        staticResourceConfigDO.setJsonKey(staticResourceConfigCO.getJsonKey());
        staticResourceConfigDO.setTenantId(staticResourceConfigCO.getTenantId());
        staticResourceConfigDO.setDescription(staticResourceConfigCO.getDescription());
        staticResourceConfigDO.setDifferentLangFlag(staticResourceConfigCO.getDifferentLangFlag());
        staticResourceConfigDO.setUploadFolder(staticResourceConfigCO.getUploadFolder());
        staticResourceConfigDO.setSourceModuleCode(staticResourceConfigCO.getSourceModuleCode());
        staticResourceConfigDO.setSourceProgram(staticResourceConfigCO.getSourceProgram());
        staticResourceConfigDO.setResourcePathList(staticResourceConfigCO.getResourcePathList());
        return staticResourceConfigDO;
    }

}
