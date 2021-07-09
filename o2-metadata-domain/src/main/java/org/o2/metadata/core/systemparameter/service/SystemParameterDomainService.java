package org.o2.metadata.core.systemparameter.service;

import org.o2.metadata.core.systemparameter.domain.SystemParameterDO;

/**
 *
 * 系统参数
 *
 * @author yipeng.zhu@hand-china.com 2021-07-09
 **/
public interface SystemParameterDomainService {

    SystemParameterDO getSystemParameter(String paramCode, Long tenantId);
}
