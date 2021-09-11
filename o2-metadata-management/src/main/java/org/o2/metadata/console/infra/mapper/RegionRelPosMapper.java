package org.o2.metadata.console.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.o2.metadata.console.infra.entity.Pos;
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
     * 通过地区ID以及网店ID获取未绑定服务点
     * @param regionRelPos 地区关联服务点
     * @return 服务点
     */
    List<Pos> selectUnbindPosByRegionId(RegionRelPos regionRelPos);

}
