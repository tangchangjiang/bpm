package org.o2.business.process.management.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 业务节点查询参数
 * @author zhilin.ren@hand-china.com 2022/08/10 15:04
 */
@Data
public class BusinessNodeQueryDTO {


    @ApiModelProperty(value = "业务流程节点bean")
    private String beanId;

    @ApiModelProperty(value = "业务节点描述")
    private String description;

    @ApiModelProperty(value = "业务类型(O2MD.BUSINESS_TYPE)")
    private String businessTypeCode;

    @ApiModelProperty(value = "二级业务类型(O2MD.SUB_BUSINESS_TYPE)")
    private String subBusinessTypeCode;

    @ApiModelProperty(value = "租户ID",hidden = true)
    private Long tenantId;
}
