package org.o2.metadata.client.infra.feign;

import org.o2.metadata.client.domain.dto.PlatformQueryInnerDTO;
import org.o2.core.common.O2Service;
import org.o2.metadata.client.infra.feign.fallback.PlatformRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * description 平台信息匹配内部调用
 *
 * @author zhilin.ren@hand-china.com 2021/08/02 22:14
 */
@FeignClient(
        value = O2Service.Metadata.NAME,
        path = "/v1",
        fallback = PlatformRemoteServiceImpl.class
)
public interface PlatformRemoteService {

    /**
     * 内部调用获取平台信息匹配结果
     * @param organizationId 租户id
     * @param platformQueryInnerDTO 类型
     * @return String 结果
     */
    @PostMapping ("/{organizationId}/platform-internal/list")
    ResponseEntity<String> listPlatforms(@RequestBody PlatformQueryInnerDTO platformQueryInnerDTO,
                                         @PathVariable(value = "organizationId") Long organizationId);

}
