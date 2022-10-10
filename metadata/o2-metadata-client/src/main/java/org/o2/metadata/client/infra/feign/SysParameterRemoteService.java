package org.o2.metadata.client.infra.feign;

import io.swagger.annotations.ApiParam;
import org.o2.core.common.O2Service;
import org.o2.metadata.client.infra.feign.fallback.SysParameterRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
@FeignClient(
        value = O2Service.Metadata.NAME,
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
    @GetMapping("/{organizationId}/sysParameter-internal/{paramCode}")
    ResponseEntity<String> getSystemParameter(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                      @PathVariable(value = "paramCode") @ApiParam(value = "参数code", required = true) String paramCode);
    /**
     * 批量从redis查询系统参数
     * @param paramCodes 编码集合
     * @param organizationId 租户ID
     * @return ResponseEntity<String>
     */
    @GetMapping("/{organizationId}/sysParameter-internal/paramCodes")
    ResponseEntity<String> listSystemParameters(@RequestParam List<String> paramCodes,@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId);
}