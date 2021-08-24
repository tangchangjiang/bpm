package org.o2.feignclient.metadata.infra.feign;

import io.swagger.annotations.ApiParam;
import org.o2.feignclient.metadata.infra.constants.O2Service;
import org.o2.feignclient.metadata.infra.feign.fallback.CarrierRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
@FeignClient(
        value = O2Service.Metadata.NAME,
        path = "/v1",
        fallback = CarrierRemoteServiceImpl.class
)
public interface CarrierRemoteService {

    /**
     * 查询承运商
     * @param organizationId 租户id
     * @return  list
     */
    @GetMapping({"/{organizationId}/internal/carriers/list"})
    ResponseEntity<String> listCarriers(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId);
}