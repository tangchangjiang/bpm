package org.o2.metadata.client;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.metadata.client.domain.co.PosStoreInfoCO;
import org.o2.metadata.client.domain.dto.StoreQueryDTO;
import org.o2.metadata.client.infra.feign.PosRemoteService;

import java.util.List;

/**
 * 服务点
 *
 * @author chao.yang05@hand-china.com 2022/4/13
 */
public class PosClient {
    private final PosRemoteService posRemoteService;

    public PosClient(final PosRemoteService posRemoteService) {
        this.posRemoteService = posRemoteService;
    }

    /**
     * 查询自提点信息
     *
     * @param posCode 服务点编码
     * @param tenantId 租户Id
     * @return 服务点信息
     */
    public PosStoreInfoCO getPosPickUpInfo(String posCode, Long tenantId) {
        return ResponseUtils.getResponse(posRemoteService.getPosPickUpInfo(tenantId, posCode), PosStoreInfoCO.class);
    }

    /**
     * 条件批量查询门店信息
     *
     * @param storeQueryDTO 查询条件
     * @param tenantId 租户Id
     * @return 门店信息
     */
    public List<PosStoreInfoCO> getStoreInfoList(StoreQueryDTO storeQueryDTO, Long tenantId) {
        return ResponseUtils.getResponse(posRemoteService.getStoreInfoList(storeQueryDTO, tenantId), new TypeReference<List<PosStoreInfoCO>>(){});
    }
}
