package org.o2.metadata.console.infra.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import org.apache.commons.collections.CollectionUtils;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.console.infra.repository.PosRelCarrierRepository;
import org.o2.metadata.console.infra.repository.PosRepository;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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

    
    /**
     *
     * 业务方法(按public protected private顺序排列)
     * @param posRepository 服务点调用
     * @return boolean
     */
    public boolean exist(final PosRepository posRepository) {
        if (this.posId != null) {
            return posRepository.existsWithPrimaryKey(this.posId);
        }

        final Pos pos = new Pos();
        pos.setPosCode(this.posCode);
        pos.setTenantId(this.tenantId);
        pos.setPosName(this.posName);
        pos.setPosTypeCode(this.posTypeCode);
        pos.setPosStatusCode(this.posStatusCode);
        return posRepository.selectCount(pos) > 0;
    }

   /**
    * 基本数据校验
    * @param posRepository 服务点调用
    */
    public void baseValidate(final PosRepository posRepository) {
        if (this.getPosId() != null) {
            final Pos record = posRepository.selectByPrimaryKey(this.posId);
            Preconditions.checkArgument(null != this.tenantId, MetadataConstants.ErrorCode.BASIC_DATA_TENANT_ID_IS_NULL);
            Assert.isTrue(record.getPosCode().equals(this.posCode), "pos code must not be changed");
            Assert.isTrue(record.getPosTypeCode().equals(this.posTypeCode), "pos type code must not be changed");
        }

        Assert.notNull(this.getAddress(), "pos must contains an address");
        Assert.notNull(this.getAddress().getDistrictCode(), "pos must contains an address");
        Assert.notNull(this.tenantId, "pos must contains tenantId");
        if (MetadataConstants.PosType.WAREHOUSE.equalsIgnoreCase(this.posTypeCode)) {
            Assert.isNull(this.businessTypeCode, "pos business type code should be null on warehouse type");
        }

        if (CollectionUtils.isNotEmpty(this.postTimes)) {
            this.postTimes.forEach(PostTime::validate);
        }
    }
    /**
     * 服务点是否存在
     * @param posRepository 服务点调用
     */
    public void validatePosCode(final PosRepository posRepository) {
        final Pos pos = new Pos();
        pos.setPosCode(this.posCode);
        pos.setTenantId(this.tenantId);
        final List<Pos> mayEmpty = posRepository.select(pos);
        if (CollectionUtils.isNotEmpty(mayEmpty)) {
            throw new CommonException(MetadataConstants.ErrorCode.BASIC_DATA_DUPLICATE_CODE, "Pos(" + pos.getPosId() + ")");
        }
    }

    public List<PosRelCarrier> posRelCarrier (PosRelCarrierRepository posRelCarrierRepository, Integer defaultFlag) {
        Condition condition =   Condition.builder(PosRelCarrier.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(PosRelCarrier.FIELD_POS_ID, this.getPosId())
                        .andEqualTo(PosRelCarrier.FIELD_TENANT_ID, this.getTenantId())
                        .andEqualTo(PosRelCarrier.FIELD_IS_DEFAULT, defaultFlag)
                ).build();
        return posRelCarrierRepository.selectByCondition(condition);
    }

    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @GeneratedValue
    private Long posId;

    @ApiModelProperty(value = "服务点编码")
    @NotBlank
    @Size(max = 255)
    private String posCode;

    @ApiModelProperty(value = "服务点名称")
    @NotBlank
    @Size(max = 255)
    @MultiLanguageField
    private String posName;

    @ApiModelProperty(value = "服务点状态")
    @NotBlank
    @Size(max = 255)
    @LovValue(lovCode = MetadataConstants.PosStatus.LOV_CODE)
    @Column(name = "pos_status_code")
    private String posStatusCode;

    @ApiModelProperty(value = "服务点类型,值集O2MD.POS_TYPE")
    @LovValue(lovCode = MetadataConstants.PosType.LOV_CODE)
    @NotBlank
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

}
