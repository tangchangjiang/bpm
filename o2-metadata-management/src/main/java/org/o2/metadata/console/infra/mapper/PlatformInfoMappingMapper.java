package org.o2.metadata.console.infra.mapper;

import org.apache.ibatis.annotations.Param;
import org.o2.metadata.console.api.dto.InfMappingDTO;
import org.o2.metadata.console.api.dto.PlatformInfMappingDTO;
import org.o2.metadata.console.infra.entity.PlatformInfoMapping;
import io.choerodon.mybatis.common.BaseMapper;

import java.util.List;

/**
 * 平台信息匹配表Mapper
 *
 * @author zhilin.ren@hand-china.com 2021-08-02 11:11:28
 */
public interface PlatformInfoMappingMapper extends BaseMapper<PlatformInfoMapping> {

    /**
     * 查询平台信息匹配列表
     * @param platformInfMapping 查询条件
     * @return List<PlatformInfMapping> 查询结果
     */
    List<PlatformInfoMapping> listInfMapping(InfMappingDTO platformInfMapping);

    /**
     * 根据主键ID查询详情
     * @param id 主键
     * @return PlatformInfMapping 结果
     */
    PlatformInfoMapping selectById(Long id);

    /**
     * 根据条件查询唯一的匹配项
     * @param platformInfMapping 条件
     * @return PlatformInfMapping 结果
     */
    PlatformInfoMapping selectOneMapping(PlatformInfMappingDTO platformInfMapping);

    /**
     * 批量查询数据
     * @param platformInfMappingDTOList 批量条件
     * @param tenantId 租户Id
     * @return List<PlatformInfMapping> 结果
     */
    List<PlatformInfoMapping> selectCondition(@Param("list") List<PlatformInfMappingDTO> platformInfMappingDTOList, @Param("tenantId") Long tenantId);
}
