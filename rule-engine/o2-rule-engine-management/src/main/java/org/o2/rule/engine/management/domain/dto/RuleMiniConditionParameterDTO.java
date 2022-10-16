package org.o2.rule.engine.management.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 最小条件参数VO
 *
 * @author xiang.zhao@hand-china.com 2022/10/13
 */
@Data
@ApiModel("最小条件参数VO")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class RuleMiniConditionParameterDTO {
    @ApiModelProperty("参数编码")
    private String parameterCode;
    @ApiModelProperty("参数ID")
    private Long parameterId;
    @ApiModelProperty("参数ID")
    private Long priority;
    @ApiModelProperty("数据类型")
    private String paramFormatCode;
    @ApiModelProperty("编辑类型")
    private String paramEditTypeCode;
    @ApiModelProperty("是否启用")
    private Integer enableFlag;
    @ApiModelProperty("是否多值")
    private Integer multiFlag;
    @ApiModelProperty("是否必输")
    private Integer notNullFlag;
    @ApiModelProperty("参数值")
    private String parameterValue;
    @ApiModelProperty(value = "值从")
    private String valueFiledFrom;
    @ApiModelProperty(value = "值至")
    private String valueFiledTo;
    @ApiModelProperty("业务模型")
    private String businessModel;
    @ApiModelProperty("参数值含义")
    private String parameterMeaning;
    @ApiModelProperty("LOV编码")
    private String lovCode;
    @ApiModelProperty("校验Bean")
    private String validators;
    @ApiModelProperty("参数Filter")
    private String paramFilters;
}