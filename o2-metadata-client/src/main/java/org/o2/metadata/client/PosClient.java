package org.o2.metadata.client;

import org.hzero.core.util.ResponseUtils;
import org.o2.metadata.client.domain.co.PosPickUpInfoCO;
import org.o2.metadata.client.infra.feign.PosRemoteService;

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
    public PosPickUpInfoCO getPosPickUpInfo(String posCode, Long tenantId) {
        return ResponseUtils.getResponse(posRemoteService.getPosPickUpInfo(posCode, tenantId), PosPickUpInfoCO.class);
    }
}
