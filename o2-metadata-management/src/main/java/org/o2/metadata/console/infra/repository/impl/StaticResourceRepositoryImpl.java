package org.o2.metadata.console.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.console.infra.entity.StaticResource;
import org.o2.metadata.console.infra.repository.StaticResourceRepository;
import org.springframework.stereotype.Component;

/**
 * 静态资源文件表 资源库实现
 *
 * @author zhanpeng.jiang@hand-china.com 2021-07-30 11:11:38
 */
@Component
public class StaticResourceRepositoryImpl extends BaseRepositoryImpl<StaticResource> implements StaticResourceRepository {
}