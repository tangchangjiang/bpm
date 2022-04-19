package org.o2.metadata.api.co;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 服务点自提信息
 *
 * @author chao.yang05@hand-china.com 2022/4/13
 */
@Data
@ApiModel("服务点自提信息")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PosPickUpInfoCO {

    @ApiModelProperty(value = "服务点编码")
    private String posCode;

    @ApiModelProperty(value = "服务点名称")
    private String posName;

    @ApiModelProperty(value = "省")
    private String regionCode;

    @ApiModelProperty(value = "市")
    private String cityCode;

    @ApiModelProperty(value = "区")
    private String districtCode;

    @ApiModelProperty(value = "省name")
    private String regionName;

    @ApiModelProperty(value = "市name")
    private String cityName;

    @ApiModelProperty(value = "区name")
    private String districtName;

    @ApiModelProperty(value = "街道,门牌号")
    private String streetName;

    @ApiModelProperty(value = "电话")
    private String phoneNumber;

    @ApiModelProperty(value = "营业时间")
    private String businessTime;
}
