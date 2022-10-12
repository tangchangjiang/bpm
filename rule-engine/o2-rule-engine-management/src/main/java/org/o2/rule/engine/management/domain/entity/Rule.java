package org.o2.rule.engine.management.domain.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.mybatis.annotation.Unique;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;

/**
 * 规则
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
@ApiModel("规则")
@VersionAudit
@ModifyAudit
@Table(name = "o2re_rule")
public class Rule extends AuditDomain {

    public static final String FIELD_RULE_ID = "ruleId";
    public static final String FIELD_RULE_CODE = "ruleCode";
    public static final String FIELD_RULE_NAME = "ruleName";
    public static final String FIELD_ENTITY_CODE = "entityCode";
    public static final String FIELD_RULE_DESCRIPTION = "ruleDescription";
    public static final String FIELD_RULE_JSON = "ruleJson";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_START_TIME = "startTime";
    public static final String FIELD_END_TIME = "endTime";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String O2RE_RULE_U1 = "o2re_rule_u1";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("主键")
    @Id
    @GeneratedValue
    private Long ruleId;
    @ApiModelProperty(value = "规则编码", required = true)
    @NotBlank
    @Unique(O2RE_RULE_U1)
    private String ruleCode;
    @ApiModelProperty(value = "规则名称", required = true)
    @NotBlank
    private String ruleName;
    @ApiModelProperty(value = "实体编码", required = true)
    @NotBlank
    private String entityCode;
    @ApiModelProperty(value = "规则描述")
    private String ruleDescription;
    @ApiModelProperty(value = "规则JSON")
    private String ruleJson;
    @ApiModelProperty(value = "是否启用，默认启用", required = true)
    @NotNull
    private Integer enableFlag;
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    @Unique(O2RE_RULE_U1)
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
     * @return 规则名称
     */
    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    /**
     * @return 实体编码
     */
    public String getEntityCode() {
        return entityCode;
    }

    public void setEntityCode(String entityCode) {
        this.entityCode = entityCode;
    }

    /**
     * @return 规则描述
     */
    public String getRuleDescription() {
        return ruleDescription;
    }

    public void setRuleDescription(String ruleDescription) {
        this.ruleDescription = ruleDescription;
    }

    /**
     * @return 规则JSON
     */
    public String getRuleJson() {
        return ruleJson;
    }

    public void setRuleJson(String ruleJson) {
        this.ruleJson = ruleJson;
    }

    /**
     * @return 是否启用，默认启用
     */
    public Integer getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(Integer enableFlag) {
        this.enableFlag = enableFlag;
    }

    /**
     * @return 开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * @return 结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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

