package org.o2.metadata.console.infra.entity;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.MultiLanguage;
import io.choerodon.mybatis.annotation.MultiLanguageField;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.boot.platform.lov.annotation.LovValue;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * 平台定义表
 *
 * @author zhilin.ren@hand-china.com 2021-08-02 11:11:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("平台定义表")
@VersionAudit
@ModifyAudit
@MultiLanguage
@Table(name = "o2md_platform")
public class Platform extends AuditDomain {

    public static final String FIELD_PLATFORM_ID = "platformId";
    public static final String FIELD_PLATFORM_CODE = "platformCode";
    public static final String FIELD_PLATFORM_TYPE_CODE = "platformTypeCode";
    public static final String FIELD_PLATFORM_NAME = "platformName";
    public static final String FIELD_ACTIVE_FLAG = "activeFlag";
    public static final String FIELD_TENANT_ID = "tenantId";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("主键Id")
    @Id
    @GeneratedValue
    private Long platformId;
    @ApiModelProperty(value = "平台编码")
    @NotNull
    private String platformCode;

    @ApiModelProperty(value = "平台类型编码")
    @LovValue(lovCode = "O2MD.PLATFORM_TYPE")
    private String platformTypeCode;
    @ApiModelProperty(value = "平台名称")
    @NotNull
    @MultiLanguageField
    private String platformName;
    @ApiModelProperty(value = "是否有效")
    @NotNull
    @LovValue(lovCode = "HPFM.ENABLED_FLAG", meaningField = "activeMeaning")
    private Integer activeFlag;
    @ApiModelProperty(value = "租户id", required = true)
    @NotNull
    @MultiLanguageField
    private Long tenantId;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------
    //

    /**
     * 平台类型含义
     */
    @Transient
    @ApiModelProperty(value = "平台类型编码")
    private String platformTypeMeaning;

    @ApiModelProperty(value = "启用含义字段")
    @Transient
    private String activeMeaning;
}

