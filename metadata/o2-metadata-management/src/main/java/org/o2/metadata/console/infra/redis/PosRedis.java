package org.o2.metadata.console.infra.redis;

import org.o2.metadata.console.infra.entity.PosInfo;

import java.util.List;

/**
 * 服务店
 *
 * @author chao.yang05@hand-china.com 2022/4/13
 */
public interface PosRedis {

    /**
     * 根据posCodes同步已关联仓库的服务点（门店类型）至Redis
     *
     * @param posCodes 服务店编码
     * @param tenantId 租户Id
     */
    void syncPosToRedis(List<String> posCodes, Long tenantId);

    /**
     * 根据posIds或者posCodes更新门店信息
     *
     * @param posIds 服务点id
     * @param posCodes 服务店编码
     * @param tenantId 租户Id
     */
    void updatePosDetail(List<Long> posIds, List<String> posCodes, Long tenantId);

    /**
     * 缓存服务点多语言
     * @param tenantId 租户id
     */
    void insertPosMultiRedis(Long tenantId, List<PosInfo> list);
}
