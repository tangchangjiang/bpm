package org.o2.metadata.core.domain.entity;

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
import org.o2.metadata.core.domain.repository.FreightTemplateRepository;
import org.o2.metadata.core.infra.constants.BasicDataConstants;
import org.springframework.util.Assert;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "o2md_freight_template")
public class FreightTemplate extends AuditDomain {

    public static final String FIELD_TEMPLATE_ID = "templateId";
    public static final String FIELD_TEMPLATE_CODE = "templateCode";
    public static final String FIELD_TEMPLATE_NAME = "templateName";
    public static final String FIELD_DELIVERY_FREE_FLAG = "deliveryFreeFlag";
    public static final String FIELD_VALUATION_TYPE = "valuationType";
    public static final String FIELD_VALUATION_UOM = "valuationUom";
    public static final String FIELD_DAFAULT_FLAG = "dafaultFlag";
    public static final String FIELD_ENABLED_FLAG = "enabledFlag";
    public static final String FIELD_TENANT_ID = "tenantId";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public boolean exist(final FreightTemplateRepository freightTemplateRepository) {
        if (this.getTemplateId() != null) {
            final Sqls sqls = Sqls.custom();
            sqls.andEqualTo(FreightTemplate.FIELD_TEMPLATE_CODE, this.getTemplateCode());
            sqls.andEqualTo(FreightTemplate.FIELD_TENANT_ID, this.getTenantId());
            sqls.andNotEqualTo(FreightTemplate.FIELD_TEMPLATE_ID, this.getTemplateId());

            return freightTemplateRepository.selectCountByCondition(
                    Condition.builder(FreightTemplate.class).andWhere(sqls).build()) > 0;
        }

        final Sqls sqls2 = Sqls.custom();
        sqls2.andEqualTo(FreightTemplate.FIELD_TEMPLATE_CODE, this.getTemplateCode());
        sqls2.andEqualTo(FreightTemplate.FIELD_TENANT_ID, this.getTenantId());

        return freightTemplateRepository.selectCountByCondition(
                Condition.builder(FreightTemplate.class).andWhere(sqls2).build()) > 0;
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
    @ApiModelProperty(value = "运费模板编码",required = true)
    @NotBlank
    private String templateCode;
    @ApiModelProperty(value = "运费模板名称")
    private String templateName;
    @ApiModelProperty(value = "是否包邮",required = true)
    @NotNull
    @Max(1)
    @Min(0)
    private Integer deliveryFreeFlag;
    @ApiModelProperty(value = "计价方式，值集O2MD.VALUATION_TYPE")
    @LovValue(lovCode = BasicDataConstants.FreightType.LOV_VALUATION_TYPE)
    private String valuationType;
    @ApiModelProperty(value = "计价单位，值集O2MD.VALUATION_UOM")
    @LovValue(lovCode = BasicDataConstants.FreightType.LOV_VALUATION_UOM)
    private String valuationUom;
    @ApiModelProperty(value = "默认运费模板标记，新建的时候默认为0",required = true)
    @NotNull
    private Integer dafaultFlag;
    @ApiModelProperty(value = "是否启用标记,默认是启用")
    private Integer enabledFlag;
    @ApiModelProperty(value = "租户ID",required = true)
    @NotNull
    private Long tenantId;

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
