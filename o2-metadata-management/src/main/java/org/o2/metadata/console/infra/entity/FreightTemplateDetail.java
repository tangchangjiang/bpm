package org.o2.metadata.console.infra.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.console.infra.repository.FreightTemplateDetailRepository;
import org.o2.metadata.console.infra.constant.BasicDataConstants;
import org.springframework.util.Assert;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 运费模板明细
 *
 * @author peixin.zhao@hand-china.com 2019/5/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("运费模板明细")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "o2md_freight_template_detail")
public class FreightTemplateDetail extends AuditDomain {

    public static final String FIELD_TEMPLATE_DETAIL_ID = "templateDetailId";
    public static final String FIELD_TRANSPORT_TYPE_CODE = "transportTypeCode";
    public static final String FIELD_REGION_ID = "regionId";
    public static final String FIELD_FIRST_PIECE_WEIGHT = "firstPieceWeight";
    public static final String FIELD_FIRST_PRICE = "firstPrice";
    public static final String FIELD_NEXT_PIECE_WEIGHT = "nextPieceWeight";
    public static final String FIELD_NEXT_PRICE = "nextPrice";
    public static final String FIELD_DEFAULT_FLAG = "defaultFlag";
    public static final String FIELD_TEMPLATE_ID = "templateId";
    public static final String FIELD_TENANT_ID = "tenantId";

    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("表ID，主键")
    @Id
    @GeneratedValue
    private Long templateDetailId;
    @LovValue(lovCode = BasicDataConstants.FreightType.LOV_TRANSPORT_TYPE)
    @ApiModelProperty(value = "运送方式，关联值集O2MD.TRANSPORT_TYPE",required = true)
    @NotNull
    private String transportTypeCode;
    @ApiModelProperty(value = "目的地")
    private Long regionId;
    @ApiModelProperty(value = "首件/千克设置")
    private BigDecimal firstPieceWeight;
    @ApiModelProperty(value = "首件/千克价格")
    private BigDecimal firstPrice;
    @ApiModelProperty(value = "续件/千克设置")
    private BigDecimal nextPieceWeight;
    @ApiModelProperty(value = "续件/千克价格")
    private BigDecimal nextPrice;
    @ApiModelProperty(value = "是否默认")
    @LovValue(lovCode = BasicDataConstants.FreightType.LOV_HPFM_FLAG)
    private Integer defaultFlag;
    @ApiModelProperty(value = "关联运费模板ID")
    private Long templateId;
    @ApiModelProperty(value = "租户ID",required = true)
    @NotNull
    private Long tenantId;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("目的地描述")
    @Transient
    private String regionName;
    @ApiModelProperty("目的地描述集合")
    @Transient
    private String parentRegionName;
    @ApiModelProperty(value = "运送方式含义")
    @Transient
    private String transportTypeMeaning;
    @ApiModelProperty(value = "是否默认")
    @Transient
    private String defaultFlagMeaninng;


    @ApiModelProperty("目的地描述集合")
    @Transient
    private List<String> regionNameArr;
    @ApiModelProperty(value = "目的ID集合")
    @Transient
    private List<Long>  regionIdArr;
    @ApiModelProperty(value = "表ID")
    @Transient
    private List<Long>  templateDetailIdArr;
    @Transient
    @ApiModelProperty(value = "版本号集合")
    private List<Long>  objectVersionNumberArr;

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public void defaultDetailValidate() {
        Assert.isNull(this.regionId, BasicDataConstants.ErrorCode.BASIC_DATA_FREIGHT_DETAIL_REGION_IS_NULL);
        validate();
    }

    public void regionDetailValidate() {
        Assert.notNull(this.regionId, BasicDataConstants.ErrorCode.BASIC_DATA_FREIGHT_DETAIL_REGION_IS_NULL);
        validate();
    }

    private void validate() {
        Assert.notNull(this.firstPieceWeight, BasicDataConstants.ErrorCode.BASIC_DATA_FREIGHT_DETAIL_FIRST_PIECE_WEIGHT_IS_NULL);
        Assert.notNull(this.firstPrice, BasicDataConstants.ErrorCode.BASIC_DATA_FREIGHT_DETAIL_FIRST_PRICE_IS_NULL);
        Assert.notNull(this.nextPieceWeight, BasicDataConstants.ErrorCode.BASIC_DATA_FREIGHT_DETAIL_NEXT_PIECE_WEIGHT_IS_NULL);
        Assert.notNull(this.nextPrice, BasicDataConstants.ErrorCode.BASIC_DATA_FREIGHT_DETAIL_NEXT_PRICE_IS_NULL);
        Assert.notNull(this.templateId, BasicDataConstants.ErrorCode.BASIC_DATA_FREIGHT_DETAIL_TEMPLATE_ID_IS_NULL);
    }

    public boolean exist(final FreightTemplateDetailRepository freightTemplateDetailRepository, final boolean isRegion) {
        if (!isRegion) { return false;}
        final Sqls sqls = Sqls.custom();
        sqls.andEqualTo(FreightTemplateDetail.FIELD_TEMPLATE_ID, this.getTemplateId());
            sqls.andEqualTo(FreightTemplateDetail.FIELD_REGION_ID, this.getRegionId());

        // 更新操作时，验证更新后的数据不与数据库中其他行重复
        if (this.getTemplateDetailId() != null) {
            sqls.andNotEqualTo(FreightTemplateDetail.FIELD_TEMPLATE_DETAIL_ID, this.getTemplateDetailId());
        }

        return freightTemplateDetailRepository.selectCountByCondition(
                    Condition.builder(FreightTemplateDetail.class).andWhere(sqls).build()) > 0;


    }


}
