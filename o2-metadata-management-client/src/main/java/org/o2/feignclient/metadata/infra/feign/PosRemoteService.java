package org.o2.feignclient.metadata.infra.feign;

import io.swagger.annotations.ApiParam;
import org.o2.feignclient.metadata.domain.dto.PosAddressDTO;
import org.o2.feignclient.metadata.infra.constants.O2Service;
import org.o2.feignclient.metadata.infra.feign.fallback.PosRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * 服务点
 * @author yipeng.zhu@hand-china.com 2021-08-02
 **/
@FeignClient(
        value = O2Service.Metadata.NAME,
        path = "/v1",
        fallback = PosRemoteServiceImpl.class
)
public interface PosRemoteService {

    /**
     * 批量查询服务点地址
     * @param posAddressDTO 服务点地址
     * @param organizationId 租户ID
     * @return string
     */
    @PostMapping("/{organizationId}/pos-internal/select-address")
    ResponseEntity<String> listPosAddress(@RequestBody PosAddressDTO posAddressDTO,
                                          @PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true)Long organizationId);
}