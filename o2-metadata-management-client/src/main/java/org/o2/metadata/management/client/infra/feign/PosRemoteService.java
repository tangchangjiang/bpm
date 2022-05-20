package org.o2.metadata.management.client.infra.feign;

import io.swagger.annotations.ApiParam;
import org.o2.core.common.O2Service;
import org.o2.metadata.management.client.domain.dto.PosAddressQueryInnerDTO;
import org.o2.metadata.management.client.infra.feign.fallback.PosRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 *
 * 服务点
 * @author yipeng.zhu@hand-china.com 2021-08-02
 **/
@FeignClient(
        value = O2Service.MetadataManagement.NAME,
        path = "/v1",
        fallback = PosRemoteServiceImpl.class
)
public interface PosRemoteService {

    /**
     * 批量查询服务点地址
     * @param posAddressQueryInnerDTO 服务点地址
     * @param organizationId 租户ID
     * @return string
     */
    @PostMapping("/{organizationId}/pos-internal/select-address")
    ResponseEntity<String> listPosAddress(@RequestBody PosAddressQueryInnerDTO posAddressQueryInnerDTO,
                                          @PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true)Long organizationId);
    /**
     * 批量查询服务点名称
     * @param posCodes 服务点编码
     * @param organizationId 租户ID
     * @return string
     */
    @PostMapping("/{organizationId}/pos-internal/select-name")
    ResponseEntity<String> listPoseName(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true)Long organizationId,
                                        @RequestBody List<String> posCodes);
}
