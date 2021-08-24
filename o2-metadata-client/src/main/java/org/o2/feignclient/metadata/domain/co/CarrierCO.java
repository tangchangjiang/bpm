package org.o2.feignclient.metadata.domain.co;

import lombok.Data;

/**
 *
 * 承运商
 *
 * @author yipeng.zhu@hand-china.com 2021-08-05
 **/
@Data
public class CarrierCO {
    private String carrierCode;
    private String carrierName;
    private String carrierTypeCode;
}
