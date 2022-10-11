package org.o2.metadata.pipeline.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.pipeline.domain.entity.PipelineNode;
import org.o2.metadata.pipeline.domain.repository.PipelineNodeRepository;
import org.o2.metadata.pipeline.infra.mapper.PipelineNodeMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author huizhen.liu@hand-china.com 2019-01-09
 */
@Repository
public class PipelineNodeRepositoryImpl extends BaseRepositoryImpl<PipelineNode> implements PipelineNodeRepository {
    private final PipelineNodeMapper pipelineNodeMapper;

    public PipelineNodeRepositoryImpl(final PipelineNodeMapper pipelineNodeMapper) {
        this.pipelineNodeMapper = pipelineNodeMapper;
    }

    @Override
    public List<PipelineNode> listPipelineNode(PipelineNode pipelineNode) {
        return pipelineNodeMapper.listPipelineNode(pipelineNode);
    }
}
