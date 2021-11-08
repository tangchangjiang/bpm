package org.o2.metadata.infra.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Region{
    /**
     * 主键
     */
    private Long regionId;

    /**
     * 地区编码
     */
    @ApiModelProperty("地区编码")
    private String regionCode;

    /**
     * 地区名称
     */
    @ApiModelProperty("地区名称")
    private String regionName;

    /**
     * 国家ID
     */
    @ApiModelProperty("国家ID")
    private Long countryId;

    /**
     * 父地区ID
     */
    @ApiModelProperty("父地区ID")
    private Long parentRegionId;

    /**
     * 父地区编码
     */
    private String parentRegionCode;
    /**
     * 父地区名称
     */
    private String parentRegionName;
    /**
     * 等级路径
     */
    @ApiModelProperty("等级路径")
    private String levelPath;
    /**
     * 是否启用
     */
    @ApiModelProperty("是否启用")
    private Integer enabledFlag;
    /**
     * 子类
     */
    @ApiModelProperty(value = "子类")
    private List<Region> children;

    /**
     * 大区
     */
    @ApiModelProperty("大区")
    private String areaCode;
    /**
     * 大区名称
     */
    @ApiModelProperty(value = "大区名称")
    private String areaMeaning;
    /**
     * 租户ID
     */
    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
    /**
     * 国家编码
     */
    private String countryCode;
    /**
     * 国家名称
     */
    private String countryName;
    /**
     * 等级
     */
    private Integer levelNumber;

}

