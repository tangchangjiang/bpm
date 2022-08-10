package org.o2.business.process.management.domain.repository;

/**
 * redis操作实现
 * @author zhilin.ren@hand-china.com 2022/08/10 15:01
 */
public interface BusinessProcessRedisRepository {


    /**
     * 更新业务流程节点的状态
     * @param hashKey hashKey
     * @param field field
     * @param value value
     */
    void updateNodeStatus(String hashKey, String field, String value);
}
