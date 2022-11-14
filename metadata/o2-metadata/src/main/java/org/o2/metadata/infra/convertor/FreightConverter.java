package org.o2.metadata.infra.convertor;

import org.o2.metadata.api.co.FreightInfoCO;
import org.o2.metadata.api.co.FreightTemplateDetailCO;
import org.o2.metadata.api.co.FreightTemplateCO;
import org.o2.metadata.domain.freight.domain.FreightInfoDO;
import org.o2.metadata.domain.freight.domain.FreightTemplateDO;
import org.o2.metadata.domain.freight.domain.FreightTemplateDetailDO;
import org.o2.metadata.infra.entity.FreightInfo;
import org.o2.metadata.infra.entity.FreightTemplate;
import org.o2.metadata.infra.entity.FreightTemplateDetail;

/**
 *
 * 运费模版转换
 *
 * @author yipeng.zhu@hand-china.com 2021-07-19
 **/
public class FreightConverter {
    private FreightConverter() { }

    /**
     * 
     * @param 
     */
    public static FreightInfoDO poToDoObject(FreightInfo freightInfo) {

        if (freightInfo == null) {
            return null;
        }
        FreightInfoDO freightInfoDO = new FreightInfoDO();
        freightInfoDO.setFreightTemplateCode(freightInfo.getFreightTemplateCode());
        freightInfoDO.setHeadTemplate(toFreightTemplateDO(freightInfo.getHeadTemplate()));
        freightInfoDO.setRegionTemplate(toFreightTemplateDetailDO(freightInfo.getRegionTemplate()));
        return freightInfoDO;
    }

    /**
     *
     * @date 2021-07-20
     * @param 
     * @return 
     */
    public static FreightInfoCO doToCoObject(FreightInfoDO freightInfoDO) {

        if (freightInfoDO == null) {
            return null;
        }
        FreightInfoCO co = new FreightInfoCO();
        co.setFreightTemplateCode(freightInfoDO.getFreightTemplateCode());
        co.setHeadTemplate(toFreightTemplateVO(freightInfoDO.getHeadTemplate()));
        co.setRegionTemplate(toFreightTemplateDetailVO(freightInfoDO.getRegionTemplate()));
        return co;
    }

    /**
     *
     * @date 2021-07-20
     * @param 
     * @return 
     */
    private static FreightTemplateCO toFreightTemplateVO(FreightTemplateDO freightTemplateDO) {
        if (freightTemplateDO == null) {
            return null;
        }
        FreightTemplateCO co = new FreightTemplateCO();
        co.setTemplateId(freightTemplateDO.getTemplateId());
        co.setTemplateCode(freightTemplateDO.getTemplateCode());
        co.setTemplateName(freightTemplateDO.getTemplateName());
        co.setDeliveryFreeFlag(freightTemplateDO.getDeliveryFreeFlag());
        co.setValuationType(freightTemplateDO.getValuationType());
        co.setValuationUom(freightTemplateDO.getValuationUom());
        co.setDafaultFlag(freightTemplateDO.getDafaultFlag());
        co.setTenantId(freightTemplateDO.getTenantId());
        co.setValuationTypeMeaning(freightTemplateDO.getValuationTypeMeaning());
        co.setValuationUomMeaning(freightTemplateDO.getValuationUomMeaning());
        return co;
    }

    /**
     *
     * @date 2021-07-20
     * @param
     * @return 
     */
    private static FreightTemplateDetailCO toFreightTemplateDetailVO(FreightTemplateDetailDO freightTemplateDetailDO) {
        if (freightTemplateDetailDO == null) {
            return null;
        }
        FreightTemplateDetailCO co = new FreightTemplateDetailCO();
        co.setTemplateDetailId(freightTemplateDetailDO.getTemplateDetailId());
        co.setTransportTypeCode(freightTemplateDetailDO.getTransportTypeCode());
        co.setRegionId(freightTemplateDetailDO.getRegionId());
        co.setFirstPieceWeight(freightTemplateDetailDO.getFirstPieceWeight());
        co.setFirstPrice(freightTemplateDetailDO.getFirstPrice());
        co.setNextPieceWeight(freightTemplateDetailDO.getNextPieceWeight());
        co.setNextPrice(freightTemplateDetailDO.getNextPrice());
        co.setDefaultFlag(freightTemplateDetailDO.getDefaultFlag());
        co.setTemplateId(freightTemplateDetailDO.getTemplateId());
        co.setTenantId(freightTemplateDetailDO.getTenantId());
        co.setRegionName(freightTemplateDetailDO.getRegionName());
        co.setTransportTypeMeaning(freightTemplateDetailDO.getTransportTypeMeaning());
        return co;
    }

    /**
     *
     * @date 2021-07-20
     * @param 
     * @return 
     */
    public static FreightTemplateDO toFreightTemplateDO(FreightTemplate freightTemplate) {
        if (freightTemplate == null) {
            return null;
        }
        FreightTemplateDO freightTemplateDO = new FreightTemplateDO();
        freightTemplateDO.setTemplateId(freightTemplate.getTemplateId());
        freightTemplateDO.setTemplateCode(freightTemplate.getTemplateCode());
        freightTemplateDO.setTemplateName(freightTemplate.getTemplateName());
        freightTemplateDO.setDeliveryFreeFlag(freightTemplate.getDeliveryFreeFlag());
        freightTemplateDO.setValuationType(freightTemplate.getValuationType());
        freightTemplateDO.setValuationUom(freightTemplate.getValuationUom());
        freightTemplateDO.setDafaultFlag(freightTemplate.getDafaultFlag());
        freightTemplateDO.setTenantId(freightTemplate.getTenantId());
        freightTemplateDO.setValuationTypeMeaning(freightTemplate.getValuationTypeMeaning());
        freightTemplateDO.setValuationUomMeaning(freightTemplate.getValuationUomMeaning());
        return freightTemplateDO;
    }

    /**
     *
     * @date 2021-07-20
     * @param 
     * @return 
     */
    private static FreightTemplateDetailDO toFreightTemplateDetailDO(FreightTemplateDetail freightTemplateDetail) {
        if (freightTemplateDetail == null) {
            return null;
        }
        FreightTemplateDetailDO freightTemplateDetailDO = new FreightTemplateDetailDO();
        freightTemplateDetailDO.setTemplateDetailId(freightTemplateDetail.getTemplateDetailId());
        freightTemplateDetailDO.setTransportTypeCode(freightTemplateDetail.getTransportTypeCode());
        freightTemplateDetailDO.setRegionId(freightTemplateDetail.getRegionId());
        freightTemplateDetailDO.setFirstPieceWeight(freightTemplateDetail.getFirstPieceWeight());
        freightTemplateDetailDO.setFirstPrice(freightTemplateDetail.getFirstPrice());
        freightTemplateDetailDO.setNextPieceWeight(freightTemplateDetail.getNextPieceWeight());
        freightTemplateDetailDO.setNextPrice(freightTemplateDetail.getNextPrice());
        freightTemplateDetailDO.setDefaultFlag(freightTemplateDetail.getDefaultFlag());
        freightTemplateDetailDO.setTemplateId(freightTemplateDetail.getTemplateId());
        freightTemplateDetailDO.setTenantId(freightTemplateDetail.getTenantId());
        freightTemplateDetailDO.setRegionName(freightTemplateDetail.getRegionName());
        freightTemplateDetailDO.setTransportTypeMeaning(freightTemplateDetail.getTransportTypeMeaning());
        return freightTemplateDetailDO;
    }
}
