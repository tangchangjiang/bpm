package org.o2.feignclient;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.feignclient.metadata.domain.co.PosAddressCO;
import org.o2.feignclient.metadata.domain.dto.PosAddressQueryInnerDTO;
import org.o2.feignclient.metadata.infra.feign.PosRemoteService;

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
}
