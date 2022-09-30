package org.o2.metadata.console.infra.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.Preconditions;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.repository.CarrierDeliveryRangeRepository;
import org.springframework.util.Assert;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * 承运商送达范围
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("承运商送达范围")
@VersionAudit
@ModifyAudit
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "o2md_carrier_delivery_range")
public class CarrierDeliveryRange extends AuditDomain {

    public static final String FIELD_DELIVERY_RANGE_ID = "deliveryRangeId";
    public static final String FIELD_COUNTRY_CODE = "countryCode";
    public static final String FIELD_REGION_CODE = "regionCode";
    public static final String FIELD_CITY_CODE = "cityCode";
    public static final String FIELD_DISTRICT_CODE = "districtCode";
    public static final String FIELD_CARRIER_ID = "carrierId";
    public static final String FIELD_TENANT_ID = "tenantId";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public boolean exist(final CarrierDeliveryRangeRepository carrierDeliveryRangeRepository) {
        final Sqls sqls = Sqls.custom().andEqualTo(CarrierDeliveryRange.FIELD_REGION_CODE, this.getRegionCode());
        if (this.getDeliveryRangeId() != null) {
            sqls.andNotEqualTo(CarrierDeliveryRange.FIELD_DELIVERY_RANGE_ID, this.getDeliveryRangeId());
        }
        if (this.getCarrierId() != null) {
            sqls.andEqualTo(CarrierDeliveryRange.FIELD_CARRIER_ID, this.getCarrierId());
        }

        if (this.getCountryCode() != null) {
            sqls.andEqualTo(CarrierDeliveryRange.FIELD_COUNTRY_CODE, this.getCountryCode());
        } else {
            sqls.andIsNull(CarrierDeliveryRange.FIELD_COUNTRY_CODE);
        }
        if (this.getRegionCode() != null) {
            sqls.andEqualTo(CarrierDeliveryRange.FIELD_REGION_CODE, this.getRegionCode());
        } else {
            sqls.andIsNull(CarrierDeliveryRange.FIELD_REGION_CODE);
        }

        if (this.getCityCode() != null) {
            sqls.andEqualTo(CarrierDeliveryRange.FIELD_CITY_CODE, this.getCityCode());
        } else {
            sqls.andIsNull(CarrierDeliveryRange.FIELD_CITY_CODE);
        }
        if (this.getDistrictCode() != null) {
            sqls.andEqualTo(CarrierDeliveryRange.FIELD_DISTRICT_CODE, this.getDistrictCode());
        } else {
            sqls.andIsNull(CarrierDeliveryRange.FIELD_DISTRICT_CODE);
        }
        return carrierDeliveryRangeRepository.selectCountByCondition(
                Condition.builder(CarrierDeliveryRange.class).andWhere(sqls).build()) > 0;
    }

    public void baseValidate() {
        Preconditions.checkArgument(null != this.tenantId, MetadataConstants.ErrorCode.BASIC_DATA_TENANT_ID_IS_NULL);
        Assert.notNull(this.carrierId, "承运商不能为空");
        Assert.notNull(this.countryCode, "国家不能为空");
        Assert.notNull(this.regionCode, "省不能为空");

    }
    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @GeneratedValue
    private Long deliveryRangeId;

    @ApiModelProperty(value = "国家,id")
    @NotNull
    private String countryCode;

    @ApiModelProperty(value = "省,id")
    @NotNull
    private String regionCode;

    @ApiModelProperty(value = "市,id")
    private String cityCode;

    @ApiModelProperty(value = "区,id")
    private String districtCode;

    @ApiModelProperty(value = "承运商id")
    @NotNull
    private Long carrierId;


    @ApiModelProperty(value = "国家,名称", hidden = true)
    @Transient
    private String countryName;

    @ApiModelProperty(value = "省,名称", hidden = true)
    @Transient
    private String regionName;

    @ApiModelProperty(value = "市,名称", hidden = true)
    @Transient
    private String cityName;

    @ApiModelProperty(value = "区,名称", hidden = true)
    @Transient
    private String districtName;

    @ApiModelProperty(value = "承运商名称", hidden = true)
    @Transient
    private String carrierName;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
}
