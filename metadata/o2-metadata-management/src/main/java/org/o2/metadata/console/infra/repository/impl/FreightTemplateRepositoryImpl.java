package org.o2.metadata.console.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.console.infra.entity.FreightTemplate;
import org.o2.metadata.console.infra.repository.FreightTemplateRepository;
import org.o2.metadata.console.api.vo.FreightTemplateManagementVO;
import org.o2.metadata.console.infra.mapper.FreightTemplateMapper;
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
    public List<FreightTemplateManagementVO> listFreightTemplates(final FreightTemplate freightTemplate) {
        return freightTemplateMapper.listFreightTemplates(freightTemplate);
    }

    @Override
    public FreightTemplate selectTemplateId(final Long templateId) {
        return freightTemplateMapper.queryFreightTemplateById(templateId);
    }


    @Override
    public FreightTemplate getDefaultTemplate(Long organizationId) {
        return freightTemplateMapper.getDefaultTemplate(organizationId);
    }

    @Override
    public List<FreightTemplate> selectAllByTenantId(Long tenantId) {
        return freightTemplateMapper.selectAllByTenantId(tenantId);
    }
}
