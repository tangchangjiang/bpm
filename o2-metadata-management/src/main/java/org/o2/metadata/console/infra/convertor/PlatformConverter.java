package org.o2.metadata.console.infra.convertor;

import org.o2.metadata.console.api.co.PlatformInfoMappingCO;
import org.o2.metadata.console.api.vo.OnlineShopVO;
import org.o2.metadata.console.api.vo.PlatformInfMappingVO;
import org.o2.metadata.console.infra.entity.OnlineShop;
import org.o2.metadata.console.infra.entity.PlatformInfoMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * description 平台信息pojo转换器
 *
 * @author zhilin.ren@hand-china.com 2021/08/02 21:55
 */
public class PlatformConverter {

    private PlatformConverter() {
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

    /**
     * po->co
     * @param platformInfoMapping po
     * @return  co
     */
    private static PlatformInfoMappingCO poToCoObject(PlatformInfoMapping platformInfoMapping) {
        if (platformInfoMapping == null) {
            return null;
        }
        PlatformInfoMappingCO co = new PlatformInfoMappingCO();
        co.setPlatformInfMappingId(platformInfoMapping.getPlatformInfMappingId());
        co.setInfTypeCode(platformInfoMapping.getInfTypeCode());
        co.setInfCode(platformInfoMapping.getInfCode());
        co.setInfName(platformInfoMapping.getInfName());
        co.setPlatformInfCode(platformInfoMapping.getPlatformInfCode());
        co.setPlatformInfName(platformInfoMapping.getPlatformInfName());
        co.setTenantId(platformInfoMapping.getTenantId());
        return co;
    }

    /**
     * PO 转 cO
     * @param platformInfoMappings 平台信息pojo转换器
     * @return  list
     */
    public static List<PlatformInfoMappingCO> poToCoListObjects(List<PlatformInfoMapping> platformInfoMappings) {
        List<PlatformInfoMappingCO> cos = new ArrayList<>();
        if (platformInfoMappings == null) {
            return cos;
        }
        for (PlatformInfoMapping platformInfoMapping : platformInfoMappings) {
            cos.add(poToCoObject(platformInfoMapping));
        }
        return cos;
    }
}
