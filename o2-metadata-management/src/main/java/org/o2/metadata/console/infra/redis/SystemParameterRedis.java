package org.o2.metadata.console.infra.redis;

import org.o2.metadata.console.api.dto.SystemParameterDTO;
import org.o2.metadata.console.infra.entity.SystemParameter;

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
     * @param tenantId 租户ID
     * @return  list
     */
    List<SystemParameter> listSystemParameters(List<String> paramCodeList, Long tenantId);

    /**
     * 全量同步系统参数
     *
     * @param ts 待同步的系统参数数据
     * @param tenantId 租户ID
     **/
    void synToRedis(List<SystemParameter> ts,Long tenantId);

    /**
     * 单条更新系统参数
     *
     * @param systemParameter 待同步的系统参数数据
     * @param tenantId 租户ID
     **/
    void updateToRedis(SystemParameter systemParameter,Long tenantId);

    /**
     * 触发全量网店库存计算
     * @param paramCode 系统参数编码
     * @param tenantId 租户ID
     */
    void extraOperate(String paramCode, Long tenantId);

}
