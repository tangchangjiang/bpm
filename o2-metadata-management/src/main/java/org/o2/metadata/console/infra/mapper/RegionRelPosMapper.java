package org.o2.metadata.console.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.o2.metadata.console.infra.entity.Pos;
import org.o2.metadata.console.infra.entity.Region;
import org.o2.metadata.console.infra.entity.RegionRelPos;

import java.util.List;

/**
 * 默认服务点配置Mapper
 *
 * @author wei.cai@hand-china.com 2020-01-09 15:41:36
 */
public interface RegionRelPosMapper extends BaseMapper<RegionRelPos> {

    /**
     * 通过网店id+租户id查询
     * @param regionRelPos 条件
     * @return 查询结果
     */
    List<RegionRelPos> selectByStoreId(RegionRelPos regionRelPos);

    /**
     * 根据网店id查询未绑定地区
     * @param organizationId 租户ID
     * @param onlineStoreId 网店ID
     * @return 地区
     */
    List<Region> selectUnbindRegionByStoreId(@Param("tenantId") Long organizationId, @Param("onlineStoreId") Long onlineStoreId);

    /**
     * 通过地区ID以及网店ID获取未绑定服务点
     * @return 服务点
     */
    List<Pos> selectUnbindPosByRegionId(RegionRelPos regionRelPos);

}
