package org.o2.metadata.api.dto;

import lombok.Data;

import java.util.List;

/**
 *
 * 地区值集查询
 *
 * @author yipeng.zhu@hand-china.com 2021-08-03
 **/
@Data
public class RegionQueryLovInnerDTO {
    /**
     * 地区名称
     */
   private String regionName;
    /**
     * 地址编码
     */
   private String regionCode;
    /**
     * 国家编码
     */
   private String countryCode;

    /**
     *  父地区编码
     */
   private List<String> parentRegionCodes;

   private List<String> notInRegionCodes;
    /**
     *  租户ID
     */
   private Long tenantId;
    /**
     *  级别
     */
   private Integer levelNumber;

   private String addressType;
    /**
     *  语言
     */
   private String lang;
    /**
     * 地址集合
     */
   private List<String> regionCodes;

    private String parentRegionCode;
}
