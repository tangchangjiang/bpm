package org.o2.metadata.pipeline.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.pipeline.app.service.PipelineNodeTenantInitService;
import org.o2.metadata.pipeline.domain.entity.PipelineAction;
import org.o2.metadata.pipeline.domain.entity.PipelineNode;
import org.o2.metadata.pipeline.domain.repository.PipelineNodeRepository;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * description
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/20 11:10
 */
@Slf4j
@Service
public class PipelineNodeTenantInitServiceImpl implements PipelineNodeTenantInitService {

    private final PipelineNodeRepository pipelineNodeRepository;

    public PipelineNodeTenantInitServiceImpl(PipelineNodeRepository pipelineNodeRepository) {
        this.pipelineNodeRepository = pipelineNodeRepository;
    }


    @Override
    public void tenantInitialize(long sourceTenantId, Long tenantId) {
        log.info("initializePipelineNode start, tenantId[{}]", tenantId);
        // 1. 查询平台级租户
        final List<PipelineNode> platformPipelineNodes = pipelineNodeRepository.selectByCondition(Condition.builder(PipelineNode.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(PipelineAction.FIELD_TENANT_ID, sourceTenantId))
                .build());

        if (CollectionUtils.isEmpty(platformPipelineNodes)) {
            log.info("platformPipelineNodes is empty.");
            return;
        }

        // 2. 查询目标租户是否存在数据
        final List<PipelineNode> targetPipelineNodes = pipelineNodeRepository.selectByCondition(Condition.builder(PipelineNode.class)
                .andWhere(Sqls.custom().andEqualTo(PipelineAction.FIELD_TENANT_ID, tenantId))
                .build());

        if (CollectionUtils.isNotEmpty(targetPipelineNodes)) {
            // 删除目标租户原数据
            pipelineNodeRepository.batchDeleteByPrimaryKey(targetPipelineNodes);
        }

        // 3. 插入目标租户
        platformPipelineNodes.forEach(pipelineNode -> {
            pipelineNode.setId(null);
            pipelineNode.setTenantId(tenantId);
        });
        pipelineNodeRepository.batchInsert(platformPipelineNodes);

        log.info("initializePipelineNode finish, tenantId[{}]", tenantId);

    }
}
