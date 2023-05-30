package org.o2.metadata.console.infra.redis;

import org.o2.metadata.console.infra.entity.Carrier;

import java.util.List;

/**
 * 承运商
 *
 * @author yipeng.zhu@hand-china.com 2021-08-05
 **/
public interface CarrierRedis {
    /**
     * 批量更新redis
     *
     * @param tenantId 租户ID
     */
    void batchUpdateRedis(Long tenantId);

    /**
     * 删除redis
     *
     * @param list     承运商I
     * @param tenantId 租户id
     */
    void deleteRedis(List<Carrier> list, Long tenantId);

    /**
     * 插入承运商多语言
     * @param carrier 租户ID
     */
    void insertCarrierMultiRedis(Carrier carrier);
}
