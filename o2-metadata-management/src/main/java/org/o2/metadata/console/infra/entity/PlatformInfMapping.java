package org.o2.metadata.console.infra.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import javax.validation.constraints.NotBlank;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.boot.platform.lov.annotation.LovValue;

/**
 * 平台信息匹配表
 *
 * @author zhilin.ren@hand-china.com 2021-08-02 11:11:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("平台信息匹配表")
@VersionAudit
@ModifyAudit
@Table(name = "o2md_platform_inf_mapping")
public class PlatformInfMapping extends AuditDomain {

    public static final String FIELD_PLATFORM_INF_MAPPING_ID = "platformInfMappingId";
    public static final String FIELD_INF_TYPE_CODE = "infTypeCode";
    public static final String FIELD_PLATFORM_CODE = "platformCode";
    public static final String FIELD_INF_CODE = "infCode";
    public static final String FIELD_INF_NAME = "infName";
    public static final String FIELD_PLATFORM_INF_CODE = "platformInfCode";
    public static final String FIELD_PLATFORM_INF_NAME = "platformInfName";
    public static final String FIELD_TENANT_ID = "tenantId";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("表主键")
    @Id
    @GeneratedValue
    private Long platformInfMappingId;
    @ApiModelProperty(value = "信息类型，值集：O2MD.INF_TYPE", required = true)
    @NotBlank
    @LovValue(lovCode = "O2MD.INF_TYPE")
    private String infTypeCode;
    @ApiModelProperty(value = "平台编码", required = true)
    @NotBlank
    private String platformCode;
    @ApiModelProperty(value = "信息编码", required = true)
    @NotBlank
    private String infCode;
    @ApiModelProperty(value = "信息名称", required = true)
    @NotBlank
    private String infName;
    @ApiModelProperty(value = "平台信息编码", required = true)
    @NotBlank
    private String platformInfCode;
    @ApiModelProperty(value = "平台信息名称", required = true)
    @NotBlank
    private String platformInfName;
    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @Transient
    @ApiModelProperty(value = "平台名称")
    private String platformName;

    @Transient
    @ApiModelProperty(value = "信息类型")
    private String infTypeMeaning;


    //
    // getter/setter
    // ------------------------------------------------------------------------------
}

