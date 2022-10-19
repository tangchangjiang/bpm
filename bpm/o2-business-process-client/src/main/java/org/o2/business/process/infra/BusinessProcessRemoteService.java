package org.o2.business.process.infra;

import io.swagger.annotations.ApiParam;
import org.o2.business.process.infra.fallback.BusinessProcessRemoteServiceImpl;
import org.o2.core.common.O2Service;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 流程器远程服务
 *
 * @author miao.chen01@hand-china.com 2021-07-23
 */
@FeignClient(
        value = O2Service.BusinessProcessManagement.NAME,
        path = "/v1",
        fallback = BusinessProcessRemoteServiceImpl.class
)
public interface BusinessProcessRemoteService {
    /**
     * 获取流程器信息
     *
     * @param organizationId 租户id
     * @param processCode  业务流程编码
     * @return 业务流程配置信息
     */
    @GetMapping("/{organizationId}/internal/business-process/{processCode}")
    ResponseEntity<String> getProcessConfigByCode(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                  @PathVariable(value = "processCode") @ApiParam(value = "业务流程编码", required = true) String processCode);



    /**
     * 获取流程器信息
     *
     * @param organizationId 租户id
     * @param processCode  业务流程编码
     * @return 业务流程配置信息
     */
    @GetMapping("/{organizationId}/internal/business-process/last-update-time/{processCode}")
    ResponseEntity<String> getProcessLastModifiedTime(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                             @PathVariable(value = "processCode") @ApiParam(value = "业务流程编码", required = true) String processCode);
}
