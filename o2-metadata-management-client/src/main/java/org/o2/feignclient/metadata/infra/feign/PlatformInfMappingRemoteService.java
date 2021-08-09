package org.o2.feignclient.metadata.infra.feign;

import org.o2.feignclient.metadata.domain.dto.PlatformInfMappingDTO;
import org.o2.feignclient.metadata.infra.constants.O2Service;
import org.o2.feignclient.metadata.infra.feign.fallback.PlatformInfMappingRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * description 平台信息匹配内部调用
 *
 * @author zhilin.ren@hand-china.com 2021/08/02 22:14
 */
@FeignClient(
        value = O2Service.Metadata.NAME,
        path = "/v1",
        fallback = PlatformInfMappingRemoteServiceImpl.class
)
public interface PlatformInfMappingRemoteService {
    /**
     * 批量，内部调用获取平台信息匹配结果
     * @param organizationId 租户id
     * @param platformInfMapping 条件
     * @return String 结果
     */
    @PostMapping("/{organizationId}/platform-inf-mappings-internal")
    ResponseEntity<String> getPlatformMapping(@PathVariable(value = "organizationId") Long organizationId,
                                              @RequestBody List<PlatformInfMappingDTO> platformInfMapping);

    /**
     * 内部调用获取平台信息匹配结果
     * @param organizationId 租户id
     * @param platformCode 平台code
     * @param infTypeCode 类型
     * @return String 结果
     */
    @GetMapping("/{organizationId}/platform-inf-mappings-internal")
    ResponseEntity<String> getPlatformInfMapping(@PathVariable(value = "organizationId") Long organizationId,
                                                 @RequestParam("platformCode") String platformCode,
                                                 @RequestParam("infTypeCode") String infTypeCode);

}
