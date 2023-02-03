package org.o2.metadata.console.infra.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.repository.PosRelCarrierRepository;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

/**
 * 服务点信息
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("服务点信息")
@MultiLanguage
@VersionAudit
@ModifyAudit
@Table(name = "o2md_pos")
public class Pos extends AuditDomain {

    public static final String FIELD_POS_ID = "posId";
    public static final String FIELD_POS_CODE = "posCode";
    public static final String FIELD_POS_NAME = "posName";
    public static final String FIELD_POS_STATUS_CODE = "posStatusCode";
    public static final String FIELD_POS_TYPE_CODE = "posTypeCode";
    public static final String FIELD_BUSINESS_TYPE_CODE = "businessTypeCode";
    public static final String FIELD_OPEN_DATE = "openDate";
    public static final String FIELD_ADDRESS_ID = "posAddressId";
    public static final String FIELD_BUSINESS_TIME = "businessTime";
    public static final String FIELD_NOTICE = "notice";
    public static final String FIELD_TENANT_ID = "tenantId";

    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @GeneratedValue
    private Long posId;

    @ApiModelProperty(value = "服务点编码")
    @NotNull
    @Size(max = 255)
    private String posCode;

    @ApiModelProperty(value = "服务点名称")
    @NotNull
    @Size(max = 255)
    @MultiLanguageField
    private String posName;

    @ApiModelProperty(value = "服务点状态")
    @NotNull
    @Size(max = 255)
    @LovValue(lovCode = MetadataConstants.PosStatus.LOV_CODE)
    @Column(name = "pos_status_code")
    private String posStatusCode;

    @ApiModelProperty(value = "服务点类型,值集O2MD.POS_TYPE")
    @LovValue(lovCode = MetadataConstants.PosType.LOV_CODE)
    @NotNull
    @Size(max = 255)
    @Column(name = "pos_type_code")
    private String posTypeCode;

    @ApiModelProperty(value = "营业类型")
    @Size(max = 255)
    @LovValue(lovCode = MetadataConstants.BusinessType.LOV_CODE)
    @Column(name = "business_type_code")
    private String businessTypeCode;

    @JsonFormat(pattern = BaseConstants.Pattern.DATE)
    @DateTimeFormat(pattern = BaseConstants.Pattern.DATE)
    @ApiModelProperty(value = "开店日期")
    private LocalDate openDate;

    @ApiModelProperty(value = "详细地址")
    private Long addressId;

    @ApiModelProperty(value = "营业时间")
    @Size(max = 255)
    private String businessTime;

    @Transient
    @ApiModelProperty(value = "门店自提接单量")
    private Long pickUpLimitQuantity;

    @ApiModelProperty(value = "店铺公告信息")
    @Size(max = 255)
    private String notice;

    @ApiModelProperty(value = "平台编码")
    private String platformCode;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty(value = "门店快递发货接单量", hidden = true)
    @Transient
    private Long expressLimitQuantity;

    @ApiModelProperty(value = "门店快递发货接单量", hidden = true)
    @Transient
    private PosAddress address;

    @ApiModelProperty(value = "服务点状态含义", hidden = true)
    @Transient
    private String posStatusMeaning;

    @ApiModelProperty(value = "服务点类型含义", hidden = true)
    @Transient
    private String posTypeMeaning;

    @ApiModelProperty(value = "营业点类型含义", hidden = true)
    @Transient
    private String businessTypeMeaning;

    @ApiModelProperty(value = "服务点接单和派单时间", hidden = true)
    @Transient
    private List<PostTime> postTimes;

    @ApiModelProperty(value = "承运商名称", hidden = true)
    @Transient
    private String carrierName;

    @ApiModelProperty(value = "承运商ID")
    @Transient
    private Long carrierId;

    @ApiModelProperty(value = "组织ID", hidden = true)
    @MultiLanguageField
    private Long tenantId;

    @ApiModelProperty("省id")
    @Transient
    private String regionCode;

    @ApiModelProperty(value = "省名称", hidden = true)
    @Transient
    private String regionName;

    @ApiModelProperty("市id")
    @Transient
    private String cityCode;

    @ApiModelProperty(value = "市名称", hidden = true)
    @Transient
    private String cityName;

    @ApiModelProperty(value = "区id")
    @Transient
    private String districtCode;

    @ApiModelProperty(value = "区名称", hidden = true)
    @Transient
    private String districtName;

    @ApiModelProperty(value = "街道地址", hidden = true)
    @Transient
    private String streetName;
    @Transient
    private String countryCode;

    @ApiModelProperty(value = "联系人", hidden = true)
    @Transient
    private String contact;

    @Transient
    private String platformName;

    @ApiModelProperty(value = "服务点编码", hidden = true)
    @Transient
    private List<String> posCodes;

    public List<PosRelCarrier> posRelCarrier(PosRelCarrierRepository posRelCarrierRepository, Integer defaultFlag) {
        Condition condition =   Condition.builder(PosRelCarrier.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(PosRelCarrier.FIELD_POS_ID, this.getPosId())
                        .andEqualTo(PosRelCarrier.FIELD_TENANT_ID, this.getTenantId())
                        .andEqualTo(PosRelCarrier.FIELD_IS_DEFAULT, defaultFlag)
                ).build();
        return posRelCarrierRepository.selectByCondition(condition);
    }

}
