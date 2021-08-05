package org.o2.metadata.infra.convertor;

import org.o2.metadata.api.vo.CarrierVO;
import org.o2.metadata.domain.carrier.domain.CarrierDO;
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
    /**
     * po->do
     * @date 2021-08-05
     * @param carrier 承运商
     * @return  do
     */
    private static CarrierDO poToDoObject(Carrier carrier) {

        if (carrier == null) {
            return null;
        }
        CarrierDO carrierDO = new CarrierDO();
        carrierDO.setCarrierId(carrier.getCarrierId());
        carrierDO.setCarrierCode(carrier.getCarrierCode());
        carrierDO.setCarrierName(carrier.getCarrierName());
        carrierDO.setCarrierTypeCode(carrier.getCarrierTypeCode());
        carrierDO.setActiveFlag(carrier.getActiveFlag());
        carrierDO.setTenantId(carrier.getTenantId());
        return carrierDO;
    }

    /**
     * Do->Vo
     * @date 2021-08-05
     * @param carrier 承运商
     * @return  Vo
     */
    private static CarrierVO doToVoObject(CarrierDO carrier) {

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
     * PO 转 DO
     * @param carrierList 系统参数集合
     * @return  list
     */
    public static List<CarrierDO> poToDoListObjects(List<Carrier> carrierList) {
        List<CarrierDO> carrierDOList = new ArrayList<>();
        if (carrierList == null) {
            return carrierDOList;
        }
        for (Carrier carrier : carrierList) {
            carrierDOList.add(poToDoObject(carrier));
        }
        return carrierDOList;
    }

    /**
     * DO 转 VO
     * @param carrierList 系统参数集合
     * @return  list
     */
    public static List<CarrierVO> doToVoListObjects(List<CarrierDO> carrierList) {
        List<CarrierVO> carrierVOList = new ArrayList<>();
        if (carrierList == null) {
            return carrierVOList;
        }
        for (CarrierDO carrier : carrierList) {
            carrierVOList.add(doToVoObject(carrier));
        }
        return carrierVOList;
    }
}
