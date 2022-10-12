package org.o2.rule.engine.management.domain.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.mybatis.annotation.Unique;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;

/**
 * 规则实体条件
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
@ApiModel("规则实体条件")
@VersionAudit
@ModifyAudit
@Table(name = "o2re_rule_entity_condition")
public class RuleEntityCondition extends AuditDomain {

    public static final String FIELD_RULE_ENTITY_CONDITION_ID = "ruleEntityConditionId";
    public static final String FIELD_RULE_ENTITY_ID = "ruleEntityId";
    public static final String FIELD_CONDITION_CODE = "conditionCode";
    public static final String FIELD_CONDITION_NAME = "conditionName";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_CONDITION_CODE_ALIAS = "conditionCodeAlias";
    public static final String FIELD_COMPONENT_CODE = "componentCode";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String O2RE_RULE_ENTITY_CONDITION_U1 = "o2re_rule_entity_condition_u1";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("主键")
    @Id
    @GeneratedValue
    private Long ruleEntityConditionId;
    @ApiModelProperty(value = "规则实体主键，rule_entity.rule_entity_id", required = true)
    @NotNull
    @Unique(O2RE_RULE_ENTITY_CONDITION_U1)
    private Long ruleEntityId;
    @ApiModelProperty(value = "规则实体条件编码", required = true)
    @NotBlank
    @Unique(O2RE_RULE_ENTITY_CONDITION_U1)
    private String conditionCode;
    @ApiModelProperty(value = "规则实体条件名称", required = true)
    @NotBlank
    private String conditionName;
    @ApiModelProperty(value = "规则实体条件是否启用，默认启用", required = true)
    @NotNull
    private Integer enableFlag;
    @ApiModelProperty(value = "规则实体条件描述")
    private String description;
    @ApiModelProperty(value = "规则实体条件编码别名")
    private String conditionCodeAlias;
    @ApiModelProperty(value = "规则实体条件组件，默认BASIC", required = true)
    @NotBlank
    private String componentCode;
    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    @Unique(O2RE_RULE_ENTITY_CONDITION_U1)
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
    public Long getRuleEntityConditionId() {
        return ruleEntityConditionId;
    }

    public void setRuleEntityConditionId(Long ruleEntityConditionId) {
        this.ruleEntityConditionId = ruleEntityConditionId;
    }

    /**
     * @return 规则实体主键，rule_entity.rule_entity_id
     */
    public Long getRuleEntityId() {
        return ruleEntityId;
    }

    public void setRuleEntityId(Long ruleEntityId) {
        this.ruleEntityId = ruleEntityId;
    }

    /**
     * @return 规则实体条件编码
     */
    public String getConditionCode() {
        return conditionCode;
    }

    public void setConditionCode(String conditionCode) {
        this.conditionCode = conditionCode;
    }

    /**
     * @return 规则实体条件名称
     */
    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    /**
     * @return 规则实体条件是否启用，默认启用
     */
    public Integer getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(Integer enableFlag) {
        this.enableFlag = enableFlag;
    }

    /**
     * @return 规则实体条件描述
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 规则实体条件编码别名
     */
    public String getConditionCodeAlias() {
        return conditionCodeAlias;
    }

    public void setConditionCodeAlias(String conditionCodeAlias) {
        this.conditionCodeAlias = conditionCodeAlias;
    }

    /**
     * @return 规则实体条件组件，默认BASIC
     */
    public String getComponentCode() {
        return componentCode;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
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

