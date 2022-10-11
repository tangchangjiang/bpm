package org.o2.metadata.pipeline.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.o2.metadata.pipeline.domain.entity.PipelineNode;

import java.util.List;

/**
 * @author huizhen.liu@hand-china.com 2019-01-09
 */
public interface PipelineNodeMapper extends BaseMapper<PipelineNode> {

    /**
     * 流程器资源库集合
     * 
     * @param pipelineNode 流程器资源库
     * @return 流程器资源库集合
     */
    List<PipelineNode> listPipelineNode(PipelineNode pipelineNode);

}
