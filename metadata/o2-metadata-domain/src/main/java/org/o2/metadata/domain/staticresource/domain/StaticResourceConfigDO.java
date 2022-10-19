package org.o2.metadata.domain.staticresource.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class StaticResourceConfigDO {

    /**
     * 静态资源编码
     */
    @ApiModelProperty(value = "静态资源编码", required = true)
    private String resourceCode;

    /**
     * 静态资源层级
     */
    @ApiModelProperty(value = "静态资源层级", required = true)
    private String resourceLevel;

    /**
     * 静态资源访问jsonKey
     */
    @ApiModelProperty(value = "静态资源访问jsonKey", required = true)
    private String jsonKey;

    /**
     * 租户ID
     */
    @ApiModelProperty(value = "租户ID", required = true)
    private Long tenantId;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 多语言标识
     */
    @ApiModelProperty(value = "是否区分多语言", required = true)
    private Integer differentLangFlag;

    /**
     * 上传目录
     */
    @ApiModelProperty(value = "上传目录")
    private String uploadFolder;

    /**
     * 来源模块
     */
    @ApiModelProperty(value = "来源模块,值集:O2MD.DOMAIN_MODULE")
    private String sourceModuleCode;

    /**
     * 来源程序
     */
    @ApiModelProperty(value = "来源程序")
    private String sourceProgram;

    /**
     * 静态资源路径
     */
    @ApiModelProperty(value = "静态资源路径")
    private List<String> resourcePathList;

    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public String getResourceLevel() {
        return resourceLevel;
    }

    public void setResourceLevel(String resourceLevel) {
        this.resourceLevel = resourceLevel;
    }

    public String getJsonKey() {
        return jsonKey;
    }

    public void setJsonKey(String jsonKey) {
        this.jsonKey = jsonKey;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDifferentLangFlag() {
        return differentLangFlag;
    }

    public void setDifferentLangFlag(Integer differentLangFlag) {
        this.differentLangFlag = differentLangFlag;
    }

    public String getUploadFolder() {
        return uploadFolder;
    }

    public void setUploadFolder(String uploadFolder) {
        this.uploadFolder = uploadFolder;
    }

    public String getSourceModuleCode() {
        return sourceModuleCode;
    }

    public void setSourceModuleCode(String sourceModuleCode) {
        this.sourceModuleCode = sourceModuleCode;
    }

    public String getSourceProgram() {
        return sourceProgram;
    }

    public void setSourceProgram(String sourceProgram) {
        this.sourceProgram = sourceProgram;
    }

    public List<String> getResourcePathList() {
        return resourcePathList;
    }

    public void setResourcePathList(List<String> resourcePathList) {
        this.resourcePathList = resourcePathList;
    }

    @Override
    public String toString() {
        return "StaticResourceConfigDO{" +
                "resourceCode='" + resourceCode + '\'' +
                ", resourceLevel='" + resourceLevel + '\'' +
                ", jsonKey='" + jsonKey + '\'' +
                ", tenantId=" + tenantId +
                ", description='" + description + '\'' +
                ", differentLangFlag=" + differentLangFlag +
                ", uploadFolder='" + uploadFolder + '\'' +
                ", sourceModuleCode='" + sourceModuleCode + '\'' +
                ", sourceProgram='" + sourceProgram + '\'' +
                ", resourcePathList=" + resourcePathList +
                '}';
    }
}
