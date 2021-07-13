package org.o2.metadata.domain.systemparameter.service;

import org.o2.metadata.domain.systemparameter.domain.SystemParameterDO;

import java.util.List;

/**
 *
 * 系统参数
 *
 * @author yipeng.zhu@hand-china.com 2021-07-09
 **/
public interface SystemParameterDomainService {
    /**
     * 获取系统参数
     * @param paramCode 编码
     * @param tenantId 租户ID
     * @return  系统参数
     */
    SystemParameterDO getSystemParameter(String paramCode, Long tenantId);
    /**
     * 获取系统参数
     * @param paramCodes 编码集合
     * @param tenantId 租户ID
     * @return 集合
     */
    List<SystemParameterDO> listSystemParameters(List<String> paramCodes, Long tenantId);
}
