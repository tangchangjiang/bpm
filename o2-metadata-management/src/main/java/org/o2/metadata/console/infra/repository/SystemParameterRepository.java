package org.o2.metadata.console.infra.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.infra.entity.SystemParameter;

import java.util.List;

/**
 * 系统参数资源库
 *
 * @author hongyun.wang01@hand-china.com 2020-02-13
 */
public interface SystemParameterRepository extends BaseRepository<SystemParameter> {

    List<SystemParameter> fuzzyQuery(SystemParameter systemParameter, Long tenantId);

}
