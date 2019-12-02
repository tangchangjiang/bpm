package org.o2.metadata.domain.entity;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.o2.metadata.domain.repository.CarrierRepository;
import org.o2.metadata.infra.constants.BasicDataConstants;
import org.springframework.util.Assert;
import javax.persistence.*;
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
public class Carrier extends AuditDomain {
    public static final String FIELD_CARRIER_ID = "carrierId";
    public static final String FIELD_CARRIER_CODE = "carrierCode";
    public static final String FIELD_CARRIER_NAME = "carrierName";
    public static final String FIELD_CARRIER_TYPE_CODE = "carrierTypeCode";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public boolean exist(final CarrierRepository carrierRepository) {
        if (this.getCarrierId() != null) {
            return carrierRepository.selectCount(this) > 0;
        }
        final List<Carrier> list = carrierRepository.select(FIELD_CARRIER_CODE, this.carrierCode);
        return list.size() > 0;
    }

    public void validate() {
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
    private String carrierName;

    @ApiModelProperty(value = "承运商类型.值集:O2MD.CARRIER_TYPE")
    @LovValue(lovCode = BasicDataConstants.CarrierType.LOV_CODE)
    private String carrierTypeCode;

    @ApiModelProperty(value = "是否有效")
    @NotNull
    @Column(name = "active_flag")
    private Integer activeFlag;
    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty(value = "承运商类型含义", hidden = true)
    @Transient
    private String carrierTypeMeaning;

    @ApiModelProperty(value = "组织ID")
    private Long tenantId;
}
