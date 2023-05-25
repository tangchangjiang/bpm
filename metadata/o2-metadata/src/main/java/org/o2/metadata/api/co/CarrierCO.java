package org.o2.metadata.api.co;

import lombok.Data;
import org.o2.multi.language.core.annotation.O2RedisMultiLanguageField;

/**
 * 承运商
 *
 * @author yipeng.zhu@hand-china.com 2021-08-05
 **/
@Data
public class CarrierCO {
    private String carrierCode;
    @O2RedisMultiLanguageField
    private String carrierName;
    private String carrierTypeCode;
}
