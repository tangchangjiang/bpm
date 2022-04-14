package org.o2.metadata.client.infra.feign;

import org.o2.core.common.O2Service;
import org.o2.metadata.client.infra.feign.fallback.PosRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 服务点
 *
 * @author chao.yang05@hand-china.com 2022/4/13
 */
@FeignClient(
        value = O2Service.Metadata.NAME,
        path = "/v1",
        fallback = PosRemoteServiceImpl.class
)
public interface PosRemoteService {

    /**
     * 获取自提点信息
     *
     * @param posCode 服务点编码
     * @param tenantId 租户Id
     * @return 自提点信息
     */
    @GetMapping("/pos-internal/pickup-info")
    ResponseEntity<String> getPosPickUpInfo(@RequestParam String posCode, @RequestParam Long tenantId);
}
