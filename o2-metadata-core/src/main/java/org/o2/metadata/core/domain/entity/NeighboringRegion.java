package org.o2.metadata.core.domain.entity;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.o2.metadata.core.domain.repository.NeighboringRegionRepository;
import org.o2.metadata.core.infra.constants.MetadataConstants;
import org.hzero.boot.platform.lov.annotation.LovValue;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * 临近省
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
@ApiModel("临近省")
@VersionAudit
@ModifyAudit
@Table(name = "o2md_neighboring_region")
public class NeighboringRegion extends AuditDomain {

    public static final String FIELD_NEIGHBORING_REGION_ID = "neighboringRegionId";
    public static final String FIELD_POS_TYPE_CODE = "posTypeCode";
    public static final String FIELD_SOURCE_COUNTRY_ID = "sourceCountryId";
    public static final String FIELD_SOURCE_REGION_ID = "sourceRegionId";
    public static final String FIELD_TARGET_COUNTRY_ID = "targetCountryId";
    public static final String FIELD_TARGET_REGION_ID = "targetRegionId";
    public static final String FIELD_COUNTRY_ID = "countryId";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public boolean exist(final NeighboringRegionRepository regionRepository,
                         final NeighboringRegion neighboringRegion) {
        return regionRepository.selectCount(neighboringRegion) > 0;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        final NeighboringRegion that = (NeighboringRegion) o;
        return posTypeCode.equals(that.posTypeCode) &&
                sourceRegionId.equals(that.sourceRegionId) &&
                targetRegionId.equals(that.targetRegionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(posTypeCode, sourceRegionId, targetRegionId);
    }
    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @GeneratedValue
    private Long neighboringRegionId;

    @ApiModelProperty(value = "服务点类型,值集: O2MD.POS_TYPE")
    @NotBlank
    @LovValue(lovCode = MetadataConstants.PosType.LOV_CODE)
    private String posTypeCode;

    @ApiModelProperty(value = "发货国家")
    @NotNull
    private Long sourceCountryId;

    @ApiModelProperty(value = "发货省")
    @NotNull
    private Long sourceRegionId;

    @ApiModelProperty(value = "收货国家")
    @NotNull
    private Long targetCountryId;

    @ApiModelProperty(value = "收货省")
    @NotNull
    private Long targetRegionId;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty(value = "发货省", hidden = true)
    @Transient

    private String sourceRegionName;

    @ApiModelProperty(value = "收货省", hidden = true)
    @Transient
    private String targetRegionName;

    @ApiModelProperty(value = "pos类型含义", hidden = true)
    @Transient
    private String posTypeMeaning;

    @ApiModelProperty(hidden = true)
    @Transient
    private String sourceCountryCode;

    @ApiModelProperty(hidden = true)
    @Transient
    private String sourceCountryName;

    @ApiModelProperty(hidden = true)
    @Transient
    private String targetCountryCode;

    @ApiModelProperty(hidden = true)
    @Transient
    private String targetCountryName;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
}
