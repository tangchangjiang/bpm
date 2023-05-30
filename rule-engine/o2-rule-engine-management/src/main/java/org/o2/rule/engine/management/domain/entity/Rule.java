package org.o2.rule.engine.management.domain.entity;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.mybatis.annotation.Unique;
import org.o2.core.helper.JsonHelper;
import org.o2.rule.engine.management.domain.dto.RuleConditionDTO;
import org.o2.rule.engine.management.infra.constants.RuleEngineConstants;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 规则
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("规则")
@VersionAudit
@ModifyAudit
@Table(name = "o2re_rule")
@Data
public class Rule extends AuditDomain {

    public static final String FIELD_RULE_ID = "ruleId";
    public static final String FIELD_RULE_CODE = "ruleCode";
    public static final String FIELD_RULE_STATUS = "ruleStatus";
    public static final String FIELD_USED_FLAG = "usedFlag";
    public static final String FIELD_RULE_NAME = "ruleName";
    public static final String FIELD_ENTITY_CODE = "entityCode";
    public static final String FIELD_RULE_DESCRIPTION = "ruleDescription";
    public static final String FIELD_RULE_JSON = "ruleJson";
    public static final String FIELD_START_TIME = "startTime";
    public static final String FIELD_END_TIME = "endTime";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String O2RE_RULE_U1 = "o2re_rule_u1";

    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("主键")
    @Id
    @GeneratedValue
    private Long ruleId;
    @Unique(O2RE_RULE_U1)
    @ApiModelProperty(value = "规则编码")
    private String ruleCode;
    @ApiModelProperty(value = "规则名称")
    @NotBlank
    private String ruleName;
    @ApiModelProperty(value = "实体编码")
    @NotBlank
    private String entityCode;
    @ApiModelProperty(value = "规则描述")
    private String ruleDescription;
    @ApiModelProperty(value = "规则JSON")
    private String ruleJson;
    @ApiModelProperty(value = "规则条件表达式")
    private String conditionExpression;
    @ApiModelProperty(value = "规则状态")
    @LovValue(lovCode = RuleEngineConstants.RuleStatus.CODE)
    @NotBlank
    private String ruleStatus;
    @ApiModelProperty(value = "是否使用标记，默认单值0")
    private Integer usedFlag;
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
    @Unique(O2RE_RULE_U1)
    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty(value = "状态含义")
    @Transient
    private String ruleStatusMeaning;

    @Transient
    @ApiModelProperty(value = "规则实体id")
    private Long ruleEntityId;

    @Transient
    @ApiModelProperty(value = "规则实体名")
    private String ruleEntityName;

    @Transient
    @ApiModelProperty(value = "规则实体别名")
    private String ruleEntityAlias;

    @Transient
    @ApiModelProperty(value = "规则创建人名称")
    private String createUserName;

    @Transient
    @ApiModelProperty(value = "规则修改人名称")
    private String updateUserName;

    @Transient
    @NotNull
    @ApiModelProperty(value = "规则条件")
    private RuleConditionDTO conditionDTO;

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    /**
     * 构建Rule Json
     */
    public void buildRule() {
        if (null != this.getConditionDTO()) {
            final String qlExpress = this.getConditionDTO().build(this, null);
            final String rule = JsonHelper.objectToString(this.getConditionDTO());
            this.setRuleJson(rule);
            this.setConditionExpression(qlExpress);
        }
    }

    /**
     * 校验规则参数合法性
     */
    public void validRule() {
        if (null != this.getConditionDTO()) {
            this.getConditionDTO().valid();
        }
    }

    /**
     * 校验规则参数合法性
     */
    public void init() {
        this.ruleId = null;
        this.ruleCode = null;
        this.setObjectVersionNumber(null);
        this.setCreatedBy(null);
        this.setLastUpdatedBy(null);
        this.setCreationDate(null);
        this.setLastUpdateDate(null);
    }
}

