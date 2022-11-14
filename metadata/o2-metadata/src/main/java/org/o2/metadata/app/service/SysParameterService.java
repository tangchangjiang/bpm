package org.o2.metadata.app.service;

import org.o2.metadata.api.co.SystemParameterCO;

import java.util.List;

/**
 * 系统参数缓存
 *
 * @author mark.bao@hand-china.com 2019-05-30
 */
public interface SysParameterService {


    /**
     * 从redis查询系统参数
     *
     * @param paramCode 参数编码
     * @param tenantId 租户ID
     * @return SystemParamDetailVO
     */
    SystemParameterCO getSystemParameter(String paramCode, Long tenantId);

    /**
     * 批量查询系统参数
     * @param  paramCodes 编码集合
     * @param  organizationId 租户id
     * @return  系统参数
     */
    List<SystemParameterCO> listSystemParameters(List<String> paramCodes, Long organizationId);
}
