package org.o2.metadata.core.infra.mapper;

import org.o2.metadata.core.domain.entity.RegionRelPos;
import io.choerodon.mybatis.common.BaseMapper;

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

}
