package org.o2.metadata.pipeline.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.pipeline.domain.entity.Pipeline;
import org.o2.metadata.pipeline.domain.repository.PipelineRepository;
import org.o2.metadata.pipeline.infra.mapper.PipelineMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author huizhen.liu@hand-china.com 2019-01-09
 */
@Repository
public class PipelineRepositoryImpl extends BaseRepositoryImpl<Pipeline> implements PipelineRepository {

    private final PipelineMapper pipelineMapper;

    public PipelineRepositoryImpl(final PipelineMapper pipelineMapper) {
        this.pipelineMapper = pipelineMapper;
    }

    @Override
    public List<Pipeline> listPipeline(Pipeline pipeline) {
        return pipelineMapper.listPipeline(pipeline);
    }
}
