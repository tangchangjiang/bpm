package org.o2.metadata.core.domain.repository;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.core.domain.entity.RegionRelPos;

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

}
