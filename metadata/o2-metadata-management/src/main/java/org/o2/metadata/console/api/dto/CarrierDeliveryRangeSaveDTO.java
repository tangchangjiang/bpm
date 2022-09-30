package org.o2.metadata.console.api.dto;

import com.google.common.base.Preconditions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;

/**
 * description
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/05/27 10:18
 */
@Data
@Slf4j
public class CarrierDeliveryRangeSaveDTO {

    @ApiModelProperty(value = "国家编码")
    private String countryCode;


    @ApiModelProperty(value = "省,id")
    @NotNull
    private String regionCode;

    @ApiModelProperty(value = "市,id")
    private String cityCode;

    @ApiModelProperty(value = "区,id")
    private String districtCode;

    @ApiModelProperty(value = "承运商id")
    @NotNull
    private Long carrierId;


    public void baseValidate() {
        log.info("CarrierDeliveryRangeSaveDTO#baseValidate start");
        Preconditions.checkArgument(StringUtils.isNoneBlank(this.countryCode)
                || null != this.countryCode, "国家不能为空");
        Preconditions.checkArgument(null != this.carrierId, "承运商不能为空");
        Preconditions.checkArgument(null != this.regionCode, "省不能为空");
    }
}
