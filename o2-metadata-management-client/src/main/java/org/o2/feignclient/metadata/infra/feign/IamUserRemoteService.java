package org.o2.feignclient.metadata.infra.feign;

import io.swagger.annotations.ApiParam;
import org.o2.feignclient.metadata.domain.dto.IamUserQueryInnerDTO;
import org.o2.feignclient.metadata.infra.constants.O2Service;
import org.o2.feignclient.metadata.infra.feign.fallback.IamUserRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * 用户信息
 *
 * @author yipeng.zhu@hand-china.com 2021-11-01
 **/
@FeignClient(
        value = O2Service.Metadata.NAME,
        path = "/v1",
        fallback = IamUserRemoteServiceImpl.class
)
public interface IamUserRemoteService {

    /**
     * 查询用户信息
     * @param organizationId 租户ID
     * @param queryInner 查询条件
     * @return str
     */
    @GetMapping("/{organizationId}/iam-user-internal/info")
    ResponseEntity<String> listIamUser(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                       IamUserQueryInnerDTO queryInner);
}
