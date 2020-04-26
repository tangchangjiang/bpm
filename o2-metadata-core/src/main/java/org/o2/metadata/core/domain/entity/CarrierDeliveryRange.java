package org.o2.metadata.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.Preconditions;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.o2.metadata.core.domain.repository.CarrierDeliveryRangeRepository;
import org.o2.metadata.core.infra.constants.BasicDataConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
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
    public static final String FIELD_COUNTRY_ID = "countryId";
    public static final String FIELD_REGION_ID = "regionId";
    public static final String FIELD_CITY_ID = "cityId";
    public static final String FIELD_DISTRICT_ID = "districtId";
    public static final String FIELD_CARRIER_ID = "carrierId";
    public static final String FIELD_TENANT_ID = "tenantId";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public boolean exist(final CarrierDeliveryRangeRepository carrierDeliveryRangeRepository) {
        final Sqls sqls = Sqls.custom().andEqualTo(CarrierDeliveryRange.FIELD_REGION_ID, this.getRegionId());
        if (this.getDeliveryRangeId() != null) {
            sqls.andNotEqualTo(CarrierDeliveryRange.FIELD_DELIVERY_RANGE_ID, this.getDeliveryRangeId());
        }
        if (this.getCarrierId() != null) {
            sqls.andEqualTo(CarrierDeliveryRange.FIELD_CARRIER_ID, this.getCarrierId());
        }

        if (this.getCountryId() != null) {
            sqls.andEqualTo(CarrierDeliveryRange.FIELD_COUNTRY_ID, this.getCountryId());
        } else {
            sqls.andIsNull(CarrierDeliveryRange.FIELD_COUNTRY_ID);
        }
        if (this.getRegionId() != null) {
            sqls.andEqualTo(CarrierDeliveryRange.FIELD_REGION_ID, this.getRegionId());
        } else {
            sqls.andIsNull(CarrierDeliveryRange.FIELD_REGION_ID);
        }

        if (this.getCityId() != null) {
            sqls.andEqualTo(CarrierDeliveryRange.FIELD_CITY_ID, this.getCityId());
        } else {
            sqls.andIsNull(CarrierDeliveryRange.FIELD_CITY_ID);
        }
        if (this.getDistrictId() != null) {
            sqls.andEqualTo(CarrierDeliveryRange.FIELD_DISTRICT_ID, this.getDistrictId());
        } else {
            sqls.andIsNull(CarrierDeliveryRange.FIELD_DISTRICT_ID);
        }
        return carrierDeliveryRangeRepository.selectCountByCondition(
                Condition.builder(CarrierDeliveryRange.class).andWhere(sqls).build()) > 0;
    }

    public void baseValidate() {
        Preconditions.checkArgument(null != this.tenantId, BasicDataConstants.ErrorCode.BASIC_DATA_TENANT_ID_IS_NULL);
        Assert.notNull(this.carrierId, "承运商不能为空");
        Assert.notNull(this.countryId, "国家不能为空");
        Assert.notNull(this.regionId, "省不能为空");

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
    private Long countryId;

    @ApiModelProperty(value = "省,id")
    @NotNull
    private Long regionId;

    @ApiModelProperty(value = "市,id")
    private Long cityId;

    @ApiModelProperty(value = "区,id")
    private Long districtId;

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
