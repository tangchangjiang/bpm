package org.o2.rule.engine.management.domain.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
 * 规则实体
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("规则实体")
@VersionAudit
@ModifyAudit
@Table(name = "o2re_rule_entity")
@Data
public class RuleEntity extends AuditDomain {

    public static final String FIELD_RULE_ENTITY_ID = "ruleEntityId";
    public static final String FIELD_RULE_ENTITY_CODE = "ruleEntityCode";
    public static final String FIELD_RULE_ENTITY_NAME = "ruleEntityName";
    public static final String FIELD_RULE_ENTITY_ALIAS = "ruleEntityAlias";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String O2RE_RULE_ENTITY_U1 = "o2re_rule_entity_u1";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("主建")
    @Id
    @GeneratedValue
    private Long ruleEntityId;
    @ApiModelProperty(value = "规则实体编码，编码规则生成", required = true)
    @NotBlank
    @Unique(O2RE_RULE_ENTITY_U1)
    private String ruleEntityCode;
    @ApiModelProperty(value = "规则实体名称", required = true)
    @NotBlank
    private String ruleEntityName;
    @ApiModelProperty(value = "规则实体别名")
    private String ruleEntityAlias;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "租户id", required = true)
    @NotNull
    @Unique(O2RE_RULE_ENTITY_U1)
    private Long tenantId;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------
}

