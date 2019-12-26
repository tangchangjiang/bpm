package org.o2.metadata.console.app.service.impl;

import org.hzero.export.vo.ExportParam;
import org.o2.metadata.console.app.service.CatalogService;
import org.o2.metadata.core.domain.vo.CatalogVO;
import org.o2.metadata.core.domain.repository.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 版本应用服务默认实现
 *
 * @author jiu.yang@hand-china.com 2019-12-02 15:33:52
 */
@Service
public class CatalogServiceImpl implements CatalogService {


    @Autowired
    private CatalogRepository catalogRepository;

    /**
     * 版本Excel导出
     * @param exportParam 版本主键字符拼接
     * @return the return
     * @throws RuntimeException exception description
     */
    @Override
    public List<CatalogVO> export(ExportParam exportParam) {
        Set<Long> catalogBatchIdList =  exportParam.getIds();
        return catalogRepository.batchFindByIds(catalogBatchIdList);
    }
}
