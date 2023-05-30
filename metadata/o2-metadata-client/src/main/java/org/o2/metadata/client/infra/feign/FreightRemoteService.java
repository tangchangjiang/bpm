package org.o2.metadata.client.infra.feign;

import io.swagger.annotations.ApiParam;
import org.o2.core.common.O2Service;
import org.o2.metadata.client.domain.dto.FreightDTO;
import org.o2.metadata.client.infra.feign.fallback.FreightRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * 运费
 *
 * @author yipeng.zhu@hand-china.com 2021-07-14
 **/
@FeignClient(
        value = O2Service.Metadata.NAME,
        path = "/v1",
        fallback = FreightRemoteServiceImpl.class
)
public interface FreightRemoteService {
    /**
     * 获取运费
     *
     * @param freight        运费参数
     * @param organizationId 租户ID
     * @return 运费
     */
    @PostMapping("/{organizationId}/freight-internal/template")
    ResponseEntity<String> getFreightTemplate(@RequestBody FreightDTO freight,
                                              @PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId);

    /**
     * 获取运费
     *
     * @param freightList    运费参数
     * @param organizationId 租户ID
     * @return 运费
     */
    @PostMapping("/{organizationId}/freight-internal/template-list")
    ResponseEntity<String> listFreightTemplates(@RequestBody List<FreightDTO> freightList,
                                                @PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId);

    /**
     * 获取运费（多租户）
     *
     * @param freightMap 运费参数map tenantId:FreightDTO
     * @return 运费
     */
    @PostMapping("/freight-internal/template-batch-tenant")
    ResponseEntity<String> getFreightTemplateBatchTenant(@RequestBody Map<Long, FreightDTO> freightMap);

    /**
     * 批量获取运费（多租户）
     * @param freightMap 运费参数map tenantId:list
     * @return 运费结果
     */
    @PostMapping("/freight-internal/template-list-batch-tenant")
    ResponseEntity<String> listFreightTemplatesBatchTenant(@RequestBody Map<Long, List<FreightDTO>> freightMap);
}
