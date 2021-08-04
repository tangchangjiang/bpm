package org.o2.metadata.console.infra.convertor;

import org.o2.metadata.console.api.vo.PosAddressVO;
import org.o2.metadata.console.infra.entity.PosAddress;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 服务点地址
 *
 * @author yipeng.zhu@hand-china.com 2021-08-02
 **/
public class PosAddressConverter {

    public static PosAddressVO poToVoObject(PosAddress posAddress) {

        if (posAddress == null) {
            return null;
        }
        PosAddressVO posAddressVO = new PosAddressVO();
        posAddressVO.setPosAddressId(posAddress.getPosAddressId());
        posAddressVO.setCountryCode(posAddress.getCountryCode());
        posAddressVO.setRegionCode(posAddress.getRegionCode());
        posAddressVO.setCityCode(posAddress.getCityCode());
        posAddressVO.setDistrictCode(posAddress.getDistrictCode());
        posAddressVO.setStreetName(posAddress.getStreetName());
        posAddressVO.setPhoneNumber(posAddress.getPhoneNumber());
        posAddressVO.setPostcode(posAddress.getPostcode());
        posAddressVO.setContact(posAddress.getContact());
        posAddressVO.setMobilePhone(posAddress.getMobilePhone());
        posAddressVO.setLongitude(posAddress.getLongitude());
        posAddressVO.setLatitude(posAddress.getLatitude());
        posAddressVO.setCountryName(posAddress.getCountryName());
        posAddressVO.setRegionName(posAddress.getRegionName());
        posAddressVO.setCityName(posAddress.getCityName());
        posAddressVO.setDistrictName(posAddress.getDistrictName());
        posAddressVO.setTenantId(posAddress.getTenantId());
        posAddressVO.setCreationDate(posAddress.getCreationDate());
        posAddressVO.setCreatedBy(posAddress.getCreatedBy());
        posAddressVO.setLastUpdateDate(posAddress.getLastUpdateDate());
        posAddressVO.setLastUpdatedBy(posAddress.getLastUpdatedBy());
        posAddressVO.setObjectVersionNumber(posAddress.getObjectVersionNumber());
        posAddressVO.set_token(posAddress.get_token());
        return posAddressVO;
    }

    /**
     * PO 转 VO
     * @param posAddressList 地址
     * @return  list
     */
    public static List<PosAddressVO> poToVoListObjects(List<PosAddress> posAddressList) {
        List<PosAddressVO> posAddressVOList = new ArrayList<>();
        if (posAddressList == null) {
            return posAddressVOList;
        }
        for (PosAddress posAddress : posAddressList) {
            posAddressVOList.add(poToVoObject(posAddress));
        }
        return posAddressVOList;
    }
}
