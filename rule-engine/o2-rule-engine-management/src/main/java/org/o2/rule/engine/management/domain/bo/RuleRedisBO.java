package org.o2.rule.engine.management.domain.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

/**
 * @author xiang.zhao@hand-chian.com 2022/10/18
 */
@Data
public class RuleRedisBO {
    @ApiModelProperty("主键")
    private Long ruleId;
    @ApiModelProperty(value = "规则编码")
    private String ruleCode;
    @ApiModelProperty(value = "规则名称")
    private String ruleName;
    @ApiModelProperty(value = "实体编码")
    private String entityCode;
    @ApiModelProperty(value = "规则实体id")
    private Long ruleEntityId;
    @ApiModelProperty(value = "规则实体别名")
    private String ruleEntityAlias;
    @ApiModelProperty(value = "规则描述")
    private String ruleDescription;
    @ApiModelProperty(value = "规则JSON")
    private String ruleJson;
    @ApiModelProperty(value = "规则条件表达式")
    private String conditionExpression;
    @ApiModelProperty(value = "是否启用，默认启用")
    private Integer enableFlag;
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
}
