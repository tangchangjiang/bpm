package org.o2.metadata.console.app.service;

import org.o2.metadata.console.infra.entity.Platform;
import java.util.List;


/**
 * 平台定义表应用服务
 *
 * @author zhilin.ren@hand-china.com 2021-08-02 11:11:28
 */
public interface PlatformService {

    
    /**
     * 批量保存平台定义表
     *
     * @param platformList 平台定义表对象列表
     * @return 平台定义表对象列表
     */
    List<Platform> batchSave(List<Platform> platformList);


    /**
     * 保存平台定义表
     *
     * @param platform 平台定义表对象
     * @return 平台定义表对象
     */
    Platform save(Platform platform);
}
