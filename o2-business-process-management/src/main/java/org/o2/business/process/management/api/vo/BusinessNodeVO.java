package org.o2.business.process.management.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.o2.business.process.management.domain.entity.BizNodeParameter;
import org.o2.business.process.management.infra.constant.BusinessProcessConstants;

import java.util.List;

/**
 * 业务节点列表查询结果展示
 * @author zhilin.ren@hand-china.com 2022/08/10 15:17
 */
@Data
public class BusinessNodeVO {

    @ApiModelProperty(value = "业务流程节点bean")
    private String beanId;

    @ApiModelProperty(value = "业务节点描述")
    private String description;

    @ApiModelProperty(value = "业务类型(O2MD.BUSINESS_TYPE)")
    @LovValue(value = BusinessProcessConstants.LovCode.BUSINESS_TYPE_CODE)
    private String businessTypeCode;

    @ApiModelProperty(value = "二级业务类型(O2MD.SUB_BUSINESS_TYPE)")
    @LovValue(value = BusinessProcessConstants.LovCode.SUB_BUSINESS_TYPE_CODE)
    private String subBusinessTypeCode;

    @ApiModelProperty(value = "1-启用/0-禁用", required = true)
    private Integer enabledFlag;

    @ApiModelProperty("表ID,主键,用于查询节点详情")
    private Long bizNodeId;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;


    @ApiModelProperty(value = "业务节点参数列表,lovFlag=1时才用数据")
    private List<BizNodeParameter> paramList;

    @ApiModelProperty(value = "业务类型含义")
    private String businessTypeMeaning;
    @ApiModelProperty(value = "二级业务类型含义")
    private String subBusinessTypeMeaning;



}
