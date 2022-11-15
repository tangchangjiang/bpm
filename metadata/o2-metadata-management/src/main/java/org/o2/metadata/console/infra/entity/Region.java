package org.o2.metadata.console.infra.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
public class Region {

    @ApiModelProperty("地区ID")
    private Long regionId;

    @ApiModelProperty("地区编码")
    private String regionCode;

    @ApiModelProperty("地区名称")
    private String regionName;

    @ApiModelProperty("国家ID")
    private Long countryId;

    @ApiModelProperty("父地区ID")
    private Long parentRegionId;

    private String parentRegionCode;
    private String parentRegionName;

    @ApiModelProperty("等级路径")
    private String levelPath;

    @ApiModelProperty("是否启用")
    private Integer enabledFlag;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty(value = "子类", hidden = true)
    private List<Region> children;

    @ApiModelProperty("大区，值集O2MD.AREA_CODE")
    private String areaCode;

    @ApiModelProperty(value = "大区名称")
    private String areaMeaning;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    private String countryCode;

    private String countryName;

    private Integer levelNumber;
    /**
     * 级别
     */
    @ApiModelProperty(value = "外部区域代码")
    private String externalCode;

    /**
     * 外部区域名称
     */
    @ApiModelProperty(value = "外部区域名称")
    private String externalName;

}

