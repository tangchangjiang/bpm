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
    @ApiModelProperty("参数值")
    private String parameterValue;
    @ApiModelProperty("参数值含义")
    private String parameterMeaning;
    @ApiModelProperty("数据类型")
    private String parameterType;
    @ApiModelProperty("LOV编码")
    private String lovCode;
    @ApiModelProperty("校验Bean")
    private String validators;
}