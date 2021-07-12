package org.o2.metadata.console.infra.redis;

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
     * @return
     */
    SystemParameter getSystemParameter(String paramCode, Long tenantId);
    /**
     * 批量 redis 查询系统参数
     * @param paramCodeList 编码集合
     * @return  list
     */
    List<SystemParameter> listSystemParameters(List<String> paramCodeList, Long tenantId);

    /**
     * 全量同步系统参数
     *
     * @param ts 待同步的系统参数数据
     * @param tenantId 租户ID
     **/
    void synToRedis(List<SystemParameter> ts,
                    Long tenantId);

    /**
     * 单条更新系统参数
     *
     * @param systemParameter 待同步的系统参数数据
     * @param tenantId 租户ID
     **/
    void updateToRedis(SystemParameter systemParameter,
                       Long tenantId);

    /**
     * 单条更新系统参数
     *
     * @param paramId  系统参数ID
     * @param tenantId 租户ID
     **/
    void updateToRedis(Long paramId, Long tenantId);
    /**
     *
     * @date 2021-07-12
     * @param
     * @return 
     */
    void extraOperate(String paramCode, Long tenantId);
    
    /**
     * 删除系统参数
     * @param
     * @return 
     */
    void delToRedis(SystemParameter systemParameter,Long tenantId);
}
