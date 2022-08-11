package org.o2.business.process.management.app.service;

import org.o2.business.process.management.domain.BusinessProcessContext;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/8/10 15:35
 */
public interface BusinessProcessRedisService {

    /**
     * 获取业务流程配置
     * @param processCode 业务流程编码
     * @param tenantId 租户id
     * @return
     */
    BusinessProcessContext getBusinessProcessConfig(String processCode, Long tenantId);
}

