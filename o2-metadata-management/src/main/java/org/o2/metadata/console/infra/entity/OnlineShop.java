package org.o2.metadata.console.infra.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.Preconditions;
import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.MultiLanguage;
import io.choerodon.mybatis.annotation.MultiLanguageField;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;
import org.o2.metadata.console.infra.repository.OnlineShopRepository;
import org.o2.metadata.console.infra.constant.BasicDataConstants;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

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
@MultiLanguage
@Table(name = "o2md_online_shop")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OnlineShop extends AuditDomain {
    public static final String FIELD_ONLINE_SHOP_ID = "onlineShopId";
    public static final String FIELD_ONLINE_SHOP_NAME = "onlineShopName";
    public static final String FIELD_ONLINE_SHOP_CODE = "onlineShopCode";
    public static final String FIELD_PLATFORM_SHOP_CODE = "platformShopCode";
    public static final String FIELD_PLATFORM_TYPE_CODE = "catalogId";
    public static final String FIELD_SOURCED = "sourcedFlag";
    public static final String FIELD_IS_ACTIVE = "activeFlag";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public boolean exist(final OnlineShopRepository shopRepository) {
        if (this.onlineShopId != null) {
            return shopRepository.existsWithPrimaryKey(this);
        }
        final OnlineShop onlineShopWithCode = new OnlineShop();
        onlineShopWithCode.setOnlineShopCode(this.onlineShopCode);
        onlineShopWithCode.setTenantId(this.tenantId);
        List<OnlineShop> onlineShopsWithCode =  shopRepository.existenceDecide(onlineShopWithCode);
        if (onlineShopsWithCode.size() > 0) {
            return true;
        }
        final OnlineShop onlineShop = new OnlineShop();
        onlineShop.setCatalogVersionCode(this.catalogVersionCode);
        onlineShop.setTenantId(this.tenantId);
        List<OnlineShop> onlineShops =  shopRepository.existenceDecide(onlineShop);
        return onlineShops.size() > 0;
    }

    public void validate(final OnlineShopRepository onlineShopRepository) {
        Preconditions.checkArgument(null != this.tenantId, BasicDataConstants.ErrorCode.BASIC_DATA_TENANT_ID_IS_NULL);
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
    @MultiLanguageField
    private String onlineShopName;

    @ApiModelProperty(value = "网点编码")
    @NotBlank
    @Size(max = 255)
    private String onlineShopCode;

    @ApiModelProperty(value = "平台店铺 code")
    @NotBlank
    @Size(max = 255)
    private String platformShopCode;

    @Transient
    @ApiModelProperty(value = "版本主键")
    private Long catalogId;

    @Transient
    @ApiModelProperty(value = "版本目录主键")
    private Long catalogVersionId;

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
    private Long tenantId;

    @ApiModelProperty(value = "是否默认网店", hidden = true)
    private Integer isDefault;

    @ApiModelProperty(value = "默认货币", hidden = true)
    private String defaultCurrency;
    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty(value = "目录编码")
    private String catalogCode;

    @ApiModelProperty(value = "目录名称")
    @Transient
    private String catalogName;

    @ApiModelProperty(value = "版本目录编码")
    private String catalogVersionCode;

    @ApiModelProperty(value = "版本目录名称")
    @Transient
    private String catalogVersionName;

    @ApiModelProperty(value = "货币名称")
    @Transient
    private String currencyName;
}