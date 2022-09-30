package org.o2.metadata.console.api.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author tingting@hand-china.com
 * @date 2019/4/30
 */

@ApiModel("区域视图")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegionVO {


    @ApiModelProperty("地区编码")
    private String regionCode;

    @ApiModelProperty("地区名称")
    private String regionName;


    private String parentRegionCode;
    private String parentRegionName;


    @ApiModelProperty(value = "子类", hidden = true)
    private List<RegionVO> children;


    private String areaCode;

    @ApiModelProperty(value = "大区名称")
    private String areaMeaning;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    private String countryCode;

    private String countryName;

    private Integer levelNumber;
}
