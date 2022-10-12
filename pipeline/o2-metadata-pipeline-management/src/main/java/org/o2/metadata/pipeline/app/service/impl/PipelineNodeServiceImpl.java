package org.o2.metadata.pipeline.app.service.impl;

import org.o2.core.helper.TransactionalHelper;
import org.o2.metadata.pipeline.app.service.PipelineNodeService;
import org.o2.metadata.pipeline.app.service.PipelineRedisService;
import org.o2.metadata.pipeline.domain.entity.PipelineNode;
import org.o2.metadata.pipeline.domain.repository.PipelineNodeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huizhen.liu@hand-china.com 2019-01-09
 */
@Service
public class PipelineNodeServiceImpl implements PipelineNodeService {

    private final PipelineNodeRepository pipelineNodeRepository;
    private final TransactionalHelper transactionalHelper;

    private final PipelineRedisService pipelineRedisService;

    public PipelineNodeServiceImpl(final PipelineNodeRepository pipelineNodeRepository,
                                   final TransactionalHelper transactionalHelper,
                                   final PipelineRedisService pipelineRedisService) {
        this.pipelineRedisService = pipelineRedisService;
        this.transactionalHelper = transactionalHelper;
        this.pipelineNodeRepository = pipelineNodeRepository;
    }

    @Override
    public int batchMerge(List<PipelineNode> pipelineNodes) {
        int existNum = 0;
        for (PipelineNode pipelineNode : pipelineNodes) {
            // 非空字段校验
            pipelineNode.validate();
            // 下个节点行为和决策类型是否重复
            boolean exist = pipelineNode.exist(pipelineNodeRepository);
            if (!exist) {
                if (pipelineNode.getId() != null) {
                    this.transactionalHelper.transactionOperation(() -> {
                        pipelineNodeRepository.updateByPrimaryKey(pipelineNode);
                        pipelineRedisService.saveRedisPipelineNodeConf(pipelineNode.getPipelineId(), pipelineNode.getTenantId());
                    });

                } else {
                    this.transactionalHelper.transactionOperation(() -> {
                        pipelineNodeRepository.insertSelective(pipelineNode);
                        pipelineRedisService.createRedisPipelineNodeConf(pipelineNode.getPipelineId(), pipelineNode.getTenantId());
                    });
                }
            } else {
                existNum++;
            }
        }
        return existNum;
    }
}
