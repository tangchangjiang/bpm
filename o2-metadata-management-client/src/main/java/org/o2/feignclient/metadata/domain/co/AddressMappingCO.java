package org.o2.feignclient.metadata.domain.co;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 地址匹配
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("地址匹配")
public class AddressMappingCO {


    @ApiModelProperty(value = "region 关联")
    private String regionCode;

    private String regionName;


    @ApiModelProperty(value = "外部区域代码")
    private String externalCode;

    @ApiModelProperty(value = "外部区域名称")
    private String externalName;

    @ApiModelProperty(value = "是否启用")
    private Integer activeFlag;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "地址类型.值集:O2MD.ADDRESS_TYPE")
    private String addressTypeCode;

    private List<AddressMappingCO> children;

}
