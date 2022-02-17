package org.o2.metadata.console.infra.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

/**
 * 详细地址信息
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("详细地址")
@VersionAudit
@ModifyAudit
@Table(name = "o2md_pos_address")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PosAddress extends AuditDomain {

    public static final String FIELD_POS_ADDRESS_ID = "posAddressId";
    public static final String FIELD_COUNTRY_CODE = "countryCode";
    public static final String FIELD_REGION_CODE = "regionCode";
    public static final String FIELD_CITY_CODE = "cityCode";
    public static final String FIELD_DISTRICT_CODE = "districtCode";
    public static final String FIELD_STREET_NAME = "streetName";
    public static final String FIELD_PHONE_NUMBER = "phoneNumber";
    public static final String FIELD_POSTCODE = "postcode";
    public static final String FIELD_CONTACT = "contact";
    public static final String FIELD_MOBILE_PHONE = "mobilePhone";
    public static final String FIELD_LONGITUDE = "longitude";
    public static final String FIELD_LATITUDE = "latitude";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @GeneratedValue
    private Long posAddressId;

    @ApiModelProperty(value = "国家")
    private String countryCode;

    @ApiModelProperty(value = "省")
    private String regionCode;

    @ApiModelProperty(value = "市")
    private String cityCode;

    @ApiModelProperty(value = "区")
    private String districtCode;

    @ApiModelProperty(value = "街道,门牌号")
    private String streetName;

    @ApiModelProperty(value = "电话")
    private String phoneNumber;

    @ApiModelProperty(value = "邮编")
    private String postcode;

    @ApiModelProperty(value = "联系人")
    private String contact;

    @ApiModelProperty(value = "手机号")
    private String mobilePhone;

    @ApiModelProperty("经度")
    private BigDecimal longitude;

    @ApiModelProperty("纬度")
    private BigDecimal latitude;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty(value = "16位地址码: (国家编码,省编码,市编码,区编码,街道或楼牌,收货人姓名,收货人手机号)转成16位")
    @Transient
    private String codeDec16;

    @ApiModelProperty(value = "国家")
    @Transient
    private String countryName;

    @ApiModelProperty(value = "省")
    @Transient
    private String regionName;

    @ApiModelProperty(value = "市")
    @Transient
    private String cityName;

    @ApiModelProperty(value = "区")
    @Transient
    private String districtName;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "关联o2md_pos.pos_code 服务点编码")
    private String posCode;
}
