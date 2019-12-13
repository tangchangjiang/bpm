package org.o2.metadata.core.domain.entity;

import com.google.common.base.Preconditions;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.o2.metadata.core.domain.repository.CarrierMappingRepository;
import org.o2.metadata.core.infra.constants.BasicDataConstants;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.core.O2CoreConstants;
import org.springframework.util.Assert;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 承运商匹配表
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("承运商匹配表")
@VersionAudit
@ModifyAudit
@Table(name = "o2md_carrier_mapping")
public class CarrierMapping extends AuditDomain {

    public static final String FIELD_CARRIER_MAPPING_ID = "carrierMappingId";
    public static final String FIELD_PLATFORM_TYPE_CODE = "catalogId";
    public static final String FIELD_CARRIER_ID = "carrierId";
    public static final String FIELD_PLATFORM_CARRIER_CODE = "externalCarrierCode";
    public static final String FIELD_PLATFORM_CARRIER_NAME = "externalCarrierName";
    public static final String FIELD_TENANT_ID = "tenantId";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public boolean exist(final CarrierMappingRepository carrierMappingRepository) {
        final Sqls sqls = Sqls.custom();
        if (null != this.carrierMappingId) {
            sqls.andNotEqualTo(CarrierMapping.FIELD_CARRIER_MAPPING_ID, this.getCarrierMappingId());
        }
        if (StringUtils.isNotBlank(this.catalogCode)) {
            sqls.andEqualTo(CarrierMapping.FIELD_PLATFORM_TYPE_CODE, this.getCatalogCode());
        }
        if (null != this.carrierId) {
            sqls.andEqualTo(CarrierMapping.FIELD_CARRIER_ID, this.getCarrierId());
        }
        if (null != this.tenantId) {
            sqls.andEqualTo(CarrierMapping.FIELD_TENANT_ID, this.getTenantId());
        }
        return carrierMappingRepository.selectCountByCondition(
                Condition.builder(CarrierMapping.class).andWhere(sqls).build()) > 0;
    }

    public void baseValidate() {
        Preconditions.checkArgument(null != this.catalogCode, BasicDataConstants.ErrorCode.BASIC_DATA_CATALOG_CODE_IS_NULL);
        Preconditions.checkArgument(null != this.tenantId, BasicDataConstants.ErrorCode.BASIC_DATA_TENANT_ID_IS_NULL);
        Assert.notNull(this.carrierId, "承运商id不能为空");
        Assert.notNull(this.externalCarrierCode, "平台承运商编码不能为空");
    }

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("表ID，主键")
    @Id
    @GeneratedValue
    private Long carrierMappingId;


    @ApiModelProperty(value = "归属电商平台,值集O2MD.PLATFORM_TYPE")
    @LovValue(lovCode = O2CoreConstants.PlatformType.LOV_CODE)
    @NotNull
    private Long catalogId;

    @ApiModelProperty(value = "承运商id")
    @NotNull
    private Long carrierId;

    @ApiModelProperty(value = "平台承运商编码")
    @NotBlank
    private String externalCarrierCode;

    @ApiModelProperty(value = "平台承运商名称")
    @NotBlank
    private String externalCarrierName;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty(value = "归属电商平台含义")
    @Transient
    private String platformTypeMeaning;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @Transient
    @ApiModelProperty(value = "版本编码")
    private String catalogCode;

    @Transient
    @ApiModelProperty(value = "版本名称")
    private String catalogName;
}