package org.o2.feignclient.metadata.infra.feign;

import org.o2.feignclient.metadata.domain.dto.SystemParameterQueryInnerDTO;
import org.o2.feignclient.metadata.infra.constants.O2Service;
import org.o2.feignclient.metadata.infra.feign.fallback.SysParameterRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiParam;

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
     * 从redis查询系统参数
     *
     * @param paramCodes 参数编码
     * @param organizationId 租户ID
     * @return ResponseEntity<String>
     */
    @GetMapping("/{organizationId}/sysParameter-internal/paramCodes")
    ResponseEntity<String> listSystemParameters(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                @RequestParam List<String> paramCodes);

    /**
     * 更新系统参数（map 类型）
     * @param  systemParameterQueryInnerDTO 系统参数
     * @param  organizationId 租户ID
     * @return  ResponseEntity<String>
     */
    @PostMapping({"/{organizationId}/sysParameter-internal/update"})
    ResponseEntity<String> updateSysParameter(@RequestBody SystemParameterQueryInnerDTO systemParameterQueryInnerDTO,
                                              @PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId);
}
