package org.o2.metadata.console.infra.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
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
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.o2.metadata.console.infra.repository.OnlineShopRepository;

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
    public static final String FIELD_PLATFORM_CODE = "platformCode";
    public static final String FIELD_ONLINE_SHOP_NAME = "onlineShopName";
    public static final String FIELD_ONLINE_SHOP_CODE = "onlineShopCode";
    public static final String FIELD_PLATFORM_SHOP_CODE = "platformShopCode";
    public static final String FIELD_PLATFORM_TYPE_CODE = "catalogId";
    public static final String FIELD_SOURCED = "sourcedFlag";
    public static final String FIELD_IS_ACTIVE = "activeFlag";
    public static final String FIELD_TENANT_ID = "tenantId";

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
        if (!onlineShopsWithCode.isEmpty()) {
            return true;
        }
        final OnlineShop onlineShop = new OnlineShop();
        onlineShop.setCatalogVersionCode(this.catalogVersionCode);
        onlineShop.setTenantId(this.tenantId);
        List<OnlineShop> onlineShops =  shopRepository.existenceDecide(onlineShop);
        return !onlineShops.isEmpty();
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

    @LovValue(lovCode = "o2md_platform")
    @ApiModelProperty(value = "平台编码")
    @NotNull
    private String platformCode;

    @ApiModelProperty(value = "网点名称")
    @NotNull
    @Size(max = 255)
    @MultiLanguageField
    private String onlineShopName;

    @ApiModelProperty(value = "网点编码")
    @NotNull
    @Size(max = 255)
    private String onlineShopCode;

    @ApiModelProperty(value = "平台店铺 code")
    @Size(max = 255)
    private String platformShopCode;

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
    @MultiLanguageField
    private Long tenantId;

    @ApiModelProperty(value = "是否默认网店", hidden = true)
    private Integer isDefault;

    @ApiModelProperty(value = "默认货币", hidden = true)
    private String defaultCurrency;

    @ApiModelProperty(value = "网店类型编码", hidden = true)
    @LovValue(value = "O2MD.ONLINE_SHOP_TYPE")
    @NotNull
    private String onlineShopType;

    @ApiModelProperty(value = "目录编码")
    private String catalogCode;

    @ApiModelProperty(value = "户号")
    private String accountNumber;

    @ApiModelProperty(value = "网店业务类型，值集O2MD.BUSINESS_TYPE")
    @NotBlank
    private String businessTypeCode;
    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty(value = "网店类型编码名称")
    @Transient
    private String onlineShopTypeMeaning;

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

    @Transient
    @ApiModelProperty(value = "平台名称")
    @NotNull
    private String platformName;

    @Transient
    @ApiModelProperty(value = "网点编码")
    private List<String> onlineShopCodes;
    @Transient
    @ApiModelProperty(value = "网店名称")
    private List<String> onlineShopNames;

    @Transient
    @ApiModelProperty(value = "版本主键")
    private Long catalogId;

    @Transient
    @ApiModelProperty(value = "版本目录主键")
    private Long catalogVersionId;

}
