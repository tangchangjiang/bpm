package org.o2.metadata.console.infra.convertor;


import org.o2.metadata.console.api.vo.FreightInfoVO;
import org.o2.metadata.console.api.vo.FreightTemplateDetailVO;
import org.o2.metadata.console.api.vo.FreightTemplateVO;
import org.o2.metadata.console.app.bo.FreightBO;
import org.o2.metadata.console.app.bo.FreightDetailBO;
import org.o2.metadata.console.app.bo.FreightTemplateBO;
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
     * do转vo
     * @param freightInfoDO 运费模版头
     * @return  vo 运费模版头
     */
    public static FreightInfoVO doToVoObject(FreightInfoDO freightInfoDO) {

        if (freightInfoDO == null) {
            return null;
        }
        FreightInfoVO freightInfoVO = new FreightInfoVO();
        freightInfoVO.setFreightTemplateCode(freightInfoDO.getFreightTemplateCode());
        freightInfoVO.setHeadTemplate(toFreightTemplateVO(freightInfoDO.getHeadTemplate()));
        freightInfoVO.setRegionTemplate(toFreightTemplateDetailVO(freightInfoDO.getRegionTemplate()));
        return freightInfoVO;
    }

    /**
     * do转vo
     * @param freightTemplateDO 运费模详情
     * @return  vo 运费模版详情
     */
    private static FreightTemplateVO toFreightTemplateVO(FreightTemplateDO freightTemplateDO) {
        if (freightTemplateDO == null) {
            return null;
        }
        FreightTemplateVO freightTemplateVO = new FreightTemplateVO();
        freightTemplateVO.setTemplateId(freightTemplateDO.getTemplateId());
        freightTemplateVO.setTemplateCode(freightTemplateDO.getTemplateCode());
        freightTemplateVO.setTemplateName(freightTemplateDO.getTemplateName());
        freightTemplateVO.setDeliveryFreeFlag(freightTemplateDO.getDeliveryFreeFlag());
        freightTemplateVO.setValuationType(freightTemplateDO.getValuationType());
        freightTemplateVO.setValuationUom(freightTemplateDO.getValuationUom());
        freightTemplateVO.setDafaultFlag(freightTemplateDO.getDafaultFlag());
        freightTemplateVO.setTenantId(freightTemplateDO.getTenantId());
        freightTemplateVO.setValuationTypeMeaning(freightTemplateDO.getValuationTypeMeaning());
        freightTemplateVO.setValuationUomMeaning(freightTemplateDO.getValuationUomMeaning());
        return freightTemplateVO;
    }
    /**
     * do转vo
     * @param freightTemplateDetailDO 运费模详情
     * @return  vo 运费模版详情
     */
    private static FreightTemplateDetailVO toFreightTemplateDetailVO(FreightTemplateDetailDO freightTemplateDetailDO) {
        if (freightTemplateDetailDO == null) {
            return null;
        }
        FreightTemplateDetailVO freightTemplateDetailVO = new FreightTemplateDetailVO();
        freightTemplateDetailVO.setTemplateDetailId(freightTemplateDetailDO.getTemplateDetailId());
        freightTemplateDetailVO.setTransportTypeCode(freightTemplateDetailDO.getTransportTypeCode());
        freightTemplateDetailVO.setRegionId(freightTemplateDetailDO.getRegionId());
        freightTemplateDetailVO.setFirstPieceWeight(freightTemplateDetailDO.getFirstPieceWeight());
        freightTemplateDetailVO.setFirstPrice(freightTemplateDetailDO.getFirstPrice());
        freightTemplateDetailVO.setNextPieceWeight(freightTemplateDetailDO.getNextPieceWeight());
        freightTemplateDetailVO.setNextPrice(freightTemplateDetailDO.getNextPrice());
        freightTemplateDetailVO.setDefaultFlag(freightTemplateDetailDO.getDefaultFlag());
        freightTemplateDetailVO.setTemplateId(freightTemplateDetailDO.getTemplateId());
        freightTemplateDetailVO.setTenantId(freightTemplateDetailDO.getTenantId());
        freightTemplateDetailVO.setRegionName(freightTemplateDetailDO.getRegionName());
        freightTemplateDetailVO.setTransportTypeMeaning(freightTemplateDetailDO.getTransportTypeMeaning());
        return freightTemplateDetailVO;
    }

    /**
     * 运费模版头 po转do
     * @param freightTemplate 运费模版头
     * @return do
     */
    private static FreightTemplateDO toFreightTemplateDO(FreightTemplate freightTemplate) {
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

    /**
     * 运费模版头 po转VO
     * @param freightTemplate 运费模版头
     * @return do
     */
    public static FreightTemplateVO poToVoObject(FreightTemplate freightTemplate) {

        if (freightTemplate == null) {
            return null;
        }
        FreightTemplateVO freightTemplateVO = new FreightTemplateVO();
        freightTemplateVO.setTemplateId(freightTemplate.getTemplateId());
        freightTemplateVO.setTemplateCode(freightTemplate.getTemplateCode());
        freightTemplateVO.setTemplateName(freightTemplate.getTemplateName());
        freightTemplateVO.setDeliveryFreeFlag(freightTemplate.getDeliveryFreeFlag());
        freightTemplateVO.setValuationType(freightTemplate.getValuationType());
        freightTemplateVO.setValuationUom(freightTemplate.getValuationUom());
        freightTemplateVO.setDafaultFlag(freightTemplate.getDafaultFlag());
        freightTemplateVO.setTenantId(freightTemplate.getTenantId());
        freightTemplateVO.setValuationTypeMeaning(freightTemplate.getValuationTypeMeaning());
        freightTemplateVO.setValuationUomMeaning(freightTemplate.getValuationUomMeaning());
        return freightTemplateVO;
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
