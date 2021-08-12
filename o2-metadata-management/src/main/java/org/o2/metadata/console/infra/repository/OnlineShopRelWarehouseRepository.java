package org.o2.metadata.console.infra.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.api.vo.OnlineShopRelWarehouseVO;
import org.o2.metadata.console.infra.entity.OnlineShopRelWarehouse;

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

    /**
     * 查询所有的关联
     * @param tenantId 租户ID
     * @return
     */
    List<OnlineShopRelWarehouse> queryAllShopRelWarehouseByTenantId(Long tenantId);

    /**
     * 查询网店关联有效仓库
     * @param onlineShopCode 网店编码
     * @param tenantId 租户ID
     * @return list
     */
    List<OnlineShopRelWarehouse> listOnlineShopRelWarehouses(String onlineShopCode, Long tenantId);
}
