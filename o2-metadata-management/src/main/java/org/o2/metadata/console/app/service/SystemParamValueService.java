package org.o2.metadata.console.app.service;


import org.o2.metadata.console.api.vo.SystemParamValueVO;

import java.util.List;
import java.util.Set;

/**
 * 系统参数值应用服务
 *
 * @author hongyun.wang01@hand-china.com 2020-02-13
 */
public interface SystemParamValueService {
    /**
     * 获取参数类型为String的参数值
     *
     * @param paramCode 参数code
     * @param tenantId  租户id
     * @return StringList<String>
     */
    String getSysValueByParam(String paramCode, Long tenantId);

    /**
     * 获取参数类型为list的参数值
     *
     * @param paramCode 参数code
     * @param tenantId  租户id
     * @return List<String>
     */
    List<String> getSysListByParam(String paramCode, Long tenantId);

    /**
     * 获取参数类型为set的参数值
     *
     * @param paramCode 参数code
     * @param tenantId  租户id
     * @return Set<String>
     */
    Set<String> getSysSetByParam(String paramCode, Long tenantId);

    /**
     * 从Redis获取系统参数
     *
     * @param paramCode 参数code
     * @param tenantId  租户id
     * @return String
     */
    String getSysValueFromRedis(String paramCode, Long tenantId);

    /**
     * 从Redis获取系统参数（KV）
     *
     * @param paramCode 参数code
     * @param tenantId  租户id
     * @return String
     */
    String getSysValueKvFromRedis(String paramCode, Long tenantId);

    /**
     * 从Redis获取系统参数（SET）
     *
     * @param paramCode 参数code
     * @param tenantId  租户id
     * @return List
     */
    List<SystemParamValueVO> getSysValueSetFromRedis(String paramCode, Long tenantId);

}
