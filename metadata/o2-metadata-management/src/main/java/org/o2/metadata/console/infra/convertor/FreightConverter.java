package org.o2.metadata.console.infra.convertor;


import org.o2.metadata.console.api.co.FreightInfoCO;
import org.o2.metadata.console.api.co.FreightTemplateCO;
import org.o2.metadata.console.api.co.FreightTemplateDetailCO;
import org.o2.metadata.console.app.bo.FreightBO;
import org.o2.metadata.console.app.bo.FreightDetailBO;
import org.o2.metadata.console.infra.entity.FreightInfo;
import org.o2.metadata.console.infra.entity.FreightTemplate;
import org.o2.metadata.console.infra.entity.FreightTemplateDetail;
import org.o2.metadata.domain.freight.domain.FreightInfoDO;
import org.o2.metadata.domain.freight.domain.FreightTemplateDO;
import org.o2.metadata.domain.freight.domain.FreightTemplateDetailDO;


/**
 *
 * 运费模版转换
 *
 * @author yipeng.zhu@hand-china.com 2021-07-19
 **/
public class FreightConverter {
    private FreightConverter() {
    }

    /**
     * po转do
     * @param freightInfo 运费模版头
     * @return  do 运费模版头
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
     * do转CO
     * @param freightInfoDO 运费模版头
     * @return  CO 运费模版头
     */
    public static FreightInfoCO doToCoObject(FreightInfoDO freightInfoDO) {

        if (freightInfoDO == null) {
            return null;
        }
        FreightInfoCO co = new FreightInfoCO();
        co.setFreightTemplateCode(freightInfoDO.getFreightTemplateCode());
        co.setHeadTemplate(toFreightTemplateCo(freightInfoDO.getHeadTemplate()));
        co.setRegionTemplate(toFreightTemplateDetailCo(freightInfoDO.getRegionTemplate()));
        return co;
    }

    /**
     * do转CO
     * @param freightTemplateDO 运费模详情
     * @return  vo 运费模版详情
     */
    public static FreightTemplateCO toFreightTemplateCo(FreightTemplateDO freightTemplateDO) {
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
        return co;
    }
    /**
     * do转CO
     * @param freightTemplateDetailDO 运费模详情
     * @return  vo 运费模版详情
     */
    private static FreightTemplateDetailCO toFreightTemplateDetailCo(FreightTemplateDetailDO freightTemplateDetailDO) {
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
     * 运费模版头 po转do
     * @param freightTemplate 运费模版头
     * @return do
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
     * 运费模详情 po转do
     * @param freightTemplateDetail 运费模详情
     * @return do
     */
    private static FreightTemplateDetailDO toFreightTemplateDetailDO(FreightTemplateDetail freightTemplateDetail) {
        if (freightTemplateDetail == null) {
            return null;
        }
        FreightTemplateDetailDO freightTemplateDetailDO = new FreightTemplateDetailDO();
        freightTemplateDetailDO.setTemplateDetailId(freightTemplateDetail.getTemplateDetailId());
        freightTemplateDetailDO.setTransportTypeCode(freightTemplateDetail.getTransportTypeCode());
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

    /**
     * 运费模版头 po转CO
     * @param freightTemplate 运费模版头
     * @return do
     */
    public static FreightTemplateCO poToCoObject(FreightTemplate freightTemplate) {

        if (freightTemplate == null) {
            return null;
        }
        FreightTemplateCO co = new FreightTemplateCO();
        co.setTemplateId(freightTemplate.getTemplateId());
        co.setTemplateCode(freightTemplate.getTemplateCode());
        co.setTemplateName(freightTemplate.getTemplateName());
        co.setDeliveryFreeFlag(freightTemplate.getDeliveryFreeFlag());
        co.setValuationType(freightTemplate.getValuationType());
        co.setValuationUom(freightTemplate.getValuationUom());
        co.setDafaultFlag(freightTemplate.getDafaultFlag());
        co.setTenantId(freightTemplate.getTenantId());
        return co;
    }

    /**
     * po -> bo
     * @param  freightTemplate 运费模版
     * @return FreightDetailBO
     */
    public static FreightBO poToBoObject(FreightTemplate freightTemplate){

        if (freightTemplate == null) {
            return null;
        }
        FreightBO freightBO = new FreightBO();
        freightBO.setTemplateId(freightTemplate.getTemplateId());
        freightBO.setTemplateCode(freightTemplate.getTemplateCode());
        freightBO.setTemplateName(freightTemplate.getTemplateName());
        freightBO.setDeliveryFreeFlag(freightTemplate.getDeliveryFreeFlag());
        freightBO.setValuationType(freightTemplate.getValuationType());
        freightBO.setValuationUom(freightTemplate.getValuationUom());
        freightBO.setTenantId(freightTemplate.getTenantId());
        freightBO.setDafaultFlag(freightTemplate.getDafaultFlag());
        return freightBO;
    }

    /**
     * po -> bo
     * @param  freightTemplateDetail 运费模版详情
     * @return FreightDetailBO
     */
    public static FreightDetailBO poToBoObject(FreightTemplateDetail freightTemplateDetail){

        if (freightTemplateDetail == null) {
            return null;
        }
        FreightDetailBO freightDetailBO = new FreightDetailBO();
        freightDetailBO.setTemplateDetailId(freightTemplateDetail.getTemplateDetailId());
        freightDetailBO.setRegionCode(freightTemplateDetail.getRegionCode());
        freightDetailBO.setDefaultFlag(freightTemplateDetail.getDefaultFlag());
        freightDetailBO.setTemplateCode(freightTemplateDetail.getTemplateCode());
        freightDetailBO.setTenantId(freightTemplateDetail.getTenantId());
        freightDetailBO.setFirstPieceWeight(freightTemplateDetail.getFirstPieceWeight());
        freightDetailBO.setFirstPrice(freightTemplateDetail.getFirstPrice());
        freightDetailBO.setNextPieceWeight(freightTemplateDetail.getNextPieceWeight());
        freightDetailBO.setNextPrice(freightTemplateDetail.getNextPrice());
        return freightDetailBO;
    }
}
