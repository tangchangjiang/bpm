package org.o2.metadata.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.validator.constraints.Range;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.core.base.BaseConstants;
import org.o2.boot.metadata.constants.MetadataConstants;
import org.o2.metadata.domain.repository.PosRepository;
import org.o2.metadata.infra.constants.BasicDataConstants;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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
    public static final String FIELD_CARRIER_ASSIGN = "carrierAssign";
    public static final String FIELD_ADDRESS_ID = "posAddressId";
    public static final String FIELD_BUSINESS_TIME = "businessTime";
    public static final String FIELD_ENABLE_PICKED_UP = "pickedUpFlag";
    public static final String FIELD_ENABLE_EXPRESSED = "expressedFlag";
    public static final String FIELD_SCORE = "score";
    public static final String FIELD_NOTICE = "notice";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public boolean exist(final PosRepository posRepository) {
        if (this.posId != null) {
            return posRepository.existsWithPrimaryKey(this.posId);
        }

        final Pos pos = new Pos();
        pos.setPosCode(this.posCode);
        pos.setPosName(this.posName);
        pos.setPosTypeCode(this.posTypeCode);
        pos.setPosStatusCode(this.posStatusCode);
        return posRepository.selectCount(pos) > 0;
    }

    public void baseValidate(final PosRepository posRepository) {
        if (this.getPosId() != null) {
            final Pos record = posRepository.selectByPrimaryKey(this.posId);
            Assert.isTrue(record.getPosCode().equals(this.posCode), "pos code must not be changed");
            Assert.isTrue(record.getPosTypeCode().equals(this.posTypeCode), "pos type code must not be changed");
        }

        Assert.notNull(this.getAddress(), "pos must contains an address");
        Assert.notNull(this.getAddress().getDistrictId(), "pos must contains an address");

        if (MetadataConstants.PosType.WAREHOUSE.equalsIgnoreCase(this.posTypeCode)) {
            Assert.isNull(this.businessTypeCode, "pos business type code should be null on warehouse type");
        }

        if (this.pickedUpFlag == 0) {
            Assert.isNull(this.pickUpLimitQuantity, "limit should be null when picked up is not enabled");
        }
        if (this.expressedFlag == 0) {
            Assert.isNull(this.expressLimitQuantity, "limit should be null when expressed is not enabled");
        }

        if (CollectionUtils.isNotEmpty(this.postTimes)) {
            this.postTimes.forEach(PostTime::validate);
        }
    }

    public void validatePosCode(final PosRepository posRepository) {
        final Pos pos = new Pos();
        pos.setPosCode(this.posCode);
        final List<Pos> mayEmpty = posRepository.select(pos);
        if (CollectionUtils.isNotEmpty(mayEmpty)) {
            throw new CommonException(BasicDataConstants.ErrorCode.BASIC_DATA_DUPLICATE_CODE, "Pos(" + pos.getPosId() + ")");
        }
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
    @LovValue(lovCode = BasicDataConstants.BusinessType.LOV_CODE)
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

    @ApiModelProperty(value = "门店自提")
    @NotNull
    @Column(name = "is_enable_picked_up")
    @Range(min = 0, max = 1)
    private Integer pickedUpFlag;

    @Transient
    @ApiModelProperty(value = "门店自提接单量")
    private Long pickUpLimitQuantity;

    @ApiModelProperty(value = "门店快递发货")
    @NotNull
    @Column(name = "expressed_flag")
    @Range(min = 0, max = 1)
    private Integer expressedFlag;

    @ApiModelProperty(value = "门店评分")
    private Long score;

    @ApiModelProperty(value = "门店公告")
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

    @ApiModelProperty(value = "组织ID", hidden = true)
    private Long organizationId;

    @ApiModelProperty(value = "默认承运商.code")
    @Size(max = 255)
    private String carrierAssign;
}
