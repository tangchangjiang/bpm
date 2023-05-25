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
    /**
     * 查询系统参数
     * @param systemParameter 系统参数
     * @param tenantId 租户ID
     * @return list
     */
    List<SystemParameter> fuzzyQuery(SystemParameter systemParameter, Long tenantId, Integer siteQueryFlag);

    /**
     * 系统初始化数据
     * @param tenantId 租户ID
     * @return list
     */
    List<SystemParameter> queryInitData(Long tenantId);

    /**
     * 查询系统参数
     *
     * @param systemParameter 查询条件
     * @return 系统参数
     */
    SystemParameter findOne(SystemParameter systemParameter);
}
