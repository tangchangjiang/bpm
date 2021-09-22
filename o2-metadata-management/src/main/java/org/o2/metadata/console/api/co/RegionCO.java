package org.o2.metadata.console.api.co;

 import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
public class RegionCO{
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



    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    private String countryCode;

    private String countryName;

    private Integer levelNumber;

}

