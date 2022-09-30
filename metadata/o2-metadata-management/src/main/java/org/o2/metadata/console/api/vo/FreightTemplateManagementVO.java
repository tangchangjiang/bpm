package org.o2.metadata.console.api.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.core.oauth.DetailsHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.domian.SecurityToken;
import org.o2.metadata.console.infra.entity.FreightTemplate;
import org.o2.metadata.console.infra.entity.FreightTemplateDetail;
import org.springframework.cglib.beans.BeanCopier;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * @author peng.xu@hand-china.com 2019/5/20
 */
@ApiModel("运费模板父子关系视图")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FreightTemplateManagementVO extends FreightTemplate {

    public static final String FIELD_DEFAULT_FREIGHT_TEMPLATE_DETAILS = "defaultFreightTemplateDetails";
    public static final String FIELD_REGION_FREIGHT_TEMPLATE_DETAILS = "regionFreightTemplateDetails";
    public static final String FIELD_REGION_FREIGHT_DETAIL_DISPLAY_LIST = "regionFreightDetailDisplayList";


    @ApiModelProperty(value = "默认运费模板明细")
    @Transient
    private List<FreightTemplateDetail> defaultFreightTemplateDetails;

    @ApiModelProperty(value = "指定地区运费模板明细")
    @Transient
    private List<FreightTemplateDetail> regionFreightTemplateDetails;

    @ApiModelProperty(value = "指定地区运费模板明细-中台前端显示")
    @Transient
    private List<FreightTemplateDetail> regionFreightDetailDisplayList;


    public FreightTemplateManagementVO() {
    }

    public FreightTemplateManagementVO(FreightTemplate freightTemplate) {
        final BeanCopier copier = BeanCopier.create(FreightTemplate.class, FreightTemplateManagementVO.class, false);
        copier.copy(freightTemplate, this, null);
    }

    /***
     * 跟进中台给过来的数据转化成数据库需要的数据格式
     * @param regionDetailDisplayList
     * @return
     */
    public  List<FreightTemplateDetail>  exchangeRegionDetailDisplay2DBlist(List<FreightTemplateDetail> regionDetailDisplayList){

        List<FreightTemplateDetail> regionDetailList = new ArrayList<>();
        if (CollectionUtils.isEmpty(regionDetailDisplayList)){ return  regionDetailList; }
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();

        regionDetailDisplayList.forEach(old ->{
             for (int i = 0; i  < old.getRegionIdArr().size() ; i++) {
                 FreightTemplateDetail detail =  new FreightTemplateDetail();
                 detail.setFirstPieceWeight(old.getFirstPieceWeight());
                 detail.setFirstPrice(old.getFirstPrice());
                 detail.setNextPieceWeight(old.getNextPieceWeight());
                 detail.setNextPrice(old.getNextPrice());
                 detail.setTransportTypeCode(old.getTransportTypeCode());
                 detail.setTransportTypeMeaning(old.getTransportTypeMeaning());
                 detail.setTemplateId(old.getTemplateId());
                 detail.setDefaultFlag(old.getDefaultFlag());
                 detail.setTenantId(old.getTenantId()==null?tenantId:old.getTenantId());

                 detail.setRegionCode(old.getRegionIdArr().get(i));
                 List<String> regionNameArr =  old.getRegionNameArr();
                 if (CollectionUtils.isNotEmpty(regionNameArr)){
                     detail.setRegionName( i<regionNameArr.size()?regionNameArr.get(i):null );
                 }
                 List<Long> templateDetailIdArr =  old.getTemplateDetailIdArr();
                 if (CollectionUtils.isNotEmpty(templateDetailIdArr)){
                     detail.setTemplateDetailId( i<(templateDetailIdArr.size())? templateDetailIdArr.get(i):null);
                 }
                 List<Long> objectVersionNumberArr =  old.getObjectVersionNumberArr();
                 if (CollectionUtils.isNotEmpty(objectVersionNumberArr)){
                     detail.setObjectVersionNumber(i<objectVersionNumberArr.size()?objectVersionNumberArr.get(i):null);
                 }
                 regionDetailList.add(detail);
            }
        });

        return  regionDetailList ;

    }


    @Override
    public Class<? extends SecurityToken> associateEntityClass() {
        return FreightTemplate.class;
    }

}
