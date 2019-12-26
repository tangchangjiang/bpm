package org.o2.metadata.core.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.core.domain.vo.CatalogVO;
import org.o2.metadata.core.domain.entity.Catalog;

import java.util.List;
import java.util.Set;

/**
 * 版本资源库
 *
 * @author jiu.yang@hand-china.com 2019-12-02 15:33:52
 */
public interface CatalogRepository extends BaseRepository<Catalog> {

    /**
     * 更据版本目录主键集合批量查询ExcelDTO
     * @param catalogIds 版本主键集合
     * @return the return
     * @throws RuntimeException exception description
     */
    List<CatalogVO> batchFindByIds(Set<Long> catalogIds);
}
