package org.o2.metadata.console.infra.convertor;

import org.o2.metadata.console.api.vo.PosVO;
import org.o2.metadata.console.infra.entity.Pos;

/**
 *
 * 服务点
 *
 * @author yipeng.zhu@hand-china.com 2021-08-04
 **/
public class PosConverter {
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
}
