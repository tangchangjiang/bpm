package org.o2.metadata.console.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.o2.metadata.console.infra.entity.RetryErrorLog;

import java.util.List;

/**
 * 元数据错误日志表Mapper
 *
 * @author chao.yang05@hand-china.com 2023-05-10 17:35:16
 */
public interface RetryErrorLogMapper extends BaseMapper<RetryErrorLog> {
    /**
     * 查询queryLog
     *
     * @param retryErrorLog 查询条件
     * @return List<QueueErrorLog>
     */
    List<Long> listQueueErrorLogId(RetryErrorLog retryErrorLog);
}
