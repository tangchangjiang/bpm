package org.o2.metadata.pipeline.infra;

import io.swagger.annotations.ApiParam;
import org.o2.metadata.pipeline.infra.constants.O2Service;
import org.o2.metadata.pipeline.infra.fallback.PipelineRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 流程器远程服务
 *
 * @author miao.chen01@hand-china.com 2021-07-23
 */
@FeignClient(
        value = O2Service.Pipeline.NAME,
        path = "/v1",
        fallback = PipelineRemoteServiceImpl.class
)
public interface PipelineRemoteService {
    /**
     * 获取流程器信息
     *
     * @param organizationId 租户id
     * @param code           流程器编码
     * @return 流程器信息
     */
    @GetMapping("/{organizationId}/pipeline/{code}")
    ResponseEntity<String> getPipelineByCode(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                             @PathVariable(value = "code") @ApiParam(value = "流程器编码", required = true) String code);
}
