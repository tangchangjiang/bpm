package org.o2.feignclient.metadata.infra.feign;

import org.o2.feignclient.metadata.infra.constants.O2Service;
import org.o2.feignclient.metadata.infra.feign.fallback.SysParameterRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.annotations.ApiParam;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
@FeignClient(
        value = O2Service.metadata.NAME,
        path = "/v1",
        fallback = SysParameterRemoteServiceImpl.class
)
public interface SysParameterRemoteService {
    /**
     * 从redis查询系统参数
     *
     * @param paramCode 参数编码
     * @param organizationId 租户ID
     * @return ResponseEntity<String>
     */
    @GetMapping("/{organizationId}/{paramCode}")
    public ResponseEntity<String> listSystemParameter(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                      @PathVariable(value = "paramCode") @ApiParam(value = "参数code", required = true) String paramCode);

}
