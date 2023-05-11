package org.o2.metadata.console.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.console.infra.entity.RetryErrorLog;
import org.o2.metadata.console.infra.repository.RetryErrorLogRepository;
import org.springframework.stereotype.Component;

/**
 * 元数据错误日志表 资源库实现
 *
 * @author chao.yang05@hand-china.com 2023-05-10 17:35:16
 */
@Component
public class RetryErrorLogRepositoryImpl extends BaseRepositoryImpl<RetryErrorLog> implements RetryErrorLogRepository {
}
