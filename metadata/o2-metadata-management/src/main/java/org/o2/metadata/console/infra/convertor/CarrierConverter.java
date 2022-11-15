package org.o2.metadata.console.infra.convertor;

import lombok.Data;
import org.o2.metadata.console.api.co.CarrierMappingCO;
import org.o2.metadata.console.infra.entity.CarrierMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * 承运商
 *
 * @author yipeng.zhu@hand-china.com 2021-08-18
 **/
@Data
public class CarrierConverter {
    private CarrierConverter() {
    }

    /**
     * PO 转 CO
     *
     * @param carrierMapping 匹配规则
     * @return co
     */
    private static CarrierMappingCO poToCoObject(CarrierMapping carrierMapping) {

        if (carrierMapping == null) {
            return null;
        }
        CarrierMappingCO co = new CarrierMappingCO();
        co.setPlatformCarrierCode(carrierMapping.getPlatformCarrierCode());
        co.setPlatformCarrierName(carrierMapping.getPlatformCarrierName());
        co.setPlatformCode(carrierMapping.getPlatformCode());
        co.setPlatformName(carrierMapping.getPlatformName());
        co.setCarrierCode(carrierMapping.getCarrierCode());
        co.setCarrierName(carrierMapping.getCarrierName());
        return co;
    }

    /**
     * PO 转 CO
     *
     * @param carrierMappings 匹配规则
     * @return list
     */
    public static List<CarrierMappingCO> poToCoListObjects(List<CarrierMapping> carrierMappings) {
        List<CarrierMappingCO> cos = new ArrayList<>();
        if (carrierMappings == null) {
            return cos;
        }
        for (CarrierMapping carrierMapping : carrierMappings) {
            cos.add(poToCoObject(carrierMapping));
        }
        return cos;
    }
}
