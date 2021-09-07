package org.o2.metadata.console.infra.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.domain.AuditDomain;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.mybatis.annotation.Unique;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.o2.metadata.console.infra.constant.MetadataConstants;

/**
 * 静态资源配置
 *
 * @author wenjie.liu@hand-china.com 2021-09-06 10:47:44
 */
@ApiModel("静态资源配置")
@VersionAudit
@ModifyAudit
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Table(name = "o2md_static_resource_config")
public class StaticResourceConfig extends AuditDomain {

    public static final String FIELD_RESOURCE_CONFIG_ID = "resourceConfigId";
    public static final String FIELD_RESOURCE_CODE = "resourceCode";
    public static final String FIELD_RESOURCE_LEVEL = "resourceLevel";
    public static final String FIELD_RESOURCE_LEVEL_MEANING = "resourceLevelMeaning";
    public static final String FIELD_JSON_KEY = "jsonKey";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_DIFFERENT_LANG_FLAG = "differentLangFlag";
    public static final String FIELD_DIFFERENT_LANG_FLAG_MEANING = "differentLangFlagMeaning";
    public static final String FIELD_UPLOAD_FOLDER = "uploadFolder";
    public static final String FIELD_SOURCE_MODULE_CODE = "sourceModuleCode";
    public static final String FIELD_SOURCE_PROGRAM = "sourceProgram";
    public static final String FIELD_ACTIVE_FLAG = "activeFlag";
    public static final String O2MD_STATIC_RESOURCE_CONFIG_U1 = "o2md_static_resource_config_u1";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("主键，自增")
    @Id
    @GeneratedValue
    private Long resourceConfigId;
    @ApiModelProperty(value = "静态资源编码", required = true)
    @NotBlank
    @Unique(O2MD_STATIC_RESOURCE_CONFIG_U1)
    private String resourceCode;
    @ApiModelProperty(value = "静态资源层级", required = true)
    @LovValue(value = MetadataConstants.PublicLov.STATIC_RESOURCE_LOV_CODE, meaningField = FIELD_RESOURCE_LEVEL_MEANING)
    @NotBlank
    private String resourceLevel;
    @ApiModelProperty(value = "静态资源访问jsonKey", required = true)
    @NotBlank
    private String jsonKey;
    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    @Unique(O2MD_STATIC_RESOURCE_CONFIG_U1)
    private Long tenantId;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "是否区分多语言", required = true)
    @LovValue(value = MetadataConstants.PublicLov.DIFFERENT_LANG_FLAG, meaningField = FIELD_DIFFERENT_LANG_FLAG_MEANING)
    @NotNull
    private Integer differentLangFlag;
    @ApiModelProperty(value = "上传目录")
    private String uploadFolder;

    @NotBlank
    @ApiModelProperty(value = "来源模块,值集:O2MD.DOMAIN_MODULE")
    @LovValue(value = MetadataConstants.PublicLov.SOURCE_MODULE_CODE)
    private String sourceModuleCode;

    @ApiModelProperty(value = "来源程序")
    @NotBlank
    private String sourceProgram;
    @ApiModelProperty(value = "启用标识")
    @NotNull
    private Integer activeFlag;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @Transient
    private String resourceLevelMeaning;

    @Transient
    private String differentLangFlagMeaning;

    @Transient
    @ApiModelProperty("来源模块")
    private String sourceModuleMeaning;

    @Transient
    @ApiModelProperty("创建人")
    private String createdName;
    @Transient
    @ApiModelProperty("更新人")
    private String updateName;

}

