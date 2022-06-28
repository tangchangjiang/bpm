package org.o2.metadata.console.infra.convertor;

import org.o2.metadata.console.api.vo.PosVO;
import org.o2.metadata.console.infra.entity.Pos;
import org.o2.metadata.console.infra.entity.PosAddress;
import org.o2.metadata.management.client.domain.co.PosCO;
import org.o2.metadata.management.client.domain.dto.PosAddressDTO;
import org.o2.metadata.management.client.domain.dto.PosDTO;

/**
 *
 * 服务点
 *
 * @author yipeng.zhu@hand-china.com 2021-08-04
 **/
public class PosConverter {
    private PosConverter() {
    }

    /**
     *  po 转 vo
     * @date 2021-08-05
     * @param pos 服务点
     * @return  vo
     */
    public static PosVO poToVoObject(Pos pos) {

        if (pos == null) {
            return null;
        }
        PosVO posVO = new PosVO();
        posVO.setPosId(pos.getPosId());
        posVO.setPosCode(pos.getPosCode());
        posVO.setPosName(pos.getPosName());
        posVO.setPosStatusCode(pos.getPosStatusCode());
        posVO.setPosTypeCode(pos.getPosTypeCode());
        posVO.setBusinessTypeCode(pos.getBusinessTypeCode());
        posVO.setOpenDate(pos.getOpenDate());
        posVO.setAddressId(pos.getAddressId());
        posVO.setBusinessTime(pos.getBusinessTime());
        posVO.setPickUpLimitQuantity(pos.getPickUpLimitQuantity());
        posVO.setNotice(pos.getNotice());
        posVO.setExpressLimitQuantity(pos.getExpressLimitQuantity());
        posVO.setPosStatusMeaning(pos.getPosStatusMeaning());
        posVO.setPosTypeMeaning(pos.getPosTypeMeaning());
        posVO.setBusinessTypeMeaning(pos.getBusinessTypeMeaning());
        posVO.setCarrierName(pos.getCarrierName());
        posVO.setCarrierId(pos.getCarrierId());
        posVO.setTenantId(pos.getTenantId());
        posVO.setRegionCode(pos.getRegionCode());
        posVO.setRegionName(pos.getRegionName());
        posVO.setCityCode(pos.getCityCode());
        posVO.setCityName(pos.getCityName());
        posVO.setDistrictCode(pos.getDistrictCode());
        posVO.setDistrictName(pos.getDistrictName());
        posVO.setStreetName(pos.getStreetName());
        posVO.setObjectVersionNumber(pos.getObjectVersionNumber());
        posVO.set_token(pos.get_token());
        posVO.setAddress(PosAddressConverter.poToVoObject(pos.getAddress()));
        posVO.setPostTimes(PostTimeConverter.toPostTimeVOList(pos.getPostTimes()));
        return posVO;
    }

    public static Pos dtoToPoObject(PosDTO posDTO) {
        if (null == posDTO) {
            return null;
        }
        Pos pos = new Pos();
        pos.setTenantId(posDTO.getTenantId());
        pos.setAddress(dtoToPoPosAddress(posDTO.getAddress()));
        pos.setBusinessTime(posDTO.getBusinessTime());
        pos.setAddressId(posDTO.getAddressId());
        pos.setPosName(posDTO.getPosName());
        pos.setPosCode(posDTO.getPosCode());
        pos.setOpenDate(posDTO.getOpenDate());
        pos.setPosStatusCode(posDTO.getPosStatusCode());
        pos.setPosTypeCode(posDTO.getPosTypeCode());
        return pos;
    }

    public static PosAddress dtoToPoPosAddress(PosAddressDTO posAddressDTO) {
        if (null == posAddressDTO) {
            return null;
        }
        PosAddress posAddress = new PosAddress();
        posAddress.setCityCode(posAddressDTO.getCityCode());
        posAddress.setCountryCode(posAddressDTO.getCountryCode());
        posAddress.setDistrictCode(posAddressDTO.getDistrictCode());
        posAddress.setLatitude(posAddressDTO.getLatitude());
        posAddress.setLongitude(posAddressDTO.getLongitude());
        posAddress.setStreetName(posAddressDTO.getStreetName());
        posAddress.setPhoneNumber(posAddressDTO.getPhoneNumber());
        posAddress.setRegionCode(posAddressDTO.getRegionCode());
        posAddress.setTenantId(posAddressDTO.getTenantId());
        return posAddress;
    }

    public static PosCO poToCoObject(Pos pos) {
        if (null == pos) {
            return null;
        }
        PosCO posCO = new PosCO();
        posCO.setTenantId(pos.getTenantId());
        posCO.setBusinessTime(pos.getBusinessTime());
        posCO.setAddressId(pos.getAddressId());
        posCO.setPosName(pos.getPosName());
        posCO.setPosCode(pos.getPosCode());
        posCO.setOpenDate(pos.getOpenDate());
        posCO.setPosStatusCode(pos.getPosStatusCode());
        posCO.setPosTypeCode(pos.getPosTypeCode());
        return posCO;
    }
}
