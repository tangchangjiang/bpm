package org.o2.metadata.core.domain.repository;

import org.o2.metadata.core.domain.entity.OnlineShopRelPos;
import org.o2.metadata.core.domain.vo.OnlineShopRelPosVO;
import org.hzero.mybatis.base.BaseRepository;
import java.util.List;

/**
 * 网店关联服务点资源库
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface OnlineShopRelPosRepository extends BaseRepository<OnlineShopRelPos> {

    /**
     * 条件查询
     *
     * @param onlineShopId 网店 id
     * @param pos          服务点查询条件，可为空
     * @return 查询列表
     */
    List<OnlineShopRelPosVO> listShopPosRelsByOption(Long onlineShopId, OnlineShopRelPosVO pos);
}
