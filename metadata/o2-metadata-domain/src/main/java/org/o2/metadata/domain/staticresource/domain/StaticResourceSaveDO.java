package org.o2.metadata.domain.staticresource.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class StaticResourceSaveDO {
    @ApiModelProperty(value = "静态资源编码[模块名_资源内容名]", required = true)
    private String resourceCode;

    @ApiModelProperty(value = "静态资源相对路径", required = true)
    private String resourceUrl;

    @ApiModelProperty(value = "静态资源host")
    private String resourceHost;

    @ApiModelProperty(value = "资源级别(参考O2MD.RESOURCE_LEVEL值集)")
    private String resourceLevel;

    @ApiModelProperty(value = "资源拥有者编码(如所属站点编码)")
    private String resourceOwner;

    @ApiModelProperty(value = "是否启用，默认启用")
    private Integer enableFlag;

    @ApiModelProperty(value = "静态资源描述")
    private String description;

    @ApiModelProperty(value = "语言")
    private String lang;

    @ApiModelProperty(value = "租户Id", required = true)
    private Long tenantId;

    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getResourceHost() {
        return resourceHost;
    }

    public void setResourceHost(String resourceHost) {
        this.resourceHost = resourceHost;
    }

    public String getResourceLevel() {
        return resourceLevel;
    }

    public void setResourceLevel(String resourceLevel) {
        this.resourceLevel = resourceLevel;
    }

    public String getResourceOwner() {
        return resourceOwner;
    }

    public void setResourceOwner(String resourceOwner) {
        this.resourceOwner = resourceOwner;
    }

    public Integer getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(Integer enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "StaticResourceSaveDO{" +
                "resourceCode='" + resourceCode + '\'' +
                ", resourceUrl='" + resourceUrl + '\'' +
                ", resourceHost='" + resourceHost + '\'' +
                ", resourceLevel='" + resourceLevel + '\'' +
                ", resourceOwner='" + resourceOwner + '\'' +
                ", enableFlag=" + enableFlag +
                ", description='" + description + '\'' +
                ", lang='" + lang + '\'' +
                ", tenantId=" + tenantId +
                '}';
    }
}
