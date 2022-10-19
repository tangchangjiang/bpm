package org.o2.metadata.domain.staticresource.service;

import org.o2.metadata.domain.staticresource.domain.StaticResourceConfigDO;
import org.o2.metadata.domain.staticresource.domain.StaticResourceSaveDO;

import java.util.List;

public interface StaticResourceBusinessService extends BaseBusinessTypeInterface{

    @Override
    default Class<? extends BaseBusinessTypeInterface> getHandlerClass(){
        return StaticResourceBusinessService.class;
    }

    /**
     * 保存静态资源
     * @param tenantId 租户ID
     * @param staticResourceSaveDOList 静态资源DO
     */
    void saveStaticResource(Long tenantId, List<StaticResourceSaveDO> staticResourceSaveDOList);

    /**
     * 查询静态资源配置
     * @param tenantId 租户ID
     * @param resourceCode 资源编码
     * @return 静态资源配置DO
     */
    StaticResourceConfigDO getStaticResourceConfig(Long tenantId, String resourceCode);
}
