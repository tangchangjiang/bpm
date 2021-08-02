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
public class PosAddressConvertor {

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
        posAddressVO.setCountry(posAddress.getCountry());
        posAddressVO.setRegion(posAddress.getRegion());
        posAddressVO.setCity(posAddress.getCity());
        posAddressVO.setDistrict(posAddress.getDistrict());
        posAddressVO.setTenantId(posAddress.getTenantId());
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
