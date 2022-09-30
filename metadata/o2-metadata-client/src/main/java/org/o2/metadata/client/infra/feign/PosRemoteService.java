package org.o2.metadata.client.infra.feign;

import io.swagger.annotations.ApiParam;
import org.o2.core.common.O2Service;
import org.o2.metadata.client.domain.dto.StoreQueryDTO;
import org.o2.metadata.client.infra.feign.fallback.PosRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
     * @param organizationId 租户Id
     * @return 自提点信息
     */
    @GetMapping("/{organizationId}/pos-internal/pickup-info")
    ResponseEntity<String> getPosPickUpInfo(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                            @RequestParam(value = "posCode") String posCode);

    /**
     * 条件批量查询门店信息
     *
     * @param storeQueryDTO 查询条件
     * @param organizationId 租户Id
     * @return 门店信息
     */
    @GetMapping("/{organizationId}/pos-internal/store-list")
    ResponseEntity<String> getStoreInfoList(@RequestBody StoreQueryDTO storeQueryDTO,
                                            @PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId);
}
