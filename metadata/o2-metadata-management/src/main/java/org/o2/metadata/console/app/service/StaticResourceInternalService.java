package org.o2.metadata.console.app.service;

import org.o2.metadata.console.api.dto.StaticResourceQueryDTO;
import org.o2.metadata.console.api.dto.StaticResourceSaveDTO;
import org.o2.metadata.console.infra.entity.StaticResource;


import java.util.List;
import java.util.Map;

import io.choerodon.mybatis.service.BaseService;

/**
 * 静态资源文件表 内部服务
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/07/30 11:53
 */
public interface StaticResourceInternalService extends BaseService<StaticResource> {

    /**
     * 根据资源编码列表 返回对应的静态资源文件url映射
     *
     * @param staticResourceQueryDTO 静态资源 QueryDTO
     * @return 静态资源文件 code & url映射
     */
    Map<String, String> queryResourceCodeUrlMap(StaticResourceQueryDTO staticResourceQueryDTO);


    /**
     * 静态资源创建/更新
     *
     * @param staticResourceSaveDTO 静态资源 SaveDTO
     */
    void saveResource(StaticResourceSaveDTO staticResourceSaveDTO);

    /**
     * 静态资源批量创建/更新
     * @param staticResourceSaveDTOList 静态资源 SaveDTO List
     */
    void batchSaveResource(List<StaticResourceSaveDTO> staticResourceSaveDTOList);
}
