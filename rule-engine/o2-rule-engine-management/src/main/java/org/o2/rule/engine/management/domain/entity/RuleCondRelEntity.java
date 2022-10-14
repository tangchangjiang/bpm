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
 * 规则关联条件
 *
 * @author xiang.zhao@hand-china.com 2022-10-10
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("规则关联条件")
@VersionAudit
@ModifyAudit
@Table(name = "o2re_rule_cond_rel_entity")
@Data
public class RuleCondRelEntity extends AuditDomain {

    public static final String FIELD_RULE_COND_REL_ENTITY_ID = "ruleCondRelEntityId";
    public static final String FIELD_RULE_ID = "ruleId";
    public static final String FIELD_RULE_CODE = "ruleCode";
    public static final String FIELD_RULE_ENTITY_COND_ID = "ruleEntityCondId";
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
    private Long ruleCondRelEntityId;
    @ApiModelProperty(value = "规则ID", required = true)
    @NotNull
    private Long ruleId;
    @ApiModelProperty(value = "规则编码", required = true)
    @NotBlank
    private String ruleCode;
    @ApiModelProperty(value = "规则实体条件ID")
    private Long ruleEntityCondId;
    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------
}

