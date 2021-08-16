package org.o2.metadata.infra.convertor;

import org.o2.metadata.api.vo.FreightInfoVO;
import org.o2.metadata.api.vo.FreightTemplateDetailVO;
import org.o2.metadata.api.vo.FreightTemplateVO;
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
     *
     * @date 2021-07-20
     * @param 
     * @return 
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
     *
     * @date 2021-07-20
     * @param
     * @return 
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
     *
     * @date 2021-07-20
     * @param 
     * @return 
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
