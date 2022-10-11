package org.o2.metadata.pipeline.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.pipeline.domain.entity.PipelineNode;

import java.util.List;

/**
 * 流程器资源库
 *
 * @author huizhen.liu@hand-china.com 2019-01-09 11:04.
 */
public interface PipelineNodeRepository extends BaseRepository<PipelineNode> {

    /**
     * 流程器资源库集合
     *
     * @param pipelineNode 流程器资源库
     * @return 流程器资源库集合
     */
    List<PipelineNode> listPipelineNode(PipelineNode pipelineNode);

}
