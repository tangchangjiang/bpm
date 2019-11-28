package org.o2.metadata.app.service.impl;

import org.o2.boot.metadata.app.bo.FreightBO;
import org.o2.boot.metadata.app.bo.FreightDetailBO;
import org.o2.boot.metadata.app.bo.FreightTemplateBO;
import org.o2.metadata.domain.entity.Carrier;
import org.o2.metadata.domain.entity.FreightTemplate;
import org.o2.metadata.domain.entity.FreightTemplateDetail;
import org.o2.metadata.domain.entity.Region;
import org.o2.metadata.domain.repository.CarrierRepository;
import org.o2.metadata.domain.repository.FreightTemplateRepository;
import org.o2.metadata.domain.repository.RegionRepository;
import org.o2.metadata.domain.vo.FreightTemplateVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 运费模板redis缓存操作抽象类
 *
 * @author peng.xu@hand-china.com 2019-06-19
 */
public abstract class AbstractFreightCacheOperation {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractFreightCacheOperation.class);

    protected CarrierRepository carrierRepository;

    protected RegionRepository regionRepository;

    protected FreightTemplateRepository freightTemplateRepository;

    /**
     * 将运费模板明细实体列表，转换为运费模板明细缓存操作对象列表
     *
     * @param freightTemplateDetailList 运费模板明细实体列表
     * @return 运费模板明细缓存操作对象列表
     */
    protected List<FreightDetailBO> convertToFreightDetail(List<FreightTemplateDetail> freightTemplateDetailList) {
        List<FreightDetailBO> freightDetailList = new ArrayList<>();

        for (FreightTemplateDetail detail : freightTemplateDetailList) {
            FreightDetailBO bo = new FreightDetailBO();

            if (detail.getCarrierId() != null) {
                Carrier carrier = carrierRepository.selectByPrimaryKey(detail.getCarrierId());
                bo.setCarrierCode(carrier != null ? carrier.getCarrierCode() : null);
            }
            if (detail.getRegionId() != null) {
                Region region = regionRepository.selectByPrimaryKey(detail.getRegionId());
                bo.setRegionCode(region != null ? region.getRegionCode() : null);
            }
            if (detail.getTemplateId() != null) {
                FreightTemplate template = freightTemplateRepository.selectByPrimaryKey(detail.getTemplateId());
                bo.setTemplateCode(template != null ? template.getTemplateCode() : null);
            }
            bo.setIsDefault(detail.getIsDefault());
            bo.setTemplateDetailId(detail.getTemplateDetailId());
            bo.setFirstPieceWeight(detail.getFirstPieceWeight());
            bo.setFirstPrice(detail.getFirstPrice());
            bo.setNextPieceWeight(detail.getNextPieceWeight());
            bo.setNextPrice(detail.getNextPrice());

            freightDetailList.add(bo);
        }

        LOG.info("freightDetailBOList.size()={}", freightDetailList.size());

        return freightDetailList;
    }

    /**
     * 将运费模板实体列表(不包含运费模板明细信息)，转换为运费模板缓存操作对象列表
     *
     * @param freightTemplate 运费模板实体列表(不包含运费模板明细信息)
     * @return 运费模板缓存操作对象列表
     */
    protected FreightBO convertToFreight(FreightTemplate freightTemplate) {
        FreightBO freight = new FreightBO();
        freight.setTemplateId(freightTemplate.getTemplateId());
        if (freightTemplate.getTemplateId() != null) {
            FreightTemplate template = freightTemplateRepository.selectByPrimaryKey(freightTemplate.getTemplateId());
            freight.setTemplateCode(template != null ? template.getTemplateCode() : null);
        }
        freight.setTemplateName(freightTemplate.getTemplateName());
        freight.setValuationTypeCode(freightTemplate.getValuationTypeCode());
        freight.setValuationUomCode(freightTemplate.getValuationUomCode());
        freight.setIsFree(freightTemplate.getIsFree());

        return freight;
    }

    /**
     * 将运费模板实体列表(包含运费模板明细信息)，转换为运费模板缓存操作对象列表
     *
     * @param freightTemplateVO 运费模板实体列表(包含运费模板明细信息)
     * @return 运费模板缓存操作对象列表
     */
    protected FreightTemplateBO convertToFreightTemplate(FreightTemplateVO freightTemplateVO) {
        FreightBO freight = convertToFreight(freightTemplateVO);

        List<FreightTemplateDetail> list = new ArrayList<>();
        if (freightTemplateVO.getDefaultFreightTemplateDetails() != null) {
            list.addAll(freightTemplateVO.getDefaultFreightTemplateDetails());
        }
        if (freightTemplateVO.getRegionFreightTemplateDetails() != null) {
            list.addAll(freightTemplateVO.getRegionFreightTemplateDetails());
        }
        List<FreightDetailBO> freightDetailList = convertToFreightDetail(list);

        FreightTemplateBO template = new FreightTemplateBO();
        template.setFreightBO(freight);
        template.setFreightDetailBOList(freightDetailList);

        LOG.info("freightTemplateBO.getFreightDetailBOList().size()={}", template.getFreightDetailBOList().size());

        return template;
    }
}
