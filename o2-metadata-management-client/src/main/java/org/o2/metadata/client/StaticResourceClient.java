package org.o2.metadata.client;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.metadata.client.domain.co.StaticResourceAndConfigCO;
import org.o2.metadata.client.domain.co.StaticResourceConfigCO;
import org.o2.metadata.client.domain.dto.StaticResourceListDTO;
import org.o2.metadata.client.domain.dto.StaticResourceQueryDTO;
import org.o2.metadata.client.domain.dto.StaticResourceSaveDTO;
import org.o2.metadata.client.infra.feign.StaticResourceRemoteService;

import java.util.List;
import java.util.Map;

/**
 *
 * 静态资源
 *
 * @author yipeng.zhu@hand-china.com 2021-09-23
 **/
public class StaticResourceClient {
    private final StaticResourceRemoteService staticResourceRemoteService;

    public StaticResourceClient(StaticResourceRemoteService staticResourceRemoteService) {
        this.staticResourceRemoteService = staticResourceRemoteService;
    }

    /**
     * 获取静态资源配置
     * @param resourceCode 资源编码
     * @param tenantId 租户ID
     * @return StaticResourceConfigCO 配置
     */
    public StaticResourceConfigCO getStaticResourceConfig(String resourceCode, Long tenantId) {
        return ResponseUtils.getResponse(staticResourceRemoteService.getStaticResourceConfig(tenantId,resourceCode),StaticResourceConfigCO.class);
    }

    /**
     * 获取json_key 和 resource_url
     * @param tenantId 租户ID
     * @param staticResourceListDTO 查询条件
     * @return List<StaticResourceAndConfigCO> 结果
     */
    public List<StaticResourceAndConfigCO> getStaticResourceAndConfig(Long tenantId, StaticResourceListDTO staticResourceListDTO){
        return ResponseUtils.getResponse(staticResourceRemoteService.getStaticResourceAndConfig(tenantId, staticResourceListDTO), new TypeReference<List<StaticResourceAndConfigCO>>() {});
    }

    /**
     * 获取启用&支持站点校验的静态资源配置列表
     * @param tenantId 租户ID
     * @return 结果
     */
    public List<StaticResourceConfigCO> listStaticResourceConfig(Long tenantId) {
        return ResponseUtils.getResponse(staticResourceRemoteService.listStaticResourceConfig(tenantId), new TypeReference<List<StaticResourceConfigCO>>() {
        });
    }

    /**
     * 保存静态资源文件表
     *
     * @param staticResourceSaveDTOList staticResourceSaveDTOList
     */
    public Boolean saveResource(final Long organizationId, List<StaticResourceSaveDTO> staticResourceSaveDTOList) {
        return ResponseUtils.getResponse(staticResourceRemoteService.saveResource(organizationId, staticResourceSaveDTOList), Boolean.class);
    }

    /**
     * 查询静态资源文件code&url映射
     *
     * @param staticResourceQueryDTO staticResourceQueryDTO
     * @return code&url映射
     */
    public Map<String, String> queryResourceCodeUrlMap(final Long organizationId, StaticResourceQueryDTO staticResourceQueryDTO) {
        return ResponseUtils.getResponse(staticResourceRemoteService.queryResourceCodeUrlMap(organizationId, staticResourceQueryDTO), new TypeReference<Map<String, String>>() {
        });
    }
}
