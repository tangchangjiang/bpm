package org.o2.metadata.console.api.dto;

import lombok.Data;

/**
 *
 * 地区查询
 *
 * @author yipeng.zhu@hand-china.com 2021-08-04
 **/
@Data
public class RegionQueryDTO {
    private String countryCode;
    private Long parentRegionId;
    private String parentRegionCode;
    private Integer enabledFlag;
    private Long organizationId;
    private Integer levelNumber;
}
