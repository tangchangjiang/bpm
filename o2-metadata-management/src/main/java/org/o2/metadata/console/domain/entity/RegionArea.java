package org.o2.metadata.console.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 大区定义
 *
 * @author houlin.cheng@hand-china.com 2020-08-07 16:51:37
 */
@ApiModel("大区定义")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "o2md_region_area")
public class RegionArea extends AuditDomain {

    public static final String FIELD_REGION_AREA_ID = "regionAreaId";
    public static final String FIELD_REGION_ID = "regionId";
    public static final String FIELD_REGION_CODE = "regionCode";
    public static final String FIELD_REGION_NAME = "regionName";
    public static final String FIELD_ENABLED_FLAG = "enabledFlag";
    public static final String FIELD_AREA_CODE = "areaCode";
    public static final String FIELD_TENANT_ID = "tenantId";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("表ID，主键")
    @Id
    @GeneratedValue
    private Long regionAreaId;
    @ApiModelProperty(value = "区域id")
    private Long regionId;
    @ApiModelProperty(value = "区域编码")
    @NotBlank
    private String regionCode;
    @ApiModelProperty(value = "区域名称")
    private String regionName;
    @ApiModelProperty(value = "是否启用标记")
    @NotNull
    private Integer enabledFlag;
    @ApiModelProperty(value = "大区，值集O2MD.AREA_CODE")
    private String areaCode;
    @ApiModelProperty(value = "租户ID")
    @NotNull
    private Long tenantId;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 表ID，主键
     */
    public Long getRegionAreaId() {
        return regionAreaId;
    }

    public void setRegionAreaId(Long regionAreaId) {
        this.regionAreaId = regionAreaId;
    }

    /**
     * @return 区域id
     */
    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    /**
     * @return 区域编码
     */
    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    /**
     * @return 区域名称
     */
    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    /**
     * @return 是否启用标记
     */
    public Integer getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(Integer enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    /**
     * @return 大区，值集O2MD.AREA_CODE
     */
    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
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
