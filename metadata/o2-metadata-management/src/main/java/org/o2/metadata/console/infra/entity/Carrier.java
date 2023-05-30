package org.o2.metadata.console.infra.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.Preconditions;
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
import org.o2.annotation.annotation.AnnotationValue;
import org.o2.annotation.infra.contants.O2AnnotationCoreConstants;
import org.o2.metadata.console.infra.constant.CarrierConstants;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.repository.CarrierRepository;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 承运商
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("承运商")
@VersionAudit
@ModifyAudit
@Table(name = "o2md_carrier")
@MultiLanguage
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Carrier extends AuditDomain {
    public static final String FIELD_CARRIER_ID = "carrierId";
    public static final String FIELD_CARRIER_CODE = "carrierCode";
    public static final String FIELD_CARRIER_NAME = "carrierName";
    public static final String FIELD_CARRIER_TYPE_CODE = "carrierTypeCode";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_PRIORITY = "priority";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public boolean exist(final CarrierRepository carrierRepository) {
        if (this.getCarrierId() != null) {
            return carrierRepository.selectCount(this) > 0;
        }
        Carrier carrier = new Carrier();
        carrier.setTenantId(this.tenantId);
        carrier.setCarrierCode(this.carrierCode);
        final List<Carrier> list = carrierRepository.select(carrier);
        return !list.isEmpty();
    }

    public void validate() {
        Preconditions.checkArgument(null != this.tenantId, MetadataConstants.ErrorCode.BASIC_DATA_TENANT_ID_IS_NULL);
        Assert.notNull(this.carrierCode, "承运商编码不能为空");
        Assert.notNull(this.carrierName, "承运商名称不能为空");
        Assert.notNull(this.carrierTypeCode, "承运商类型不能为空");
    }
    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @GeneratedValue
    private Long carrierId;

    @ApiModelProperty(value = "承运商编码")
    private String carrierCode;

    @ApiModelProperty(value = "承运商名称")
    @MultiLanguageField
    private String carrierName;

    @ApiModelProperty(value = "承运商类型.值集:O2MD.CARRIER_TYPE")
    @LovValue(lovCode = CarrierConstants.CarrierType.LOV_CODE)
    private String carrierTypeCode;

    @ApiModelProperty(value = "是否有效")
    @NotNull
    @Column(name = "active_flag")
    private Integer activeFlag;

    @ApiModelProperty(value = "送达范围类型,值集:O2MD.CARRIER_DELIVERY_REGION_TYPE")
    @NotBlank
    @LovValue(lovCode = "O2MD.CARRIER_DELIVERY_REGION_TYPE")
    private String deliveryRegionTypeCode;

    @ApiModelProperty(value = "优先级")
    @NotNull
    private Long priority;
    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty(value = "承运商类型含义", hidden = true)
    @Transient
    private String carrierTypeMeaning;

    @ApiModelProperty(value = "送达范围类型含义")
    @Transient
    private String deliveryRegionTypeMeaning;

    @ApiModelProperty(value = "组织ID")
    @MultiLanguageField
    @AnnotationValue(type = O2AnnotationCoreConstants.Type.TENANT,name = "tenantName")
    private Long tenantId;

    @ApiModelProperty("租户名称")
    @Transient
    private String tenantName;

    @ApiModelProperty(value = "承运商编码")
    @Transient
    private List<String> carrierCodes;

    @ApiModelProperty(value = "模板名称")
    @Transient
    private String templateName;
}
