package org.o2.metadata.app.service;

import org.o2.metadata.api.co.PosStoreInfoCO;
import org.o2.metadata.api.dto.StoreQueryDTO;

import java.util.List;

/**
 * 服务点
 *
 * @author chao.yang05@hand-china.com 2022/4/14
 */
public interface PosService {

    /**
     * 查询自提点信息
     *
     * @param posCode 门店code
     * @param tenantId 租户Id
     * @return 自提信息
     */
    PosStoreInfoCO getPosPickUpInfo(String posCode, Long tenantId);

    /**
     * 条件批量查询门店信息
     *
     * @param storeQueryDTO 查询条件
     * @param tenantId 租户Id
     * @return 门店信息
     */
    List<PosStoreInfoCO> getStoreInfoList(StoreQueryDTO storeQueryDTO, Long tenantId);
}
