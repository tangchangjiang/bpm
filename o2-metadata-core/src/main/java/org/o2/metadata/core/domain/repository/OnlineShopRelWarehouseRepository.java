package org.o2.metadata.core.domain.repository;

import org.o2.metadata.core.domain.entity.OnlineShopRelWarehouse;
import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.core.domain.vo.OnlineShopRelWarehouseVO;

import java.util.List;

/**
 * 网店关联服务点资源库
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface OnlineShopRelWarehouseRepository extends BaseRepository<OnlineShopRelWarehouse> {

    /**
     * 条件查询
     *
     * @param onlineShopId 网店 id
     * @param warehouseVO          服务点查询条件，可为空
     * @return 查询列表
     */
    List<OnlineShopRelWarehouseVO> listShopPosRelsByOption(Long onlineShopId, OnlineShopRelWarehouseVO warehouseVO);
}
