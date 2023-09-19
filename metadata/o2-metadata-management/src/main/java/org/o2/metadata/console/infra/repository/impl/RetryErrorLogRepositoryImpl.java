package org.o2.metadata.console.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.console.infra.entity.RetryErrorLog;
import org.o2.metadata.console.infra.mapper.RetryErrorLogMapper;
import org.o2.metadata.console.infra.repository.RetryErrorLogRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 元数据错误日志表 资源库实现
 *
 * @author chao.yang05@hand-china.com 2023-05-10 17:35:16
 */
@Component("o2mdRetryErrorLogRepositoryImpl")
public class RetryErrorLogRepositoryImpl extends BaseRepositoryImpl<RetryErrorLog> implements RetryErrorLogRepository {

    private final RetryErrorLogMapper retryErrorLogMapper;

    public RetryErrorLogRepositoryImpl(RetryErrorLogMapper retryErrorLogMapper) {
        this.retryErrorLogMapper = retryErrorLogMapper;
    }

    @Override
    public List<Long> listQueueErrorLogId(RetryErrorLog retryErrorLog) {
        return retryErrorLogMapper.listQueueErrorLogId(retryErrorLog);
    }
}
