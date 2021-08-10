package org.o2.metadata.console.api.dto;

import lombok.Data;

import java.util.List;

/**
 *
 * 地区值集查询
 *
 * @author yipeng.zhu@hand-china.com 2021-08-03
 **/
@Data
public class RegionQueryLovDTO {
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

   private Long parentRegionId;

   private String parentRegionCode;

   private List<String> parentRegionCodes;

   private Integer enabledFlag;

   private Long tenantId;

   private List<String> notInRegionCodes;

   private List<Long> parentRegionIds;

   private List<Long> regionIds;

   private Integer levelNumber;

   private String lang;

    /**
     * 地址集合
     */
   private List<String> regionCodes;
}
