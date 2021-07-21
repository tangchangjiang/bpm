package org.o2.metadata.console.infra.repository;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import org.o2.core.response.BatchResponse;
import org.o2.metadata.console.infra.entity.Pos;
import org.o2.metadata.console.infra.entity.Region;
import org.o2.metadata.console.infra.entity.RegionRelPos;

import java.util.List;

/**
 * 默认服务点配置资源库
 *
 * @author wei.cai@hand-china.com 2020-01-09 15:41:36
 */
public interface RegionRelPosRepository extends BaseRepository<RegionRelPos> {

    /**
     * 通过条件查询
     * 如果不传条件则分页查询所有的数据(网店维度)
     * 传入网店ID则查询网店下的所有地区(去重)
     * 传入网店ID+区域ID查询所有服务店(去重)
     * @param regionRelPos 条件
     * @param pageRequest 分页条件
     * @return 查询结果
     */
    Page<RegionRelPos> listByCondition(PageRequest pageRequest, RegionRelPos regionRelPos);

    /**
     * 通过网店ID查询未关联地区(树形结构)
     * @param organizationId 租户ID
     * @param onlineStoreId 网店ID
     * @return 地区数据
     */
    List<Region> listUnbindRegion(Long organizationId, Long onlineStoreId);

    /**
     * 通过网店ID以及地区ID查未关联服务点
     * @param pageRequest 分页请求
     * @param regionRelPos 地区关联服务点
     * @return 服务点数据
     */
    Page<Pos> listUnbindPos(PageRequest pageRequest, RegionRelPos regionRelPos);

    /**
     * 批量创建数据
     * @param organizationId 租户Id
     * @param regionRelPos 创建数据
     * @return 批量数据
     */
    BatchResponse<RegionRelPos> batchCreate(Long organizationId, List<RegionRelPos> regionRelPos);

    /**
     * 批量更新数据
     * @param organizationId 租户Id
     * @param regionRelPos 更新数据
     * @return 批量数据
     */
    BatchResponse<RegionRelPos> batchUpdate(Long organizationId, List<RegionRelPos> regionRelPos);

}
