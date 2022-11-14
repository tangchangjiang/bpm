package org.o2.metadata.infra.convertor;

import org.o2.metadata.api.co.CarrierCO;
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

    private CarrierConverter() { }

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
    private static CarrierCO doToVoObject(CarrierDO carrier) {

        if (carrier == null) {
            return null;
        }
        CarrierCO co = new CarrierCO();
        co.setCarrierCode(carrier.getCarrierCode());
        co.setCarrierName(carrier.getCarrierName());
        co.setCarrierTypeCode(carrier.getCarrierTypeCode());
        return co;
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
     * DO 转 CO
     * @param carrierList 系统参数集合
     * @return  list
     */
    public static List<CarrierCO> doToCoListObjects(List<CarrierDO> carrierList) {
        List<CarrierCO> cos = new ArrayList<>();
        if (carrierList == null) {
            return cos;
        }
        for (CarrierDO carrier : carrierList) {
            cos.add(doToVoObject(carrier));
        }
        return cos;
    }
}
