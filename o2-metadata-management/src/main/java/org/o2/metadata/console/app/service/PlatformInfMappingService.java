package org.o2.metadata.console.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.o2.metadata.console.api.dto.InfMappingDTO;
import org.o2.metadata.console.infra.entity.PlatformInfMapping;
import java.util.List;


/**
 * 平台信息匹配表应用服务
 *
 * @author zhilin.ren@hand-china.com 2021-08-02 11:11:28
 */
public interface PlatformInfMappingService {

    
    /**
     * 批量保存平台信息匹配表
     *
     * @param platformInfMappingList 平台信息匹配表对象列表
     * @return 平台信息匹配表对象列表
     */
    List<PlatformInfMapping> batchSave(List<PlatformInfMapping> platformInfMappingList);


    /**
     * 保存平台信息匹配表
     *
     * @param platformInfMapping 平台信息匹配表对象
     * @return 平台信息匹配表对象
     */
    PlatformInfMapping save(PlatformInfMapping platformInfMapping);


    /**
     * 分页查询信息匹配
     * @param platformInfMapping 条件
     * @param pageRequest 分页
     * @return Page<PlatformInfMapping> 结果
     */
    Page<PlatformInfMapping> page(InfMappingDTO platformInfMapping, PageRequest pageRequest);
}
