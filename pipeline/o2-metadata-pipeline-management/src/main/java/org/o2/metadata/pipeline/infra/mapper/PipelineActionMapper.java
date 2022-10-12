package org.o2.metadata.pipeline.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.o2.metadata.pipeline.domain.entity.PipelineAction;

import java.util.List;

/**
 * 流程器行为Mapper
 *
 * @author wenjun.deng01@hand-china.com 2019-12-16 10:36:04
 */
public interface PipelineActionMapper extends BaseMapper<PipelineAction> {

    /**
     * 查询动作
     * @param pipelineAction 动作
     * @return 动作
     */
    List<PipelineAction> listPipelineAction(PipelineAction pipelineAction);

    /**
     * 查询动作,带参数
     * @param pipelineAction 动作
     * @return 动作
     */
    List<PipelineAction> allWithParameters(PipelineAction pipelineAction);

}
