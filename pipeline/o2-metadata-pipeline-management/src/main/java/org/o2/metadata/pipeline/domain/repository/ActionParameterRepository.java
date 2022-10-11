package org.o2.metadata.pipeline.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.pipeline.domain.entity.ActionParameter;

/**
 * 行为参数资源库
 *
 * @author wei.cai@hand-china.com 2020-03-18
 */
public interface ActionParameterRepository extends BaseRepository<ActionParameter>, LibraryCacheInterface<ActionParameter> {
}
