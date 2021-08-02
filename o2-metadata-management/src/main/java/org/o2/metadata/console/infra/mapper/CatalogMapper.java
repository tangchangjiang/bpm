package org.o2.metadata.console.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.o2.metadata.console.infra.entity.Catalog;
import org.o2.metadata.console.api.vo.CatalogVO;

import java.util.List;
import java.util.Set;

/**
 * 版本Mapper
 *
 * @author jiu.yang@hand-china.com 2019-12-02 15:33:52
 */
public interface CatalogMapper extends BaseMapper<Catalog> {

    /**
     * 更据版本目录主键集合批量查询ExcelDTO
     * @param catalogIds 版本主键集合
     * @param tenantId 租户ID
     * @return the return
     * @throws RuntimeException exception description
     */
    List<CatalogVO> batchFindByIds(@Param(value = "catalogIds") Set<Long> catalogIds, @Param("tenantId") Long tenantId);

    /**
     * 查询目录
     * @author yipeng.zhu@hand-china.com
     * @date 2020-01-03
     * @param catalog 目录查询条件
     * @return 目录列表
     */
    List<Catalog> listCatalog(Catalog catalog);

    /**
     * 通过编码查询目录
     * @param  catalogCodes 目录编码
     * @param  tenantId 租户ID
     * @return 目录
     */
    List<Catalog> batchSelectByCodes(@Param(value = "catalogCodes") List<String> catalogCodes, @Param("tenantId") Long tenantId);
}
