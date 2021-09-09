package org.o2.metadata.console.infra.entity;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.mybatis.annotation.Unique;
import org.o2.metadata.console.infra.constant.MetadataConstants;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 商城前端多语言内容维护表
 *
 * @author changjiang.tang@hand-china.com 2021-08-05 09:57:27
 */
@ApiModel("商城前端多语言内容维护表")
@VersionAudit
@ModifyAudit
@Table(name = "o2md_mall_lang_prompt")
public class MallLangPrompt extends AuditDomain {

    public static final String FIELD_LANG_PROMPT_ID = "langPromptId";
    public static final String FIELD_LANG = "lang";
    public static final String FIELD_PROMPT_DETAIL = "promptDetail";
    public static final String FIELD_MALL_TYPE = "mallType";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_STATUS = "status";
    public static final String O2MD_MALL_LANG_PROMPT_U1 = "o2md_mall_lang_prompt_u1";
	public static final String FIELD_STATUS_MEANING = "statusMeaning";
	public static final String FIELD_LANG_MEANING = "langMeaning";


	//
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("主键，自增字段")
    @Id
    @GeneratedValue
    private Long langPromptId;
    @ApiModelProperty(value = "语言字段，取值zh_CN/en_US", required = true)
    @NotBlank
    @Unique(O2MD_MALL_LANG_PROMPT_U1)
	@LovValue(lovCode = "O2MD.LANGUAGE", meaningField = FIELD_LANG_MEANING)
    private String lang;
    @ApiModelProperty(value = "多语言内容详情")
    private String promptDetail;
    @ApiModelProperty(value = "商城类型，取值B2B,B2C", required = true)
    @NotBlank
    @Unique(O2MD_MALL_LANG_PROMPT_U1)
    private String mallType;
    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    @Unique(O2MD_MALL_LANG_PROMPT_U1)
    private Long tenantId;
    @ApiModelProperty(value = "状态类型.值集:O2CMS.APPROVE_STATUS", required = true)
	@LovValue(lovCode = "O2CMS.APPROVE_STATUS", meaningField = FIELD_STATUS_MEANING)
    private String status;

    @ApiModelProperty(value = "创建人")
    private Long createdBy;

    @ApiModelProperty(value = "更新人")
    private Long lastUpdatedBy;

    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "适用站点范围")
    private String siteRang;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------

	@Transient
	@ApiModelProperty("状态类型含义")
	private String statusMeaning;

	@Transient
	@ApiModelProperty("语言类型含义")
	private String langMeaning;

	@Transient
	@ApiModelProperty("创建人姓名")
	private String createdByName;
	@Transient
	@ApiModelProperty("更新者姓名")
	private String lastUpdatedByName;

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 主键，自增字段
     */
	public Long getLangPromptId() {
		return langPromptId;
	}

	public void setLangPromptId(Long langPromptId) {
		this.langPromptId = langPromptId;
	}
    /**
     * @return 语言字段，取值zh_CN/en_US
     */
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
    /**
     * @return 多语言内容详情
     */
	public String getPromptDetail() {
		return promptDetail;
	}

	public void setPromptDetail(String promptDetail) {
		this.promptDetail = promptDetail;
	}
    /**
     * @return 商城类型，取值B2B,B2C
     */
	public String getMallType() {
		return mallType;
	}

	public void setMallType(String mallType) {
		this.mallType = mallType;
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
    /**
     * @return 数据状态
     */
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusMeaning() {
		return statusMeaning;
	}

	public void setStatusMeaning(String statusMeaning) {
		this.statusMeaning = statusMeaning;
	}

	public String getLangMeaning() {
		return langMeaning;
	}

	public void setLangMeaning(String langMeaning) {
		this.langMeaning = langMeaning;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public String getLastUpdatedByName() {
		return lastUpdatedByName;
	}

	public void setLastUpdatedByName(String lastUpdatedByName) {
		this.lastUpdatedByName = lastUpdatedByName;
	}

	@Override
	public Long getCreatedBy() {
		return createdBy;
	}

	@Override
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public Long getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	@Override
	public void setLastUpdatedBy(Long lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSiteRang() {
		return siteRang;
	}

	public void setSiteRang(String siteRang) {
		this.siteRang = siteRang;
	}
}

