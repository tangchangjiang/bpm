package org.o2.rule.engine.management.domain.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;

/**
 * 规则条件参数值
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("规则条件参数值")
@VersionAudit
@ModifyAudit
@Table(name = "o2re_cond_param_value")
@Data
public class RuleCondParamValue extends AuditDomain {

    public static final String FIELD_COND_PARAM_VALUE_ID = "condParamValueId";
    public static final String FIELD_PARAM_ID = "paramId";
    public static final String FIELD_RULE_CONDITION_ID = "ruleConditionId";
    public static final String FIELD_PARAM_VALUE = "paramValue";
    public static final String FIELD_TENANT_ID = "tenantId";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("主键")
    @Id
    @GeneratedValue
    private Long condParamValueId;
    @ApiModelProperty(value = "参数ID", required = true)
    @NotNull
    private Long paramId;
    @ApiModelProperty(value = "规则条件ID", required = true)
    @NotNull
    private Long ruleConditionId;
    @ApiModelProperty(value = "规则参数值", required = true)
    @NotBlank
    private String paramValue;
    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

}

