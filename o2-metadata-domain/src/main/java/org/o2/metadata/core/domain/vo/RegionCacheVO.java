package org.o2.metadata.core.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * 静态文件
 *
 * @author: yipeng.zhu@hand-china.com 2020-05-20 13:35
 **/
@Data
public class RegionCacheVO  implements Serializable {
    private String regionName;

    private Long countryId;

    private Long parentRegionId;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    private Long regionId;

    private String regionCode;

    private String lang;

    private String countryCode;
}
