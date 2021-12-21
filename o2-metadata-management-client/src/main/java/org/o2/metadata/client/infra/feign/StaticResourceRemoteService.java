package org.o2.metadata.client.infra.feign;

import io.swagger.annotations.ApiParam;
import org.o2.metadata.client.domain.dto.StaticResourceListDTO;
import org.o2.metadata.client.domain.dto.StaticResourceQueryDTO;
import org.o2.metadata.client.domain.dto.StaticResourceSaveDTO;
import org.o2.core.common.O2Service;
import org.o2.metadata.client.infra.feign.fallback.StaticResourceRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 静态资源表 -远程调用资源库接口
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/07/30 15:33
 */
@FeignClient(
        value = O2Service.MetadataManagement.NAME,
        path = "/v1",
        fallback = StaticResourceRemoteServiceImpl.class
)
public interface StaticResourceRemoteService {

    /**
     * 查询资源code&url映射
     *
     * @param staticResourceQueryDTO staticResourceQueryDTO
     * @return code&url映射关系
     */
    @PostMapping("/{organizationId}/static-resources-internal/query-resource-url")
    ResponseEntity<String> queryResourceCodeUrlMap(@PathVariable(value = "organizationId") Long organizationId, @RequestBody StaticResourceQueryDTO staticResourceQueryDTO);


    /**
     * 保存静态资源文件
     *
     * @param staticResourceSaveDTOList staticResourceSaveDTOList
     * @return code&url映射关系
     */
    @PostMapping("/{organizationId}/static-resources-internal/save")
    ResponseEntity<String> saveResource(@PathVariable(value = "organizationId") Long organizationId, @RequestBody List<StaticResourceSaveDTO> staticResourceSaveDTOList);

    /**
     * 获取静态资源配置
     * @param organizationId 租户ID
     * @param resourceCode 资源编码
     * @return String 静态资源配置
     */
    @GetMapping("/{organizationId}/static-resource-configs-internal/{resourceCode}")
    ResponseEntity<String> getStaticResourceConfig(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                   @PathVariable(value = "resourceCode") @ApiParam(value = "参数code", required = true) String resourceCode);

    /**
     * 获取json_key和resource_url
     * @param organizationId 租户ID
     * @param staticResourceListDTO 查询条件
     * @return String 结果
     */
    @PostMapping("/{organizationId}/static-resources-internal/get_resource_and_config")
    ResponseEntity<String> getStaticResourceAndConfig(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                      @RequestBody StaticResourceListDTO staticResourceListDTO);

    /**
     * 获取启用&支持站点校验的静态资源配置列表
     * @param organizationId 租户ID
     * @return 静态资源配置
     */
    @GetMapping("/{organizationId}/static-resource-configs-internal/list")
    ResponseEntity<String> listStaticResourceConfig(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId);

}

