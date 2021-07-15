package org.o2.metadata.pipeline.node;

import org.o2.metadata.pipeline.data.PipelineExecParam;

/**
 * 流水线节点执行器
 *
 * @author mark.bao@hand-china.com 2018/12/21
 */
public interface PipelineNodeExecutor<T extends PipelineExecParam> {

    /**
     * 流水线节点执行实体
     *
     * @param dataObject 执行流转数据参数(已序列化)
     */
    void run(final T dataObject);
}
