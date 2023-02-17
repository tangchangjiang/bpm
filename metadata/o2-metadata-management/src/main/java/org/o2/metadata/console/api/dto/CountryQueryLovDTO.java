package org.o2.metadata.console.api.dto;

import lombok.Data;

/**
 * 地区值集查询
 *
 * @author yipeng.zhu@hand-china.com 2021-08-03
 **/
@Data
public class CountryQueryLovDTO {
    /**
     * 国家编码
     */
    private String countryCode;

    private Long tenantId;

    private Integer page;

    private Integer size;

    /**
     *  语言
     */
    private String lang;

}
