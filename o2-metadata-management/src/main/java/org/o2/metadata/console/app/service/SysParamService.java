package org.o2.metadata.console.app.service;


import org.o2.metadata.console.infra.entity.SystemParameter;
import org.o2.metadata.console.api.vo.SystemParameterVO;

import java.util.List;

/**
 * @author yong.nie@hand-china.com
 * @date 2020/6/30 21:26
 **/

public interface SysParamService {

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

    void extraOperate(String paramCode, Long tenantId);


    /**
     * 从redis查询系统参数
     *
     * @param paramCode 参数编码
     * @param tenantId 租户ID
     * @return SystemParamDetailVO
     */
    SystemParameterVO getSystemParameter(String paramCode, Long tenantId);

}
