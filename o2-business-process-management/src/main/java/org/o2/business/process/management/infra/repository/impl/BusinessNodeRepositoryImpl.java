package org.o2.business.process.management.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.business.process.management.domain.entity.BusinessNode;
import org.o2.business.process.management.domain.repository.BusinessNodeRepository;
import org.springframework.stereotype.Component;

/**
 * 业务流程节点表 资源库实现
 *
 * @author zhilin.ren@hand-china.com 2022-08-10 14:31:01
 */
@Component
public class BusinessNodeRepositoryImpl extends BaseRepositoryImpl<BusinessNode> implements BusinessNodeRepository {
}
