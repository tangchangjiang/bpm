package org.o2.metadata.client;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.metadata.client.domain.co.PlatformCO;
import org.o2.metadata.client.domain.dto.PlatformQueryInnerDTO;
import org.o2.metadata.client.infra.feign.PlatformRemoteService;

import java.util.Map;

/**
 *
 * 平台
 *
 * @author yipeng.zhu@hand-china.com 2021-09-23
 **/
public class PlatformClient {

    private final PlatformRemoteService platformRemoteService;

    public PlatformClient(PlatformRemoteService platformRemoteService) {
        this.platformRemoteService = platformRemoteService;
    }

    /**
     * 查询平台信息 1
     * @param tenantId 租户id
     * @param platformQueryInnerDTO 平台入参
     * @return key : platformCode（平台编码）
     */
    public Map<String, PlatformCO> listPlatforms(PlatformQueryInnerDTO platformQueryInnerDTO, Long tenantId) {
        return ResponseUtils.getResponse(platformRemoteService.listPlatforms(platformQueryInnerDTO,tenantId),new TypeReference<Map<String,PlatformCO>>(){});
    }
}
