package org.o2.metadata.pipeline.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.pipeline.domain.entity.Pipeline;

import java.util.List;

/**
 * 流程器资源库
 *
 * @author huizhen.liu@hand-china.com 2019-01-09 11:04.
 */
public interface PipelineRepository extends BaseRepository<Pipeline> {
    /**
     * 流程器资源库查询
     *
     * @param pipeline 流程器资源库
     * @return 流程器资源库集合
     */
    List<Pipeline> listPipeline(Pipeline pipeline);

}
