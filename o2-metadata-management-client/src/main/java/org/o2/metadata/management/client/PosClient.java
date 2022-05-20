package org.o2.metadata.management.client;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.metadata.management.client.domain.co.PosAddressCO;
import org.o2.metadata.management.client.domain.dto.PosAddressQueryInnerDTO;
import org.o2.metadata.management.client.domain.dto.PosQueryInnerDTO;
import org.o2.metadata.management.client.infra.feign.PosRemoteService;

import java.util.Map;

/**
 *
 * 服务点
 *
 * @author yipeng.zhu@hand-china.com 2021-09-23
 **/
public class PosClient {
    private final PosRemoteService posRemoteService;

    public PosClient(PosRemoteService posRemoteService) {
        this.posRemoteService = posRemoteService;
    }

    /**
     * 批量查询服务点地址 1
     * @param posAddressQueryInnerDTO 服务点地址
     * @param tenantId 租户ID
     * @return string
     */
    public Map<String, PosAddressCO> listPosAddress(PosAddressQueryInnerDTO posAddressQueryInnerDTO, Long tenantId) {
        return ResponseUtils.getResponse(posRemoteService.listPosAddress(posAddressQueryInnerDTO, tenantId), new TypeReference<Map<String, PosAddressCO>>() {
        });
    }

    public Map<String, String> listPoseName(Long tenantId, PosQueryInnerDTO posQueryInnerDTO) {
        return ResponseUtils.getResponse(posRemoteService.listPoseName(tenantId, posQueryInnerDTO), new TypeReference<Map<String, String>>() {
        });
    }
}
