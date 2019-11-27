package org.o2.metadata.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.ext.metadata.domain.entity.PostTime;
import org.o2.ext.metadata.domain.repository.PostTimeRepository;
import org.springframework.stereotype.Component;

/**
 * 服务点接单和派单时间 资源库实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Component
public class PostTimeRepositoryImpl extends BaseRepositoryImpl<PostTime> implements PostTimeRepository {

}
