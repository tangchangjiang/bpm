package org.o2.metadata.console.app.bo;

import lombok.Data;

/**
 * 地址匹配 BO
 * @author hanzhu.chen@hand-china.com 2022-03-21
 */
@Data
public class AddressMappingBO {

    /**
     * 地区名称
     */
    private String regionName;
    /**
     * 上级地区编码
     */
    private String parentRegionCode;
    /**
     * 地区编码
     */
    private String regionCode;
    /**
     * 国家编码
     */
    private String countryCode;
    /**
     * 外部名称
     */
    private String externalName;
}
