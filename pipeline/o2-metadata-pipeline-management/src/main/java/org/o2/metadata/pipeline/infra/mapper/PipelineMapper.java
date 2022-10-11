package org.o2.metadata.pipeline.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.o2.metadata.pipeline.domain.entity.Pipeline;

import java.util.List;

/**
 * @author huizhen.liu@hand-china.com 2019-01-09
 */
public interface PipelineMapper extends BaseMapper<Pipeline> {

    /**
     * 流程器资源库查询
     *
     * @param pipeline 流程器资源库
     * @return 流程器资源库集合
     */
    List<Pipeline> listPipeline(Pipeline pipeline);

}
