package org.o2.metadata.domain.entity;

import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.o2.metadata.domain.repository.OnlineShopRepository;
import org.o2.metadata.infra.constants.BasicDataConstants;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 网店
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("网店基础设置")
@ModifyAudit
@VersionAudit
@Table(name = "o2md_online_shop")
public class OnlineShop extends AuditDomain {
    public static final String FIELD_ONLINE_SHOP_ID = "onlineShopId";
    public static final String FIELD_ONLINE_SHOP_NAME = "onlineShopName";
    public static final String FIELD_ONLINE_SHOP_CODE = "onlineShopCode";
    public static final String FIELD_PLATFORM_SHOP_CODE = "platformShopCode";
    public static final String FIELD_PLATFORM_TYPE_CODE = "catalogCode";
    public static final String FIELD_SOURCED = "sourcedFlag";
    public static final String FIELD_IS_ACTIVE = "activeFlag";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public boolean exist(final OnlineShopRepository shopRepository) {
        if (this.onlineShopId != null) {
            return shopRepository.existsWithPrimaryKey(this);
        }
        final OnlineShop onlineShop = new OnlineShop();
        onlineShop.setOnlineShopCode(this.onlineShopCode);
        onlineShop.setOnlineShopName(this.onlineShopName);
        onlineShop.setPlatformShopCode(this.platformShopCode);
        onlineShop.setCatalogCode(this.catalogCode);
        return shopRepository.selectCount(onlineShop) > 0;
    }

    public void validate(final OnlineShopRepository onlineShopRepository) {
        if (this.getOnlineShopId() != null) {
            final OnlineShop record = onlineShopRepository.selectByPrimaryKey(this.onlineShopId);
            if (!record.getOnlineShopCode().equalsIgnoreCase(this.onlineShopCode)) {
                throw new CommonException(BasicDataConstants.ErrorCode.BASIC_DATA_ENTITY_CANNOT_UPDATE, "OnlineShop(" + this.getOnlineShopId() + ")");
            }
        }
    }

    /**
     * 按默认值初始化
     */
    public void initDefaultProperties() {
        if (this.getSourcedFlag() == null) {
            this.setSourcedFlag(0);
        }
        if (this.getActiveFlag() == null) {
            this.setActiveFlag(0);
        }
        if (this.getExchangedFlag() == null) {
            this.setExchangedFlag(0);
        }
        if (this.getReturnedFlag() == null) {
            this.setReturnedFlag(0);
        }
        if (this.getPickedUpFlag() == null) {
            this.setPickedUpFlag(0);
        }
        if (this.getEnableSplitFlag() == null) {
            this.setEnableSplitFlag(0);
        }
    }

    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty(value = "表ID", hidden = true)
    @Id
    @GeneratedValue
    private Long onlineShopId;

    @ApiModelProperty(value = "网点名称")
    @NotBlank
    @Size(max = 255)
    private String onlineShopName;

    @ApiModelProperty(value = "网点编码")
    @NotBlank
    @Size(max = 255)
    private String onlineShopCode;

    @ApiModelProperty(value = "平台店铺 code")
    @NotBlank
    @Size(max = 255)
    private String platformShopCode;

    @ApiModelProperty(value = "归属电商平台类型,值集:O2MD.CATALOG")
    @LovValue(lovCode = BasicDataConstants.Catalog.LOV_CODE)
    @NotBlank
    @Size(max = 255)
    private String catalogCode;

    @ApiModelProperty(value = "是否支持到店退", hidden = true)
    private String catalogVersionCode;

    @ApiModelProperty(value = "是否支持寻源", hidden = true)
    @Column(name = "sourced_flag")
    @NotNull
    @Range(min = 0, max = 1)
    private Integer sourcedFlag;

    @ApiModelProperty(value = "是否支持自提", hidden = true)
    @Range(min = 0, max = 1)
    @NotNull
    @Column(name = "picked_up_flag")
    private Integer pickedUpFlag;

    @ApiModelProperty(value = "是否支持到店退", hidden = true)
    @Range(min = 0, max = 1)
    @NotNull
    @Column(name = "returned_flag")
    private Integer returnedFlag;

    @ApiModelProperty(value = "是否有换货权限", hidden = true)
    @Range(min = 0, max = 1)
    @NotNull
    @Column(name = "exchanged_flag")
    private Integer exchangedFlag;

    @ApiModelProperty(value = "是否有效")
    @Range(min = 0, max = 1)
    @NotNull
    @Column(name = "active_flag")
    private Integer activeFlag;

    @ApiModelProperty(value = "是否拆分平台订单", hidden = true)
    @Range(min = 0, max = 1)
    @NotNull
    @Column(name = "enable_split_flag")
    private Integer enableSplitFlag;

    @ApiModelProperty(value = "组织ID")
    private Long organizationId;

    @ApiModelProperty(value = "归属电商平台含义")
    @Transient
    private String catalogCodeMeaning;

//    @ApiModelProperty(value = "关联目录，值集O2PCM.PLATFORM_CATALOG")
//    @Size(max = 255)
//    @LovValue(lovCode = "O2PCM.PLATFORM_CATALOG")
//    private String associatedCatalogCode;


}