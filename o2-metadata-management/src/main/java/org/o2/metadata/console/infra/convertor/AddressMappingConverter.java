package org.o2.metadata.console.infra.convertor;

import org.o2.metadata.console.api.vo.AddressMappingVO;
import org.o2.metadata.console.infra.entity.AddressMapping;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 地址匹配
 *
 * @author yipeng.zhu@hand-china.com 2021-08-05
 **/
public class AddressMappingConverter {
    private AddressMappingConverter() {
    }
    /**
     * po -> vo
     * @param addressMapping 地址匹配
     * @return  vo
     */
    public static AddressMappingVO poToVoObject(AddressMapping addressMapping) {

        if (addressMapping == null) {
            return null;
        }
        AddressMappingVO addressMappingVO = new AddressMappingVO();
        addressMappingVO.setAddressMappingId(addressMapping.getAddressMappingId());
        addressMappingVO.setRegionCode(addressMapping.getRegionCode());
        addressMappingVO.setAddressTypeCode(addressMapping.getAddressTypeCode());
        addressMappingVO.setExternalCode(addressMapping.getExternalCode());
        addressMappingVO.setExternalName(addressMapping.getExternalName());
        addressMappingVO.setActiveFlag(addressMapping.getActiveFlag());
        addressMappingVO.setTenantId(addressMapping.getTenantId());
        addressMappingVO.setRegionName(addressMapping.getRegionName());
        addressMappingVO.setPlatformTypeMeaning(addressMapping.getPlatformTypeMeaning());
        addressMappingVO.setAddressTypeMeaning(addressMapping.getAddressTypeMeaning());
        addressMappingVO.setRegionPathIds(addressMapping.getRegionPathIds());
        addressMappingVO.setRegionPathCodes(addressMapping.getRegionPathCodes());
        addressMappingVO.setRegionPathNames(addressMapping.getRegionPathNames());
        addressMappingVO.setPlatformCode(addressMapping.getPlatformCode());
        addressMappingVO.setPlatformName(addressMapping.getPlatformName());
        addressMappingVO.setCreationDate(addressMapping.getCreationDate());
        addressMappingVO.setCreatedBy(addressMapping.getCreatedBy());
        addressMappingVO.setLastUpdateDate(addressMapping.getLastUpdateDate());
        addressMappingVO.setLastUpdatedBy(addressMapping.getLastUpdatedBy());
        addressMappingVO.setObjectVersionNumber(addressMapping.getObjectVersionNumber());
        addressMappingVO.set_token(addressMapping.get_token());
        return addressMappingVO;
    }

    /**
     * PO 转 VO
     * @param addressMappings 地址匹配
     * @return  list
     */
    public static List<AddressMappingVO> poToVoListObjects(List<AddressMapping> addressMappings) {
        List<AddressMappingVO> addressMappingVOList = new ArrayList<>();
        if (addressMappings == null) {
            return addressMappingVOList;
        }
        for (AddressMapping addressMapping : addressMappings) {
            addressMappingVOList.add(poToVoObject(addressMapping));
        }
        return addressMappingVOList;
    }


}
