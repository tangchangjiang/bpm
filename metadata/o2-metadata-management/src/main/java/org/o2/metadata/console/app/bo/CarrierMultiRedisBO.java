package org.o2.metadata.console.app.bo;

import lombok.Data;
import org.o2.multi.language.core.annotation.O2RedisMultiLanguage;
import org.o2.multi.language.core.annotation.O2RedisMultiLanguageField;

/**
 *
 * 承运商
 *
 * @author yipeng.zhu@hand-china.com 20283-05-24
 **/
@Data
@O2RedisMultiLanguage(tlsTable = "o2md_carrier_tl")
public class CarrierMultiRedisBO {
    private String carrierCode;
    @O2RedisMultiLanguageField
    private String carrierName;
    private String carrierTypeCode;
    @O2RedisMultiLanguageField(tableUniqueKey=true)
    private Long carrierId;
}
