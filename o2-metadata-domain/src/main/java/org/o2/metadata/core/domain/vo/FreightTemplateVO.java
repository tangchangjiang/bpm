package org.o2.metadata.core.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.core.oauth.DetailsHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.o2.metadata.core.domain.entity.FreightTemplate;
import org.o2.metadata.core.domain.entity.FreightTemplateDetail;
import org.hzero.mybatis.domian.SecurityToken;
import org.springframework.cglib.beans.BeanCopier;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


    public FreightTemplateVO() {
    }

    public FreightTemplateVO(FreightTemplate freightTemplate) {
        final BeanCopier copier = BeanCopier.create(FreightTemplate.class, FreightTemplateVO.class, false);
        copier.copy(freightTemplate, this, null);
    }

    /***
     * 跟进中台给过来的数据转化成数据库需要的数据格式
     * @param regionDetailDisplayList
     * @return
     */
    public  List<FreightTemplateDetail>  exchangeRegionDetailDisplay2DBlist( List<FreightTemplateDetail> regionDetailDisplayList){

        List<FreightTemplateDetail> regionDetailList = new ArrayList<>();
        if (CollectionUtils.isEmpty(regionDetailDisplayList)){ return  regionDetailList; }
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();

        regionDetailDisplayList.forEach(old ->{
             for (int i = 0; i  < old.getRegionIdArr().size() ; i++) {
                 FreightTemplateDetail  detail =  new FreightTemplateDetail();
                 detail.setFirstPieceWeight(old.getFirstPieceWeight());
                 detail.setFirstPrice(old.getFirstPrice());
                 detail.setNextPieceWeight(old.getNextPieceWeight());
                 detail.setNextPrice(old.getNextPrice());
                 detail.setTransportTypeCode(old.getTransportTypeCode());
                 detail.setTransportTypeMeaning(old.getTransportTypeMeaning());
                 detail.setTemplateId(old.getTemplateId());
                 detail.setDefaultFlag(old.getDefaultFlag());
                 detail.setTenantId(old.getTenantId()==null?tenantId:old.getTenantId());

                 detail.setRegionId(old.getRegionIdArr().get(i));
                 List<String> regionNameArr =  old.getRegionNameArr();
                 if (CollectionUtils.isNotEmpty(regionNameArr)){
                     detail.setRegionName( i<regionNameArr.size()?regionNameArr.get(i):null );
                 }
                 List<Long> TemplateDetailIdArr =  old.getTemplateDetailIdArr();
                 if (CollectionUtils.isNotEmpty(TemplateDetailIdArr)){
                     detail.setTemplateDetailId( i<(TemplateDetailIdArr.size())? TemplateDetailIdArr.get(i):null);
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

    /***
     * 跟进中台给过来的数据转化成数据库需要的数据格式
     * @param regionDetailTemplateList
     * @return
     */
    public  List<FreightTemplateDetail>  exchangeRegionDetailTemplateList2Displayist( List<FreightTemplateDetail> regionDetailTemplateList){

        final List<FreightTemplateDetail> regionDetailDisplayList = new ArrayList<>();
        if (CollectionUtils.isEmpty(regionDetailTemplateList)){return regionDetailDisplayList;}
        //转化成前端需要的显示格式~
        Map<String, List<FreightTemplateDetail>> complexMap = regionDetailTemplateList.stream().collect(Collectors.groupingBy(detail -> fetchGroupKey(detail)));

        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        complexMap.forEach((key,value)->{
            FreightTemplateDetail fist =   value.get(0);
            FreightTemplateDetail  detail =  new FreightTemplateDetail();
            detail.setFirstPieceWeight(fist.getFirstPieceWeight());
            detail.setFirstPrice(fist.getFirstPrice());
            detail.setNextPieceWeight(fist.getNextPieceWeight());
            detail.setNextPrice(fist.getNextPrice());
            detail.setTransportTypeCode(fist.getTransportTypeCode());
            detail.setTransportTypeMeaning(fist.getTransportTypeMeaning());
            detail.setTemplateId(fist.getTemplateId());
            detail.setDefaultFlag(fist.getDefaultFlag());
            detail.setTenantId(fist.getTenantId()==null ? tenantId:fist.getTenantId());
            detail.setRegionIdArr(value.stream().map(e -> e.getRegionId()).collect(Collectors.toList()));
            Map<String ,List<FreightTemplateDetail>> regionMap = value.stream().collect(
                    Collectors.groupingBy(regionItem -> StringUtils.isEmpty(regionItem.getParentRegionName())?"":regionItem.getParentRegionName()));
            List<String>  displayRegion = new ArrayList<>();
            regionMap.forEach((parentRegion,region)->{
              String    curStr = parentRegion+":["+ StringUtils.join(region.stream().map(e -> e.getRegionName()).collect(Collectors.toList()), ",") +"]" ;
              displayRegion.add(curStr);
            });

            detail.setRegionName(StringUtils.join(displayRegion, ","));
            detail.setRegionNameArr(value.stream().map(e -> e.getRegionName()).collect(Collectors.toList()));
            detail.setObjectVersionNumberArr(value.stream().map(e -> e.getObjectVersionNumber()).collect(Collectors.toList()));
            detail.setTemplateDetailIdArr(value.stream().map(e -> e.getTemplateDetailId()).collect(Collectors.toList()));


            regionDetailDisplayList.add(detail);
        });

        return  regionDetailDisplayList;
    }

    /**
     * 前端分组依据
     * @param detail
     * @return
     */
    public static String fetchGroupKey(final FreightTemplateDetail detail) {
        return detail.getFirstPieceWeight()+""+detail.getFirstPrice()+""+detail.getNextPieceWeight()+""+detail.getNextPrice()+""+detail.getTransportTypeCode();
    }



    @Override
    public Class<? extends SecurityToken> associateEntityClass() {
        return FreightTemplate.class;
    }

}
