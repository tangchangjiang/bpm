package org.o2.metadata.console.infra.entity;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.mybatis.annotation.Unique;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 承运商不可送达范围
 *
 * @author zhilin.ren@hand-china.com 2022-10-10 13:44:09
 */
@ApiModel("承运商不可送达范围")
@VersionAudit
@ModifyAudit
@Table(name = "o2md_carrier_cant_delivery")
@Data
@EqualsAndHashCode(callSuper = true)
public class CarrierCantDelivery extends AuditDomain {

    public static final String FIELD_CARRIER_CANT_DELIVERY_ID = "carrierCantDeliveryId";
    public static final String FIELD_CARRIER_CODE = "carrierCode";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_COUNTRY_CODE = "countryCode";
    public static final String FIELD_REGION_CODE = "regionCode";
    public static final String FIELD_CITY_CODE = "cityCode";
    public static final String FIELD_DISTRICT_CODE = "districtCode";
    public static final String O2MD_CARRIER_CANT_DELIVERY_U1 = "o2md_carrier_cant_delivery_u1";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @GeneratedValue
    private Long carrierCantDeliveryId;
    @ApiModelProperty(value = "承运商编码，关联o2md_carrier.carrier_code", required = true)
    @NotBlank
    @Unique(O2MD_CARRIER_CANT_DELIVERY_U1)
    private String carrierCode;
    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    @Unique(O2MD_CARRIER_CANT_DELIVERY_U1)
    private Long tenantId;
    @ApiModelProperty(value = "国家编码")
    @Unique(O2MD_CARRIER_CANT_DELIVERY_U1)
    private String countryCode;
    @ApiModelProperty(value = "省编码")
    @Unique(O2MD_CARRIER_CANT_DELIVERY_U1)
    private String regionCode;
    @ApiModelProperty(value = "市编码")
    @Unique(O2MD_CARRIER_CANT_DELIVERY_U1)
    private String cityCode;
    @ApiModelProperty(value = "区编码")
    @Unique(O2MD_CARRIER_CANT_DELIVERY_U1)
    private String districtCode;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------
    //
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

    // getter/setter
    // ------------------------------------------------------------------------------
}

