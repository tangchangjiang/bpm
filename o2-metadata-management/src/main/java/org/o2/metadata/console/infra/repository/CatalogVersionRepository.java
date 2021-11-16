package org.o2.metadata.console.infra.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.api.co.CatalogCO;
import org.o2.metadata.console.api.dto.CatalogQueryInnerDTO;
import org.o2.metadata.console.api.dto.CatalogRelVersionQueryDTO;
import org.o2.metadata.console.infra.entity.CatalogVersion;

import java.util.List;

/**
 * 版本目录资源库
 *
 * @author jiu.yang@hand-china.com 2019-12-02 15:33:52
 */
public interface CatalogVersionRepository extends BaseRepository<CatalogVersion> {

    /**
     * 通过编码查询目录版本
     * @param  catalogVersionCodes 目录版本编码
     * @param  organizationId 租户ID
     * @return 目录版本
     */
    List<CatalogVersion> batchSelectByCodes(List<String> catalogVersionCodes, Long organizationId);

    /**
     * 查询目录&目录版本
     * @param queryDTO 入参
     * @return  list
     */
    List<CatalogVersion> catalogRelVersion(CatalogRelVersionQueryDTO queryDTO);

    /**
     * 查询目录&目录版本 父子接口 内部接口逻辑
     * @param queryInner 入参
     * @param organizationId 租户ID
     * @return  list
     */
    List<CatalogCO> listCatalogAndVersion(CatalogQueryInnerDTO queryInner, Long organizationId);
}
