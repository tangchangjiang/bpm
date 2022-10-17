package org.o2.rule.engine.management.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

/**
 * @author xiang.zhao@hand-chian.com 2022/10/17
 */
@Data
public class RuleVO {
    @ApiModelProperty("主键")
    private Long ruleId;
    @ApiModelProperty(value = "规则编码", required = true)
    private String ruleCode;
    @ApiModelProperty(value = "规则名称", required = true)
    private String ruleName;
    @ApiModelProperty(value = "实体编码", required = true)
    private String entityCode;
    @ApiModelProperty(value = "规则条件表达式")
    private String conditionExpression;
    @ApiModelProperty(value = "是否启用，默认启用", required = true)
    private Integer enableFlag;
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
    @ApiModelProperty(value = "租户ID", required = true)
    private Long tenantId;

    @ApiModelProperty(value = "规则实体编码，编码规则生成", required = true)
    private String ruleEntityCode;
    @ApiModelProperty(value = "规则实体别名")
    private String ruleEntityAlias;

    @ApiModelProperty(value = "规则条件")
    private RuleConditionVO condition;
}
