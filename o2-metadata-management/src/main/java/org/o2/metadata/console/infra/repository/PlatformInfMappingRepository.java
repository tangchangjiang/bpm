package org.o2.metadata.console.infra.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.infra.entity.PlatformInfMapping;

import java.util.List;

/**
 * 平台信息匹配表资源库
 *
 * @author zhilin.ren@hand-china.com 2021-08-02 11:11:28
 */
public interface PlatformInfMappingRepository extends BaseRepository<PlatformInfMapping> {
    /**
     * 查询平台信息匹配列表
     * @param platformInfMapping 查询条件
     * @return List<PlatformInfMapping> 查询结果
     */
    List<PlatformInfMapping> listInfMapping(PlatformInfMapping platformInfMapping);

    /**
     * 根据主键ID查询详情
     * @param id 主键
     * @return PlatformInfMapping 结果
     */
    PlatformInfMapping selectById(Long id);
}
