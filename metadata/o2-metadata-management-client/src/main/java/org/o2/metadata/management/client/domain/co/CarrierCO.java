package org.o2.metadata.management.client.domain.co;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 承运商
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
@ApiModel("承运商")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarrierCO {

    @ApiModelProperty("表ID，主键，供其他表做外键")
    private Long carrierId;

    @ApiModelProperty(value = "承运商编码")
    private String carrierCode;

    @ApiModelProperty(value = "承运商名称")
    private String carrierName;

    @ApiModelProperty(value = "承运商类型.值集:O2MD.CARRIER_TYPE")
    private String carrierTypeCode;

    @ApiModelProperty(value = "是否有效")
    private Integer activeFlag;

    @ApiModelProperty(value = "组织ID")
    private Long tenantId;

    @ApiModelProperty(value = "优先级")
    private Long priority;

}
