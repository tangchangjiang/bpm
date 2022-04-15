package org.o2.metadata.console.infra.redis;

import java.util.List;

/**
 * 服务店
 *
 * @author chao.yang05@hand-china.com 2022/4/13
 */
public interface PosRedis {

    /**
     * 同步关联仓库的门店类型服务店至Redis
     *
     * @param posCodes 服务店编码
     * @param tenantId 租户Id
     */
    void syncPosToRedis(List<String> posCodes, Long tenantId);

    /**
     * 更新门店信息
     *
     * @param posIds 服务点id
     * @param posCodes 服务店编码
     * @param tenantId 租户Id
     */
    void updatePodDetail(List<Long> posIds, List<String> posCodes, Long tenantId);
}
