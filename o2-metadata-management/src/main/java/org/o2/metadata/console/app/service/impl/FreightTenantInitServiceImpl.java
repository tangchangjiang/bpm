package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.initialize.domain.context.TenantInitContext;
import org.o2.metadata.console.app.service.FreightTenantInitService;
import org.o2.metadata.console.infra.constant.TenantInitConstants;
import org.o2.metadata.console.infra.entity.FreightTemplate;
import org.o2.metadata.console.infra.redis.FreightRedis;
import org.o2.metadata.console.infra.repository.FreightTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FreightTenantInitServiceImpl implements FreightTenantInitService {

    private final FreightTemplateRepository freightTemplateRepository;
    private final FreightRedis redis;

    public FreightTenantInitServiceImpl(FreightTemplateRepository freightTemplateRepository, FreightRedis redis) {
        this.freightTemplateRepository = freightTemplateRepository;
        this.redis = redis;
    }


    @Override
    public void tenantInitializeBusiness(TenantInitContext context) {
        // 1. 查询源租户
       List<FreightTemplate> sourceFreightTemplate = freightTemplateRepository.selectByCondition(Condition.builder(FreightTemplate.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(FreightTemplate.FIELD_TENANT_ID, context.getSourceTenantId())
                        .andEqualTo(FreightTemplate.FIELD_TEMPLATE_CODE, context.getParamMap().get(TenantInitConstants.InitBusinessParam.BUSINESS_FREIGHT)))
                .build());
        if (CollectionUtils.isEmpty(sourceFreightTemplate)) {
            log.info("Business: FreightTemplate is empty.");
            return;
        }
        // 2. 查询目标租户
        List<FreightTemplate> targetFreightTemplate = freightTemplateRepository.selectByCondition(Condition.builder(FreightTemplate.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(FreightTemplate.FIELD_TENANT_ID, context.getTargetTenantId())
                        .andEqualTo(FreightTemplate.FIELD_TEMPLATE_CODE, context.getParamMap().get(TenantInitConstants.InitBusinessParam.BUSINESS_FREIGHT)))
                .build());

       if (CollectionUtils.isNotEmpty(targetFreightTemplate)) {
           freightTemplateRepository.batchDeleteByPrimaryKey(targetFreightTemplate);
       }
        for (FreightTemplate freightTemplate : sourceFreightTemplate) {
            freightTemplate.setTemplateId(null);
            freightTemplate.setTenantId(context.getTargetTenantId());
        }
        freightTemplateRepository.batchInsert(sourceFreightTemplate);
        redis.batchUpdateRedis(sourceFreightTemplate,new ArrayList<>(),context.getTargetTenantId());
    }
}
