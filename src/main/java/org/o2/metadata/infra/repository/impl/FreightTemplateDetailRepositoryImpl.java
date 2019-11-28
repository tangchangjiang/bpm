package org.o2.metadata.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.domain.entity.FreightTemplateDetail;
import org.o2.metadata.domain.repository.FreightTemplateDetailRepository;
import org.o2.metadata.infra.mapper.FreightTemplateDetailMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 运费模板明细资源库实现
 *
 * @author peng.xu@hand-china.com 2019/5/17
 */
@Component
public class FreightTemplateDetailRepositoryImpl extends BaseRepositoryImpl<FreightTemplateDetail> implements FreightTemplateDetailRepository {
    private FreightTemplateDetailMapper freightTemplateDetailMapper;

    public FreightTemplateDetailRepositoryImpl(FreightTemplateDetailMapper freightTemplateDetailMapper) {
        this.freightTemplateDetailMapper = freightTemplateDetailMapper;
    }

    @Override
    public List<FreightTemplateDetail> queryDefaultFreightTemplateDetail(final Long templateId) {
        return freightTemplateDetailMapper.queryDefaultFreightTemplateDetails(templateId);
    }

    @Override
    public List<FreightTemplateDetail> queryRegionFreightTemplateDetail(final Long templateId) {
        return freightTemplateDetailMapper.queryRegionFreightTemplateDetails(templateId);
    }

    @Override
    public List<FreightTemplateDetail> queryFreightTemplateDetailByTemplateId(final Long templateId) {
        return freightTemplateDetailMapper.queryFreightTemplateDetailByTemplateId(templateId);
    }

    @Override
    public List<FreightTemplateDetail> queryOtherDefaultFreightTemplateDetail(FreightTemplateDetail freightTemplateDetail) {
        return freightTemplateDetailMapper.queryOtherDefaultFreightTemplateDetail(freightTemplateDetail);
    }
}
