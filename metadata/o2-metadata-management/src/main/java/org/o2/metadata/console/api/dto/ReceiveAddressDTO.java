package org.o2.metadata.console.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 收货地址
 *
 * @author zhilin.ren@hand-china.com 2022/10/12 11:17
 */
@Data
public class ReceiveAddressDTO {

    @ApiModelProperty(value = "国家编码")
    @NotBlank
    private String countryCode;

    @ApiModelProperty(value = "省关联")
    @NotBlank
    private String regionCode;

    @ApiModelProperty(value = "市编码")
    @NotBlank
    private String cityCode;

    @ApiModelProperty(value = "区编码")
    @NotBlank
    private String districtCode;


    public ReceiveAddressDTO copy() {
        ReceiveAddressDTO tempAddress = new ReceiveAddressDTO();
        tempAddress.setCountryCode(this.getCountryCode());
        tempAddress.setRegionCode(this.getRegionCode());
        tempAddress.setCityCode(this.getCityCode());
        tempAddress.setDistrictCode(this.getDistrictCode());
        return tempAddress;
    }


}
