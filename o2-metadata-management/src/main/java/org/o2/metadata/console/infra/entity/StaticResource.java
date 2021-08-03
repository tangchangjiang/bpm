package org.o2.metadata.console.infra.entity;

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
 * 静态资源文件表
 *
 * @author zhanpeng.jiang@hand-china.com 2021-07-30 11:11:38
 */
@ApiModel("静态资源文件表")
@VersionAudit
@ModifyAudit
@Table(name = "o2md_static_resource")
public class StaticResource extends AuditDomain {

    public static final String FIELD_RESOURCE_ID = "resourceId";
    public static final String FIELD_RESOURCE_CODE = "resourceCode";
    public static final String FIELD_SOURCE_MODULE_CODE = "sourceModuleCode";
    public static final String FIELD_RESOURCE_URL = "resourceUrl";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_LANG = "lang";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String O2MD_STATIC_RESOURCE_U1 = "o2md_static_resource_u1";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("主键")
    @Id
    @GeneratedValue
    private Long resourceId;
    @ApiModelProperty(value = "静态资源编码[模块名_资源内容名]", required = true)
    @NotBlank
    @Unique(O2MD_STATIC_RESOURCE_U1)
    private String resourceCode;
    @ApiModelProperty(value = "来源模块编码", required = true)
    @NotBlank
    private String sourceModuleCode;
    @ApiModelProperty(value = "静态资源相对路径", required = true)
    @NotBlank
    private String resourceUrl;
    @ApiModelProperty(value = "静态资源描述")
    private String description;

    @ApiModelProperty(value = "语言")
    private String lang;

    @ApiModelProperty(value = "租户Id", required = true)
    @NotNull
    @Unique(O2MD_STATIC_RESOURCE_U1)
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
    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * @return 静态资源编码[模块名_资源内容名]
     */
    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    /**
     * @return 来源模块编码
     */
    public String getSourceModuleCode() {
        return sourceModuleCode;
    }

    public void setSourceModuleCode(String sourceModuleCode) {
        this.sourceModuleCode = sourceModuleCode;
    }

    /**
     * @return 静态资源相对路径
     */
    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    /**
     * @return 静态资源描述
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 租户Id
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}

