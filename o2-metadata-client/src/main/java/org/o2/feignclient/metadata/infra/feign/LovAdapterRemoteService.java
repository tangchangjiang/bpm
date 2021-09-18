package org.o2.feignclient.metadata.infra.feign;

import io.swagger.annotations.ApiParam;
import org.o2.feignclient.metadata.infra.constants.O2Service;
import org.o2.feignclient.metadata.infra.feign.fallback.LovAdapterRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * 值集查询
 *
 * @author yipeng.zhu@hand-china.com 2021-08-30
 **/
@FeignClient(
        value = O2Service.Metadata.NAME,
        path = "/v1",
        fallback = LovAdapterRemoteServiceImpl.class
)
public interface LovAdapterRemoteService {


    /**
     * 查询值集中指定值的 描述信息（meaning）
     *
     * @param organizationId 租户id
     * @param lovCode  值集code
     * @param lovValue 值集value
     * @return String
     */
    @GetMapping("/{organizationId}/lov/query-lov-value-meaning")
    ResponseEntity<String> queryLovValueMeaning(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                @RequestParam String lovCode,
                                                @RequestParam String lovValue);
}
