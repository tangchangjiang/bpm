package org.o2.metadata.management.client.infra.feign;

import io.swagger.annotations.ApiParam;
import org.o2.metadata.management.client.domain.dto.FreightDTO;
import org.o2.metadata.management.client.infra.feign.fallback.FreightServiceRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 *
 * 运费
 *
 * @author yipeng.zhu@hand-china.com 2021-07-14
 **/
@FeignClient(
        value = "${o2.service.metadata-management.name:o2-metadata-management-36108}",
        path = "/v1",
        fallback = FreightServiceRemoteServiceImpl.class
)
public interface FreightRemoteService {
    /**
     * 获取运费
     *
     * @param freight 运费参数
     * @param organizationId  租户ID
     * @return 运费
     */
    @PostMapping("/{organizationId}/freight-internal/template")
    ResponseEntity<String> getFreightTemplate(@RequestBody FreightDTO freight,
                                              @PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId);

    /**
     * 获取默认模版
     *
     * @param organizationId 租户id
     * @return 模版
     */
    @GetMapping("/{organizationId}/freight-internal/default")
    ResponseEntity<String> getDefaultTemplate(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId);

    /**
     * 批量获取运费
     *
     * @param templateCodes 运费模板编码
     * @param organizationId  租户ID
     * @return 运费
     */
    @PostMapping("/{organizationId}/freight-internal/templates")
    ResponseEntity<String> listFreightTemplate(@RequestBody List<String> templateCodes,
                                              @PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId);
}
