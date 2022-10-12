package org.o2.rule.engine.management.domain.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
@ApiModel("规则关联条件")
@VersionAudit
@ModifyAudit
@Table(name = "o2re_rule_cond_rel_entity")
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
    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 主键
     */
    public Long getRuleCondRelEntityId() {
        return ruleCondRelEntityId;
    }

    public void setRuleCondRelEntityId(Long ruleCondRelEntityId) {
        this.ruleCondRelEntityId = ruleCondRelEntityId;
    }

    /**
     * @return 规则ID
     */
    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    /**
     * @return 规则编码
     */
    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    /**
     * @return 规则实体条件ID
     */
    public Long getRuleEntityCondId() {
        return ruleEntityCondId;
    }

    public void setRuleEntityCondId(Long ruleEntityCondId) {
        this.ruleEntityCondId = ruleEntityCondId;
    }

    /**
     * @return 租户ID
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

}

