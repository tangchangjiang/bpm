package org.o2.metadata.management.client.infra.feign;

import io.swagger.annotations.ApiParam;
import org.o2.core.common.O2Service;
import org.o2.metadata.management.client.domain.dto.IamUserQueryInnerDTO;
import org.o2.metadata.management.client.infra.feign.fallback.IamUserRemoteServiceImpl;
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
        value = O2Service.MetadataManagement.NAME,
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
    @Deprecated
    ResponseEntity<String> listIamUser(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                       IamUserQueryInnerDTO queryInner);

    /**
     * 查询用户信息
     * @param organizationId 租户ID
     * @param queryInner 查询条件
     * @return str
     */
    @GetMapping("/{organizationId}/iam-user-internal/info-list")
    ResponseEntity<String> listIamUserInfos(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                            IamUserQueryInnerDTO queryInner);

    /**
     * 查询用户信息（平台层）
     *
     * @param queryInner 查询条件
     * @return 用户信息
     */
    @GetMapping("/iam-user-internal/info-list")
    ResponseEntity<String> listIamUserInfoOfSite(IamUserQueryInnerDTO queryInner);
}
