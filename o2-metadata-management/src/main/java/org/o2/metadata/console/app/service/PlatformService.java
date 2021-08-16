package org.o2.metadata.console.app.service;

import org.o2.metadata.console.api.co.PlatformCO;
import org.o2.metadata.console.api.dto.PlatformQueryInnerDTO;
import org.o2.metadata.console.infra.entity.Platform;
import java.util.Map;


/**
 * 平台定义表应用服务
 *
 * @author zhilin.ren@hand-china.com 2021-08-02 11:11:28
 */
public interface PlatformService {




    /**
     * 保存平台定义表
     *
     * @param platform 平台定义表对象
     * @return 平台定义表对象
     */
    Platform save(Platform platform);

    /**
     * 查询平台信息
     * @param queryInnerDTO 入参
     * @return  map
     */
    Map<String, PlatformCO> selectCondition(PlatformQueryInnerDTO queryInnerDTO);
}
