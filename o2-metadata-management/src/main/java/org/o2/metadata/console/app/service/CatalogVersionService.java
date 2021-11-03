package org.o2.metadata.console.app.service;

import org.o2.metadata.console.api.co.CatalogCO;
import org.o2.metadata.console.api.dto.CatalogQueryInnerDTO;
import org.o2.metadata.console.api.dto.CatalogRelVersionQueryDTO;
import org.o2.metadata.console.api.dto.CatalogVersionQueryInnerDTO;
import org.o2.metadata.console.infra.entity.CatalogVersion;

import java.util.List;
import java.util.Map;

/**
 * 版本目录应用服务
 *
 * @author jiu.yang@hand-china.com 2019-12-02 15:33:52
 */
public interface CatalogVersionService {
    /**
     * 更新
     * @date 2020-05-26
     * @param catalogVersion 目录版本
     */
    void update(final CatalogVersion catalogVersion);
    /**
     * 更新
     * @date 2020-06-10
     * @param catalogVersion 目录版本
     */
    void insert(final CatalogVersion catalogVersion);

    /**
     * 批量查询目录版本
     * @param catalogVersionQueryInnerDTO 目录版本
     * @param organizationId 租户ID
     * @return map
     */
    Map<String,String> listCatalogVersions(CatalogVersionQueryInnerDTO catalogVersionQueryInnerDTO, Long organizationId);

    /**
     * 查询目录&目录版本
     * @param queryDTO 入参
     * @return  list
     */
    List<CatalogVersion> catalogRelVersion(CatalogRelVersionQueryDTO queryDTO);
    /**
     *  目录&目录版本 父子结构 内部接口
     * @param queryInner 入参
     * @param organizationId 租户ID
     * @return  list
     */

    List<CatalogCO> listCatalogAndVersion(CatalogQueryInnerDTO queryInner, Long organizationId);
}
