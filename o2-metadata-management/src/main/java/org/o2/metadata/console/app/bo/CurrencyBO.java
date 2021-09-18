package org.o2.metadata.console.app.bo;


import lombok.Data;

/**
 * 货币BO
 *
 * @author wei.cai@hand-china.com 2021/8/24
 */
@Data
public class CurrencyBO {
    /**
     * 货币名称
     */
    private String name;

    /**
     * 符号
     */
    private String currencySymbol;

    /**
     * 国际名称
     */
    private String countryName;

    /**
     * 国际编码
     */
    private String countryCode;
}
