package org.o2.metadata.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.domain.entity.FreightTemplate;
import org.o2.metadata.domain.repository.FreightTemplateRepository;
import org.o2.metadata.domain.vo.FreightTemplateVO;
import org.o2.metadata.infra.mapper.FreightTemplateMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 运费模板资源库实现
 *
 * @author peng.xu@hand-china.com 2019/5/15
 */
@Component
public class FreightTemplateRepositoryImpl extends BaseRepositoryImpl<FreightTemplate> implements FreightTemplateRepository {
    private FreightTemplateMapper freightTemplateMapper;

    public FreightTemplateRepositoryImpl(FreightTemplateMapper freightTemplateMapper) {
        this.freightTemplateMapper = freightTemplateMapper;
    }

    @Override
    public List<FreightTemplateVO> listFreightTemplates(final FreightTemplate freightTemplate) {
        return freightTemplateMapper.listFreightTemplates(freightTemplate);
    }

    @Override
    public FreightTemplate selectyTemplateId(final Long templateId) {
        return freightTemplateMapper.queryFreightTemplateById(templateId);
    }

    @Override
    public boolean isFreightTemplateRelatePro(FreightTemplate freightTemplate) {
        return freightTemplateMapper.freightTemplateRelateProCount(freightTemplate) > 0;
    }
}
