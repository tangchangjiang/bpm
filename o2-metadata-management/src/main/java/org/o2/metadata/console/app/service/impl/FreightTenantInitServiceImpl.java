package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.console.app.service.FreightTenantInitService;
import org.o2.metadata.console.infra.constant.TenantInitConstants;
import org.o2.metadata.console.infra.entity.Catalog;
import org.o2.metadata.console.infra.entity.FreightTemplate;
import org.o2.metadata.console.infra.repository.FreightTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FreightTenantInitServiceImpl implements FreightTenantInitService {

    private final FreightTemplateRepository freightTemplateRepository;

    public FreightTenantInitServiceImpl(FreightTemplateRepository freightTemplateRepository) {
        this.freightTemplateRepository = freightTemplateRepository;
    }


    @Override
    public void tenantInitializeBusiness(long sourceTenantId, Long targetTenantId) {
        // 1. 查询源租户
       List<FreightTemplate> sourceFreightTemplate = freightTemplateRepository.selectByCondition(Condition.builder(Catalog.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(FreightTemplate.FIELD_TENANT_ID, sourceTenantId)
                        .andEqualTo(FreightTemplate.FIELD_TEMPLATE_CODE, TenantInitConstants.FreightBusiness.FREIGHT_CODE))
                .build());
        if (CollectionUtils.isEmpty(sourceFreightTemplate)) {
            log.info("Business: FreightTemplate is empty.");
            return;
        }
        // 2. 查询目标租户
        List<FreightTemplate> targetFreightTemplate = freightTemplateRepository.selectByCondition(Condition.builder(Catalog.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(FreightTemplate.FIELD_TENANT_ID, targetTenantId)
                        .andEqualTo(FreightTemplate.FIELD_TEMPLATE_CODE, TenantInitConstants.FreightBusiness.FREIGHT_CODE))
                .build());

       if (CollectionUtils.isEmpty(targetFreightTemplate)) {
           return;
       }
        freightTemplateRepository.batchInsert(sourceFreightTemplate);
    }
}
