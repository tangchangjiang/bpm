package org.o2.metadata.pipeline.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.pipeline.app.service.PipelineTenantInitCoreService;
import org.o2.metadata.pipeline.domain.entity.Pipeline;
import org.o2.metadata.pipeline.domain.entity.PipelineAction;
import org.o2.metadata.pipeline.domain.entity.PipelineNode;
import org.o2.metadata.pipeline.domain.repository.PipelineActionRepository;
import org.o2.metadata.pipeline.domain.repository.PipelineNodeRepository;
import org.o2.metadata.pipeline.domain.repository.PipelineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * description
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/18 13:26
 */
@Slf4j
@Service
public class PipelineTenantInitCoreServiceImpl implements PipelineTenantInitCoreService {

    private final PipelineRepository pipelineRepository;

    private final PipelineNodeRepository pipelineNodeRepository;

    private final PipelineActionRepository pipelineActionRepository;

    public PipelineTenantInitCoreServiceImpl(PipelineRepository pipelineRepository,
                                             PipelineNodeRepository pipelineNodeRepository,
                                             PipelineActionRepository pipelineActionRepository) {
        this.pipelineRepository = pipelineRepository;
        this.pipelineNodeRepository = pipelineNodeRepository;
        this.pipelineActionRepository = pipelineActionRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tenantInitialize(long sourceTenantId, Long targetTenantId) {
        log.info("initializePipeline start, targetTenantId[{}]", targetTenantId);
        // 1. 查询平台级租户
        final List<Pipeline> platformPipelines = pipelineRepository.selectByCondition(Condition.builder(Pipeline.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(Pipeline.FIELD_TENANT_ID, sourceTenantId)
                        .andEqualTo(Pipeline.FIELD_ACTIVE_FLAG, BaseConstants.Flag.YES))
                .build());

        if (CollectionUtils.isEmpty(platformPipelines)) {
            log.info("pipeline is empty.");
            return;
        }

        // 1.2 查询平台级 所有行为定义
        final List<PipelineAction> platformPipelineActions = pipelineActionRepository.selectByCondition(Condition.builder(PipelineAction.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(PipelineAction.FIELD_TENANT_ID, sourceTenantId))
                .build());

        // key: newId; value:oldId
        final HashMap<Long, Long> oldNewPipeActionMap = new HashMap<>(platformPipelineActions.size());

        // 1.5 查询平台级 关联的流程器节点
        final List<PipelineNode> platformPipelineNodes = pipelineNodeRepository.selectByCondition(Condition.builder(PipelineNode.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(PipelineNode.FIELD_TENANT_ID, sourceTenantId)
                        .andIn(PipelineNode.FIELD_PIPELINE_ID, platformPipelines.stream().map(Pipeline::getId).collect(Collectors.toList())))
                .build());

        final Map<Long, List<PipelineNode>> pipelineNodeMap = platformPipelineNodes.stream().collect(Collectors.groupingBy(PipelineNode::getPipelineId));


        // 2. 查询目标租户是否存在数据 (需要提前删除已存在的旧数据)
        final List<Pipeline> targetPipelines = pipelineRepository.selectByCondition(Condition.builder(Pipeline.class)
                .andWhere(Sqls.custom().andEqualTo(Pipeline.FIELD_TENANT_ID, targetTenantId))
                .build());

        if (CollectionUtils.isNotEmpty(targetPipelines)) {
            // 删除目标租户流程器定义
            pipelineRepository.batchDeleteByPrimaryKey(targetPipelines);
        }


        final List<PipelineAction> targetPipelineActions = pipelineActionRepository.selectByCondition(Condition.builder(PipelineAction.class)
                .andWhere(Sqls.custom().andEqualTo(PipelineAction.FIELD_TENANT_ID, targetTenantId))
                .build());

        if (CollectionUtils.isNotEmpty(targetPipelineActions)) {
            // 删除目标租户行为定义
            pipelineActionRepository.batchDeleteByPrimaryKey(targetPipelineActions);
        }


        // 2.2 目标租户的流程器行为定义先插入
        if (CollectionUtils.isNotEmpty(platformPipelineActions)) {
            platformPipelineActions.forEach(pipelineAction -> {
                final Long oldActionId = pipelineAction.getId();
                pipelineAction.setId(null);
                pipelineAction.setTenantId(targetTenantId);
                pipelineActionRepository.insert(pipelineAction);
                oldNewPipeActionMap.put(oldActionId, pipelineAction.getId());
            });
        }


        // 2.5 查询目标租户是否存在旧的流程器节点数据
        final List<PipelineNode> targetPipelineNodes = pipelineNodeRepository.selectByCondition(Condition.builder(PipelineNode.class)
                .andWhere(Sqls.custom().andEqualTo(PipelineAction.FIELD_TENANT_ID, targetTenantId))
                .build());

        if (CollectionUtils.isNotEmpty(targetPipelineNodes)) {
            // 删除目标租户原数据
            pipelineNodeRepository.batchDeleteByPrimaryKey(targetPipelineNodes);
        }

        // 3. 插入目标租户

        platformPipelines.forEach(pipeline -> {
            final List<PipelineNode> pipelineNodes = pipelineNodeMap.get(pipeline.getId());
            pipeline.setId(null);
            pipeline.setTenantId(targetTenantId);

            // 关联动作
            pipeline.setStartAction(oldNewPipeActionMap.get(pipeline.getStartAction()));
            pipeline.setEndAction(oldNewPipeActionMap.get(pipeline.getEndAction()));

            // 3.1 初始化关联的流程器节点
            pipelineRepository.insert(pipeline);


            if (CollectionUtils.isNotEmpty(pipelineNodes)) {
                pipelineNodes.forEach(pipelineNode -> {
                    pipelineNode.setId(null);
                    pipelineNode.setTenantId(targetTenantId);
                    // 插入后主键
                    pipelineNode.setPipelineId(pipeline.getId());
                    pipelineNode.setCurAction(oldNewPipeActionMap.get(pipelineNode.getCurAction()));
                    pipelineNode.setNextAction(oldNewPipeActionMap.get(pipelineNode.getNextAction()));

                });
                pipelineNodeRepository.batchInsert(pipelineNodes);
            }
        });
    }
}
