package org.o2.metadata.console.infra.convertor;

import org.o2.metadata.console.api.vo.PlatformInfMappingVO;
import org.o2.metadata.console.infra.entity.PlatformInfMapping;

/**
 * description 平台信息pojo转换器
 *
 * @author zhilin.ren@hand-china.com 2021/08/02 21:55
 */
public class PlatformInfMappingConvertor {
    public static PlatformInfMappingVO toPlatformInfMappingVO(PlatformInfMapping platformInfMapping) {
        if (platformInfMapping == null) {
            return null;
        }
        PlatformInfMappingVO platformInfMappingVO = new PlatformInfMappingVO();
        platformInfMappingVO.setPlatformInfMappingId(platformInfMapping.getPlatformInfMappingId());
        platformInfMappingVO.setInfTypeCode(platformInfMapping.getInfTypeCode());
        platformInfMappingVO.setPlatformCode(platformInfMapping.getPlatformCode());
        platformInfMappingVO.setInfCode(platformInfMapping.getInfCode());
        platformInfMappingVO.setInfName(platformInfMapping.getInfName());
        platformInfMappingVO.setPlatformInfCode(platformInfMapping.getPlatformInfCode());
        platformInfMappingVO.setPlatformInfName(platformInfMapping.getPlatformInfName());
        platformInfMappingVO.setTenantId(platformInfMapping.getTenantId());
        platformInfMappingVO.setPlatformName(platformInfMapping.getPlatformName());
        platformInfMappingVO.setInfTypeMeaning(platformInfMapping.getInfTypeMeaning());
        return platformInfMappingVO;
    }
}
