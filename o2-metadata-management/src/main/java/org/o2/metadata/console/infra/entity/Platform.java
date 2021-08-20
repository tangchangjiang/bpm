package org.o2.metadata.console.infra.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@Table(name = "o2md_platform")
public class Platform extends AuditDomain {

    public static final String FIELD_PLATFORM_ID = "platformId";
    public static final String FIELD_PLATFORM_CODE = "platformCode";
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
    @ApiModelProperty(value = "平台名称")
    @NotNull
    private String platformName;
    @ApiModelProperty(value = "是否有效")
    @NotNull
    private Integer activeFlag;
    @ApiModelProperty(value = "租户id", required = true)
    @NotNull
    private Long tenantId;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------
    //
    // getter/setter
    // ------------------------------------------------------------------------------

}

