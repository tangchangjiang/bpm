package org.o2.metadata.console.app.service;

import org.hzero.export.vo.ExportParam;
import org.o2.metadata.core.api.dto.CatalogDTO;

import java.util.List;

/**
 * 版本应用服务
 *
 * @author jiu.yang@hand-china.com 2019-12-02 15:33:52
 */
public interface CatalogService {

    /**
     * 版本Excel导出
     * @param exportParam 版本主键字符拼接
     * @return the return
     * @throws RuntimeException exception description
     */
    List<CatalogDTO> export(ExportParam exportParam);
}
