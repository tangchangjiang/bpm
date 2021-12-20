package org.o2.metadata.pipeline.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.pipeline.app.service.PipelineActionTenantInitService;
import org.o2.metadata.pipeline.domain.entity.PipelineAction;
import org.o2.metadata.pipeline.domain.repository.PipelineActionRepository;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * description
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/18 13:29
 */
@Slf4j
@Service
public class PipelineActionTenantInitServiceImpl implements PipelineActionTenantInitService {

    private final PipelineActionRepository pipelineActionRepository;

    public PipelineActionTenantInitServiceImpl(PipelineActionRepository pipelineActionRepository) {
        this.pipelineActionRepository = pipelineActionRepository;
    }


    @Override
    public void tenantInitialize(Long tenantId) {
        log.info("initializePipelineAction start, tenantId[{}]", tenantId);
        // 1. 查询平台级租户
        final List<PipelineAction> platformPipelineActions = pipelineActionRepository.selectByCondition(Condition.builder(PipelineAction.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(PipelineAction.FIELD_TENANT_ID, BaseConstants.DEFAULT_TENANT_ID))
                .build());

        if (CollectionUtils.isEmpty(platformPipelineActions)) {
            log.info("platformPipelineActions is empty.");
            return;
        }

        // 2. 查询目标租户是否存在数据
        final List<PipelineAction> targetPipelineActions = pipelineActionRepository.selectByCondition(Condition.builder(PipelineAction.class)
                .andWhere(Sqls.custom().andEqualTo(PipelineAction.FIELD_TENANT_ID, tenantId))
                .build());

        if (CollectionUtils.isNotEmpty(targetPipelineActions)) {
            // 删除目标租户原数据
            pipelineActionRepository.batchDeleteByPrimaryKey(targetPipelineActions);
        }

        // 3. 插入目标租户
        platformPipelineActions.forEach(pipelineAction -> {
            pipelineAction.setId(null);
            pipelineAction.setTenantId(tenantId);
        });
        pipelineActionRepository.batchInsert(platformPipelineActions);

        log.info("initializePipelineAction finish, tenantId[{}]", tenantId);

    }
}
