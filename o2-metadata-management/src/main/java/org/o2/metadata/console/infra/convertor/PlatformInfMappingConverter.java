package org.o2.metadata.console.infra.convertor;

import org.o2.metadata.console.api.vo.PlatformInfMappingVO;
import org.o2.metadata.console.infra.entity.PlatformInfoMapping;

/**
 * description 平台信息pojo转换器
 *
 * @author zhilin.ren@hand-china.com 2021/08/02 21:55
 */
public class PlatformInfMappingConverter {

    private PlatformInfMappingConverter() {
    }

    /**
     * po ->vo
     * @param platformInfoMapping 平台信息pojo转换器
     * @return  vo
     */
    public static PlatformInfMappingVO toPlatformInfMappingVO(PlatformInfoMapping platformInfoMapping) {
        if (platformInfoMapping == null) {
            return null;
        }
        PlatformInfMappingVO platformInfMappingVO = new PlatformInfMappingVO();
        platformInfMappingVO.setPlatformInfMappingId(platformInfoMapping.getPlatformInfMappingId());
        platformInfMappingVO.setInfTypeCode(platformInfoMapping.getInfTypeCode());
        platformInfMappingVO.setPlatformCode(platformInfoMapping.getPlatformCode());
        platformInfMappingVO.setInfCode(platformInfoMapping.getInfCode());
        platformInfMappingVO.setInfName(platformInfoMapping.getInfName());
        platformInfMappingVO.setPlatformInfCode(platformInfoMapping.getPlatformInfCode());
        platformInfMappingVO.setPlatformInfName(platformInfoMapping.getPlatformInfName());
        platformInfMappingVO.setTenantId(platformInfoMapping.getTenantId());
        platformInfMappingVO.setPlatformName(platformInfoMapping.getPlatformName());
        platformInfMappingVO.setInfTypeMeaning(platformInfoMapping.getInfTypeMeaning());
        return platformInfMappingVO;
    }
}
