package org.o2.metadata.console.infra.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.app.bo.WarehouseCacheBO;
import org.o2.metadata.console.infra.entity.Warehouse;

import java.util.List;


/**
 * 仓库信息资源库
 *
 * @author yuying.shi@hand-china.com 2020/3/2
 */
public interface WarehouseRepository extends BaseRepository<Warehouse> {

    /**
     * 查询未与网店关联的仓库
     *
     * @param shopId   网店 id
     * @param warehouseCode  仓库编码
     * @param warehouseName  仓库名称
     * @param tenantId 租户id
     * @return 仓库列表
     */
    List<Warehouse> listUnbindWarehouseList(Long shopId, String warehouseCode, String warehouseName, Long tenantId);

    /**
     *  条件查询仓促
     * @param warehouse 仓库
     * @return list
     */
    List<Warehouse> listWarehouseByCondition(Warehouse warehouse);

    /**
     * 查询租户下的所有仓库
     * @param tenantId 租户ID
     * @return list
     */
    List<Warehouse> queryAllWarehouseByTenantId(Long tenantId);

    /**
     * 查询有效的仓库
     * @param onlineShopCode 网店编码
     * @param organizationId 租户ID
     * @return list
     */
    List<Warehouse> listActiveWarehouses(String onlineShopCode, Long organizationId);

    /**
     *  编码查询仓促
     * @param warehouseCodes 仓库
     * @param tenantId 租户ID
     * @return list
     */
    List<WarehouseCacheBO> listWarehouseByCode(List<String> warehouseCodes, Long tenantId);
}
