package org.o2.metadata.core.infra.repository.impl;

import org.o2.metadata.core.domain.entity.PostTime;
import org.o2.metadata.core.domain.repository.PostTimeRepository;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Component;

/**
 * 服务点接单和派单时间 资源库实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Component
public class PostTimeRepositoryImpl extends BaseRepositoryImpl<PostTime> implements PostTimeRepository {

}
