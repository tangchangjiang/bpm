package org.o2.metadata.console.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.o2.metadata.console.domain.entity.NeighboringRegion;

import java.util.List;

/**
 * 临近省Mapper
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface NeighboringRegionMapper extends BaseMapper<NeighboringRegion> {
    /**
     * 查询临近省结果集
     *
     * @param posTypeCode    服务点类型
     * @param sourceRegionId 发货省
     * @param targetRegionId 收货省
     * @param tenantId 租户ID
     * @return 查询结果集
     */
    List<NeighboringRegion> findNeighboringRegions(@Param(value = "posTypeCode") String posTypeCode,
                                                   @Param(value = "sourceRegionId") Long sourceRegionId,
                                                   @Param(value = "targetRegionId") Long targetRegionId,
                                                   @Param(value = "tenantId") Long tenantId);
}
