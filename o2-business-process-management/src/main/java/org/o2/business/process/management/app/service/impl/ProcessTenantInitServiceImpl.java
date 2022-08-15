package org.o2.business.process.management.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.business.process.management.app.service.BusinessProcessRedisService;
import org.o2.business.process.management.app.service.ProcessTenantInitService;
import org.o2.business.process.management.domain.entity.BizNodeParameter;
import org.o2.business.process.management.domain.entity.BusinessNode;
import org.o2.business.process.management.domain.entity.BusinessProcess;
import org.o2.business.process.management.domain.repository.BizNodeParameterRepository;
import org.o2.business.process.management.domain.repository.BusinessNodeRepository;
import org.o2.business.process.management.domain.repository.BusinessProcessRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/8/15 16:57
 */
@Service
@Slf4j
public class ProcessTenantInitServiceImpl implements ProcessTenantInitService {

    private final BusinessProcessRepository businessProcessRepository;
    private final BusinessNodeRepository businessNodeRepository;
    private final BizNodeParameterRepository bizNodeParameterRepository;
    private final BusinessProcessRedisService businessProcessRedisService;

    public ProcessTenantInitServiceImpl(BusinessProcessRepository businessProcessRepository, BusinessNodeRepository businessNodeRepository, BizNodeParameterRepository bizNodeParameterRepository, BusinessProcessRedisService businessProcessRedisService) {
        this.businessProcessRepository = businessProcessRepository;
        this.businessNodeRepository = businessNodeRepository;
        this.bizNodeParameterRepository = bizNodeParameterRepository;
        this.businessProcessRedisService = businessProcessRedisService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tenantInitialize(Long sourceTenantId, Long targetTenantId) {
        log.info("initializePipeline start, targetTenantId[{}]", targetTenantId);
        // 1. 查询平台级租户
        final List<BusinessProcess> platformProcessList = businessProcessRepository.selectByCondition(Condition.builder(BusinessProcess.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(BusinessProcess.FIELD_TENANT_ID, sourceTenantId)
                        .andEqualTo(BusinessProcess.FIELD_ENABLED_FLAG, BaseConstants.Flag.YES))
                .build());

        if (CollectionUtils.isEmpty(platformProcessList)) {
            log.info("pipeline is empty.");
            return;
        }

        List<BusinessNode> platformNodes = businessNodeRepository.selectByCondition(Condition.builder(BusinessNode.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(BusinessNode.FIELD_TENANT_ID, sourceTenantId)
                        .andEqualTo(BusinessNode.FIELD_ENABLED_FLAG, BaseConstants.Flag.YES))
                .build());

        List<BizNodeParameter> platformParameters = bizNodeParameterRepository.selectByCondition(Condition.builder(BizNodeParameter.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(BizNodeParameter.FIELD_TENANT_ID, sourceTenantId)
                        .andEqualTo(BizNodeParameter.FIELD_ENABLED_FLAG, BaseConstants.Flag.YES))
                .build());

        // 同步数据库信息
        BusinessProcess businessProcess = new BusinessProcess();
        businessProcess.setTenantId(targetTenantId);
        BusinessNode businessNode = new BusinessNode();
        businessNode.setTenantId(targetTenantId);
        BizNodeParameter bizNodeParameter = new BizNodeParameter();
        bizNodeParameter.setTenantId(targetTenantId);
        businessProcessRepository.delete(businessProcess);
        businessNodeRepository.delete(businessNode);
        bizNodeParameterRepository.delete(bizNodeParameter);
        platformProcessList.forEach(p -> p.setTenantId(targetTenantId));
        platformNodes.forEach(n -> n.setTenantId(targetTenantId));
        platformParameters.forEach(p -> p.setTenantId(targetTenantId));
        businessProcessRepository.batchInsert(platformProcessList);
        businessNodeRepository.batchInsert(platformNodes);
        bizNodeParameterRepository.batchInsert(platformParameters);
        // 同步redis数据
        businessProcessRedisService.batchUpdateProcessConfig(platformProcessList, targetTenantId);
    }
}
