package org.o2.metadata.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.mybatis.domian.SecurityToken;
import org.o2.metadata.domain.entity.FreightTemplate;
import org.o2.metadata.domain.entity.FreightTemplateDetail;
import org.springframework.cglib.beans.BeanCopier;

import javax.persistence.Transient;
import java.util.List;

/**
 * @author peng.xu@hand-china.com 2019/5/20
 */
@ApiModel("运费模板父子关系视图")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FreightTemplateVO extends FreightTemplate {

    public static final String FIELD_DEFAULT_FREIGHT_TEMPLATE_DETAILS = "defaultFreightTemplateDetails";
    public static final String FIELD_REGION_FREIGHT_TEMPLATE_DETAILS = "regionFreightTemplateDetails";

    @ApiModelProperty(value = "默认运费模板明细")
    @Transient
    private List<FreightTemplateDetail> defaultFreightTemplateDetails;

    @ApiModelProperty(value = "指定地区运费模板明细")
    @Transient
    private List<FreightTemplateDetail> regionFreightTemplateDetails;

    public FreightTemplateVO() {
    }

    public FreightTemplateVO(FreightTemplate freightTemplate) {
        final BeanCopier copier = BeanCopier.create(FreightTemplate.class, FreightTemplateVO.class, false);
        copier.copy(freightTemplate, this, null);
    }

    @Override
    public Class<? extends SecurityToken> associateEntityClass() {
        return FreightTemplate.class;
    }

}
