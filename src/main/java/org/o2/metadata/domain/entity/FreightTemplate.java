package org.o2.metadata.domain.entity;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.ext.metadata.domain.repository.FreightTemplateRepository;
import org.o2.ext.metadata.infra.constants.BasicDataConstants;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 运费模板
 *
 * @author peng.xu@hand-china.com 2019/5/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("运费模板")
@VersionAudit
@ModifyAudit
@Table(name = "o2md_freight_template")
public class FreightTemplate extends AuditDomain {

    public static final String FIELD_TEMPLATE_ID = "templateId";
    public static final String FIELD_TEMPLATE_CODE = "templateCode";
    public static final String FIELD_TEMPLATE_NAME = "templateName";
    public static final String FIELD_IS_FREE = "isFree";
    public static final String FIELD_VALUATION_TYPE_CODE = "valuationTypeCode";
    public static final String FIELD_VALUATION_UOM_CODE = "valuationUomCode";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public boolean exist(final FreightTemplateRepository freightTemplateRepository) {
        if (this.getTemplateId() != null) {
            final Sqls sqls = Sqls.custom();
            sqls.andEqualTo(FreightTemplate.FIELD_TEMPLATE_CODE, this.getTemplateCode());
            sqls.andNotEqualTo(FreightTemplate.FIELD_TEMPLATE_ID, this.getTemplateId());

            return freightTemplateRepository.selectCountByCondition(
                    Condition.builder(FreightTemplate.class).andWhere(sqls).build()) > 0;
        }
        final List<FreightTemplate> list = freightTemplateRepository.select(FIELD_TEMPLATE_CODE, this.templateCode);
        return list.size() > 0;
    }

    public void validate() {
        Assert.notNull(this.templateCode, BasicDataConstants.ErrorCode.BASIC_DATA_FREIGHT_CODE_IS_NULL);
        Assert.notNull(this.templateName, BasicDataConstants.ErrorCode.BASIC_DATA_FREIGHT_NAME_IS_NULL);
    }

    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("表ID，主键")
    @Id
    @GeneratedValue
    private Long templateId;

    @ApiModelProperty("运费模板编码")
    private String templateCode;

    @ApiModelProperty("运费模板名称")
    private String templateName;

    @ApiModelProperty("是否包邮")
    @Max(1)
    @Min(0)
    private Integer isFree;

    @ApiModelProperty("计价方式，值集HPFM.UOM_TYPE")
    @Column(name = "valuation_type")
    private String valuationTypeCode;

    @ApiModelProperty("计价单位，值集HPFM.UOM")
    @Column(name = "valuation_uom")
    private String valuationUomCode;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty(value = "计价方式描述")
    @Transient
    private String valuationTypeMeaning;

    @ApiModelProperty(value = "计价单位描述")
    @Transient
    private String valuationUomMeaning;

}
