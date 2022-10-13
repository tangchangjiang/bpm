package org.o2.rule.engine.management.domain.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.mybatis.annotation.Unique;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;

/**
 * 规则参数
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("规则参数")
@VersionAudit
@ModifyAudit
@Table(name = "o2re_rule_param")
@Data
public class RuleParam extends AuditDomain {

    public static final String FIELD_RULE_PARAM_ID = "ruleParamId";
    public static final String FIELD_PARAM_REL_ENTITY_ID = "paramRelEntityId";
    public static final String FIELD_PARAM_REL_ENTITY_TYPE = "paramRelEntityType";
    public static final String FIELD_PARAM_CODE = "paramCode";
    public static final String FIELD_PARAM_NAME = "paramName";
    public static final String FIELD_PARAM_ALIAS = "paramAlias";
    public static final String FIELD_ORDER_SEQ = "orderSeq";
    public static final String FIELD_PARAM_FORMAT_CODE = "paramFormatCode";
    public static final String FIELD_PARAM_EDIT_TYPE_CODE = "paramEditTypeCode";
    public static final String FIELD_MULTIFLAG = "multiflag";
    public static final String FIELD_NOT_NULL_FLAG = "notNullFlag";
    public static final String FIELD_BUSINESS_MODEL = "businessModel";
    public static final String FIELD_VALUE_FILED_FROM = "valueFiledFrom";
    public static final String FIELD_VALUE_FILED_TO = "valueFiledTo";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_DEFAULT_MEANING = "defaultMeaning";
    public static final String FIELD_VALIDATORS = "validators";
    public static final String FIELD_FILTERS = "filters";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String O2RE_RULE_PARAM_U1 = "o2re_rule_param_u1";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("主键")
    @Id
    @GeneratedValue
    private Long ruleParamId;
    @ApiModelProperty(value = "参数关联实体id", required = true)
    @NotNull
    @Unique(O2RE_RULE_PARAM_U1)
    private Long paramRelEntityId;
    @ApiModelProperty(value = "参数关联实体类型，COND/ACTION", required = true)
    @NotBlank
    @Unique(O2RE_RULE_PARAM_U1)
    private String paramRelEntityType;
    @ApiModelProperty(value = "参数编码", required = true)
    @NotBlank
    @Unique(O2RE_RULE_PARAM_U1)
    private String paramCode;
    @ApiModelProperty(value = "参数名称", required = true)
    @NotBlank
    private String paramName;
    @ApiModelProperty(value = "参数别名")
    private String paramAlias;
    @ApiModelProperty(value = "参数顺序", required = true)
    @NotNull
    private Long orderSeq;
    @ApiModelProperty(value = "参数格式类型编码", required = true)
    @NotBlank
    private String paramFormatCode;
    @ApiModelProperty(value = "参数编辑类型编码", required = true)
    @NotBlank
    private String paramEditTypeCode;
    @ApiModelProperty(value = "多值Flag标记，默认单值", required = true)
    @NotNull
    private Integer multiflag;
    @ApiModelProperty(value = "必输标记，默认必输", required = true)
    @NotNull
    private Integer notNullFlag;
    @ApiModelProperty(value = "业务模型")
    private String businessModel;
    @ApiModelProperty(value = "值从")
    private String valueFiledFrom;
    @ApiModelProperty(value = "值至")
    private String valueFiledTo;
    @ApiModelProperty(value = "是否启用", required = true)
    @NotNull
    private Integer enableFlag;
    @ApiModelProperty(value = "LOV Meaning值")
    private String defaultMeaning;
    @ApiModelProperty(value = "校验Bean，用,分割")
    private String validators;
    @ApiModelProperty(value = "过滤Bean，用,分割")
    private String filters;
    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @Transient
    private RuleCondParamValue paramValue;
}

