package org.o2.metadata.infra.convertor;

import org.o2.metadata.api.vo.CarrierVO;
import org.o2.metadata.infra.entity.Carrier;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 承运商
 *
 * @author yipeng.zhu@hand-china.com 2021-08-05
 **/
public class CarrierConverter {
    public static CarrierVO poToVoObject(Carrier carrier) {

        if (carrier == null) {
            return null;
        }
        CarrierVO carrierVO = new CarrierVO();
        carrierVO.setCarrierCode(carrier.getCarrierCode());
        carrierVO.setCarrierName(carrier.getCarrierName());
        carrierVO.setCarrierTypeCode(carrier.getCarrierTypeCode());
        return carrierVO;
    }

    /**
     * PO 转 VO
     * @param carrierList 系统参数集合
     * @return  list
     */
    public static List<CarrierVO> poToVoListObjects(List<Carrier> carrierList) {
        List<CarrierVO> carrierVOList = new ArrayList<>();
        if (carrierList == null) {
            return carrierVOList;
        }
        for (Carrier carrier : carrierList) {
            carrierVOList.add(poToVoObject(carrier));
        }
        return carrierVOList;
    }
}
