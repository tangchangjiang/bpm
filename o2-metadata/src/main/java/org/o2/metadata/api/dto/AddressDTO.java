package org.o2.metadata.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 *
 * 地区
 *
 * @author yipeng.zhu@hand-china.com 2021-07-14
 **/
@Data
public class AddressDTO {
    @ApiModelProperty(value = "省code")
    private String regionCode;

    @ApiModelProperty(value = "市code")
    private String cityCode;

    @ApiModelProperty(value = "区code")
    private String districtCode;
}
