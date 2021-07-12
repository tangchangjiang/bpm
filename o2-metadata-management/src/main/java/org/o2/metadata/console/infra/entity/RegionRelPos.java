package org.o2.metadata.console.infra.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.MultiLanguage;
import io.choerodon.mybatis.annotation.MultiLanguageField;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
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
 * 默认服务点配置
 *
 * @author wei.cai@hand-china.com 2020-01-09 15:41:36
 */
@Data
@ApiModel("默认服务点配置")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "o2md_region_rel_pos")
@MultiLanguage
public class RegionRelPos extends AuditDomain {

    public static final String FIELD_REGION_REL_POS_ID = "regionRelPosId";
    public static final String FIELD_ONLINE_SHOP_ID = "onlineShopId";
    public static final String FIELD_AREA_CODE = "areaCode";
    public static final String FIELD_REGION_ID = "regionId";
    public static final String FIELD_REGION_DESCRIPTION = "regionDescription";
    public static final String FIELD_POS_ID = "posId";
    public static final String FIELD_ACTIVE_FLAG = "activeFlag";
    public static final String FIELD_PRIORITY = "priority";
    public static final String FIELD_TENANT_ID = "tenantId";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    @JsonIgnore
    public boolean isNew() {
        return get_status() == RecordStatus.create;
    }

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("表ID，主键")
    @Id
    @GeneratedValue
    private Long regionRelPosId;
    @ApiModelProperty(value = "网店id，关联o2md_online_shop.online_shop_id")
    @NotNull
    @Unique
    private Long onlineShopId;
    @ApiModelProperty(value = "大区，值集O2MD.AREA_CODE")
    //todo 添加值集注解
    private String areaCode;
    @ApiModelProperty(value = "区域id，关联hpfm_region.region_id")
    @NotNull
    @Unique
    private Long regionId;
    @ApiModelProperty(value = "区域描述")
    @NotBlank
    @MultiLanguageField
    private String regionDescription;
    @ApiModelProperty(value = "服务点id,关联o2md_pos.pos_id")
    @NotNull
    private Long posId;
    @ApiModelProperty(value = "是否有效")
    @NotNull
    private Integer activeFlag;
    @ApiModelProperty(value = "优先级")
    @Unique
    @NotNull
    private Long priority;
    @ApiModelProperty(value = "租户ID")
    @Unique
    private Long tenantId;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty(value = "POS编码")
    @Transient
    private String posCode;
    @ApiModelProperty(value = "POS名称")
    @Transient
    private String posName;
    @ApiModelProperty(value = "POS类型")
    @Transient
    @LovValue(lovCode = MetadataConstants.PosType.LOV_CODE)
    private String posType;
    @ApiModelProperty(value = "服务点类型含义", hidden = true)
    @Transient
    private String posTypeMeaning;

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 表ID，主键
     */
    public Long getRegionRelPosId() {
        return regionRelPosId;
    }

    public void setRegionRelPosId(Long regionRelPosId) {
        this.regionRelPosId = regionRelPosId;
    }

    /**
     * @return 网店id，关联o2md_online_shop.online_shop_id
     */
    public Long getOnlineShopId() {
        return onlineShopId;
    }

    public void setOnlineShopId(Long onlineShopId) {
        this.onlineShopId = onlineShopId;
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
     * @return 区域id，关联hpfm_region.region_id
     */
    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    /**
     * @return 区域描述
     */
    public String getRegionDescription() {
        return regionDescription;
    }

    public void setRegionDescription(String regionDescription) {
        this.regionDescription = regionDescription;
    }

    /**
     * @return 服务点id, 关联o2md_pos.pos_id
     */
    public Long getPosId() {
        return posId;
    }

    public void setPosId(Long posId) {
        this.posId = posId;
    }

    /**
     * @return 是否有效
     */
    public Integer getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(Integer activeFlag) {
        this.activeFlag = activeFlag;
    }

    /**
     * @return 优先级
     */
    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
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
