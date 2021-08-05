package org.o2.metadata.console.infra.redis;




/**
 *
 * 承运商
 *
 * @author yipeng.zhu@hand-china.com 2021-08-05
 **/
public interface CarrierRedis {
    /**
     * 批量更新redis
     * @param tenantId 租户ID
     */
    void batchUpdateRedis(Long tenantId);
}
