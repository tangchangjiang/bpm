package org.o2.business.process.management.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.business.process.management.domain.entity.BusinessProcess;
import org.o2.business.process.management.domain.repository.BusinessProcessRepository;
import org.springframework.stereotype.Component;

/**
 * 业务流程定义表 资源库实现
 *
 * @author youlong.peng@hand-china.com 2022-08-10 14:23:57
 */
@Component
public class BusinessProcessRepositoryImpl extends BaseRepositoryImpl<BusinessProcess> implements BusinessProcessRepository {
}
