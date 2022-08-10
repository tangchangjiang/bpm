package org.o2.business.process.management.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.business.process.management.domain.entity.BizNodeParameter;
import org.o2.business.process.management.domain.repository.BizNodeParameterRepository;
import org.springframework.stereotype.Component;

/**
 * 业务节点参数表 资源库实现
 *
 * @author zhilin.ren@hand-china.com 2022-08-10 14:31:01
 */
@Component
public class BizNodeParameterRepositoryImpl extends BaseRepositoryImpl<BizNodeParameter> implements BizNodeParameterRepository {
}
