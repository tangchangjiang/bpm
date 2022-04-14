package org.o2.metadata.infra.redis;

import org.o2.metadata.api.dto.StoreQueryDTO;
import org.o2.metadata.infra.entity.Pos;

import java.util.List;

/**
 * @author chao.yang05@hand-china.com 2022/4/14
 */
public interface PosRedis {

    /**
     * 查询自提点信息
     *
     * @param posCode 门店code
     * @param tenantId 租户Id
     * @return 服务点信息
     */
    Pos getPosPickUpInfo(String posCode, Long tenantId);

    /**
     * 条件批量查询门店信息
     *
     * @param storeQueryDTO 查询条件
     * @param tenantId 租户Id
     * @return 服务点信息
     */
    List<Pos> getStoreInfoList(StoreQueryDTO storeQueryDTO, Long tenantId);
}
