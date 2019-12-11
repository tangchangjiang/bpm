package org.o2.metadata.core.domain.entity;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.o2.metadata.core.domain.repository.FreightTemplateDetailRepository;
import org.o2.metadata.core.infra.constants.BasicDataConstants;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.util.Assert;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

/**
 * 运费模板明细
 *
 * @author peng.xu@hand-china.com 2019/5/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("运费模板明细")
@VersionAudit
@ModifyAudit
@Table(name = "o2md_freight_template_detail")
public class FreightTemplateDetail extends AuditDomain {

    public static final String FIELD_TEMPLATE_DETAIL_ID = "templateDetailId";
    public static final String FIELD_CARRIER_ID = "carrierId";
    public static final String FIELD_REGION_ID = "regionId";
    public static final String FIELD_FIRST_PIECE_WEIGHT = "firstPieceWeight";
    public static final String FIELD_FIRST_PRICE = "firstPrice";
    public static final String FIELD_NEXT_PIECE_WEIGHT = "nextPieceWeight";
    public static final String FIELD_NEXT_PRICE = "nextPrice";
    public static final String FIELD_IS_DEFAULT = "defaultFlag";
    public static final String FIELD_TEMPLATE_ID = "templateId";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public void defaultDetailValidate() {
        Assert.notNull(this.carrierId, BasicDataConstants.ErrorCode.BASIC_DATA_FREIGHT_DETAIL_CARRIER_IS_NULL);
        Assert.isNull(this.regionId, BasicDataConstants.ErrorCode.BASIC_DATA_FREIGHT_DETAIL_REGION_IS_NULL);
        validate();
    }

    public void regionDetailValidate() {
        Assert.notNull(this.carrierId, BasicDataConstants.ErrorCode.BASIC_DATA_FREIGHT_DETAIL_CARRIER_IS_NULL);
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
        final Sqls sqls = Sqls.custom();
        sqls.andEqualTo(FreightTemplateDetail.FIELD_TEMPLATE_ID, this.getTemplateId());
        sqls.andEqualTo(FreightTemplateDetail.FIELD_CARRIER_ID, this.getCarrierId());

        if (isRegion) {
            sqls.andEqualTo(FreightTemplateDetail.FIELD_REGION_ID, this.getRegionId());
        } else {
            sqls.andIsNull(FreightTemplateDetail.FIELD_REGION_ID);
        }

        // 更新操作时，验证更新后的数据不与数据库中其他行重复
        if (this.getTemplateDetailId() != null) {
            sqls.andNotEqualTo(FreightTemplateDetail.FIELD_TEMPLATE_DETAIL_ID, this.getTemplateDetailId());
        }

        return freightTemplateDetailRepository.selectCountByCondition(
                Condition.builder(FreightTemplateDetail.class).andWhere(sqls).build()) > 0;
    }

    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("表ID，主键")
    @Id
    @GeneratedValue
    private Long templateDetailId;

    @ApiModelProperty("快递公司")
    private Long carrierId;

    @ApiModelProperty("目的地")
    private Long regionId;

    @ApiModelProperty("首件/千克设置")
    private BigDecimal firstPieceWeight;

    @ApiModelProperty("首件/千克价格")
    private BigDecimal firstPrice;

    @ApiModelProperty("续件/千克设置")
    private BigDecimal nextPieceWeight;

    @ApiModelProperty("续件/千克价格")
    private BigDecimal nextPrice;

    @ApiModelProperty("是否默认")
    private Integer isDefault;

    @ApiModelProperty("关联运费模板ID")
    private Long templateId;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("快递公司描述")
    @Transient
    private String carrierName;

    @ApiModelProperty("目的地描述")
    @Transient
    private String regionName;

    @ApiModelProperty(value = "承运商类型.值集:O2MD.CARRIER_TYPE")
    @LovValue(lovCode = BasicDataConstants.CarrierType.LOV_CODE)
    @Transient
    private String carrierTypeCode;

    @ApiModelProperty(value = "承运商类型含义")
    @Transient
    private String carrierTypeMeaning;
}
