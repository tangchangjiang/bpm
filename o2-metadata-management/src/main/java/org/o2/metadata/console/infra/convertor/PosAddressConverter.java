package org.o2.metadata.console.infra.convertor;

import org.o2.metadata.console.api.co.PosAddressCO;
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
    private PosAddressConverter() {
    }

    /**
     * po 转 vo
     * @date 2021-08-05
     * @param posAddress 服务点地址
     * @return  vo
     */
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

    /**
     * po 转 CO
     * @date 2021-08-05
     * @param posAddress 服务点地址
     * @return  vo
     */
    private static PosAddressCO poToCoObject(PosAddress posAddress) {


        if (posAddress == null) {
            return null;
        }
        PosAddressCO co = new PosAddressCO();
        co.setPosAddressId(posAddress.getPosAddressId());
        co.setCountryCode(posAddress.getCountryCode());
        co.setRegionCode(posAddress.getRegionCode());
        co.setCityCode(posAddress.getCityCode());
        co.setDistrictCode(posAddress.getDistrictCode());
        co.setStreetName(posAddress.getStreetName());
        co.setPhoneNumber(posAddress.getPhoneNumber());
        co.setPostcode(posAddress.getPostcode());
        co.setContact(posAddress.getContact());
        co.setMobilePhone(posAddress.getMobilePhone());
        co.setLongitude(posAddress.getLongitude());
        co.setLatitude(posAddress.getLatitude());
        co.setTenantId(posAddress.getTenantId());
        co.setDistrict(posAddress.getDistrictName());
        co.setCity(posAddress.getCityName());
        co.setCountry(posAddress.getCountryName());
        co.setRegion(posAddress.getRegionName());
        co.setPosCode(posAddress.getPosCode());
        return co;
    }

    /**
     * PO 转 CO
     * @param posAddressList 地址
     * @return  list
     */
    public static List<PosAddressCO> poToCoListObjects(List<PosAddress> posAddressList) {
        List<PosAddressCO> cos = new ArrayList<>();
        if (posAddressList == null) {
            return cos;
        }
        for (PosAddress posAddress : posAddressList) {
            cos.add(poToCoObject(posAddress));
        }
        return cos;
    }
}
