package org.o2.metadata.console.infra.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.infra.entity.RetryErrorLog;

import java.util.List;

/**
 * 元数据错误日志表资源库
 *
 * @author chao.yang05@hand-china.com 2023-05-10 17:35:16
 */
public interface RetryErrorLogRepository extends BaseRepository<RetryErrorLog> {

    /**
     * 查询queryLog
     *
     * @param retryErrorLog 查询条件
     * @return List<QueueErrorLog>
     */
    List<Long> listQueueErrorLogId(RetryErrorLog retryErrorLog);
}
