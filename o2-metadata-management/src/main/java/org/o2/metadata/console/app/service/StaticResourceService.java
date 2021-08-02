package org.o2.metadata.console.app.service;

import org.o2.metadata.console.infra.entity.StaticResource;


import java.util.List;


/**
 * 静态资源文件表应用服务
 *
 * @author zhanpeng.jiang@hand-china.com 2021-07-30 11:11:38
 */
public interface StaticResourceService {

    
    /**
     * 批量保存静态资源文件表
     *
     * @param staticResourceList 静态资源文件表对象列表
     * @return 静态资源文件表对象列表
     */
    List<StaticResource> batchSave(List<StaticResource> staticResourceList);


    /**
     * 保存静态资源文件表
     *
     * @param staticResource 静态资源文件表对象
     * @return 静态资源文件表对象
     */
    StaticResource save(StaticResource staticResource);
}
