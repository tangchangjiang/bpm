package org.o2.business.process.management.domain.repository;

import java.util.List;
import java.util.Map;

/**
 * redis操作实现
 * @author zhilin.ren@hand-china.com 2022/08/10 15:01
 */
public interface BusinessProcessRedisRepository {

    /**
     * 查询业务流程配置
     * @param processCode
     * @param tenantId
     * @return
     */
    String getBusinessProcessConfig(String processCode, Long tenantId);


    /**
     * 更新业务流程节点的状态
     * @param key key
     * @param hashKey hashKey
     * @param value value
     */
    void updateNodeStatus(String key, String hashKey, Integer value);


    /**
     * 批量查询业务流程状态
     * @param keys
     * @param tenantId
     * @return
     */
    Map<String, Integer> listNodeStatus(List<String> keys, Long tenantId);
}
