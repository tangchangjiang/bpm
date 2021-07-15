package org.o2.metadata.pipeline.app.service;

import org.o2.metadata.pipeline.domain.entity.PipelineNode;

import java.util.List;

/**
 * @author huizhen.liu@hand-china.com 2019-01-09
 */
public interface PipelineNodeService {

    /**
     * 批量更新
     * 
     * @param pipelineNodes 已修改流水线行
     * @return 修改的行数量
     */
    int batchMerge(List<PipelineNode> pipelineNodes);
}
