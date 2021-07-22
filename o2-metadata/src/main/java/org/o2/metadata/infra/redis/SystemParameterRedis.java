package org.o2.metadata.infra.redis;


import org.o2.metadata.infra.entity.SystemParameter;

import java.util.List;

/**
 *
 * 系统参数redis
 *
 * @author yipeng.zhu@hand-china.com 2021-07-11
 **/
public interface SystemParameterRedis {
    /**
     * redis 查询系统参数
     * @param paramCode 编码
     * @param tenantId 租户ID
     * @return 系统参数
     */
    SystemParameter getSystemParameter(String paramCode, Long tenantId);

    /**
     * 批量 redis 查询系统参数
     * @param paramCodeList 编码集合
     * @param tenantId  租户ID
     * @return  list
     */
    List<SystemParameter> listSystemParameters(List<String> paramCodeList, Long tenantId);
}
