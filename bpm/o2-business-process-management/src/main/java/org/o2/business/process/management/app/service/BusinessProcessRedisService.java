package org.o2.business.process.management.app.service;

import org.o2.business.process.management.domain.entity.BusinessNode;
import org.o2.business.process.management.domain.entity.BusinessProcess;
import org.o2.process.domain.engine.BpmnModel;

import java.util.List;

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
    BpmnModel getBusinessProcessConfig(String processCode, Long tenantId);


    /**
     * 批量更新节点缓存
     * @param tenantId
     * @param businessNodes
     */
    void batchUpdateNodeStatus(List<BusinessNode> businessNodes, Long tenantId);

    /**
     * 批量更新业务流程缓存配置
     * @param processList
     * @param tenantId
     */
    void batchUpdateProcessConfig(List<BusinessProcess> processList, Long tenantId);


    /**
     * 查询流程最后更新时间
     * @param processCode
     * @param tenantId
     * @return
     */
    Long getProcessLastUpdateTime(String processCode, Long tenantId);
}

