package org.o2.metadata.console.infra.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.api.co.WarehouseCO;
import org.o2.metadata.console.api.co.WarehouseRelAddressCO;
import org.o2.metadata.console.api.dto.WarehouseAddrQueryDTO;
import org.o2.metadata.console.api.dto.WarehousePageQueryInnerDTO;
import org.o2.metadata.console.api.dto.WarehouseQueryInnerDTO;
import org.o2.metadata.console.api.dto.WarehouseRelCarrierQueryDTO;
import org.o2.metadata.console.app.bo.WarehouseCacheBO;
import org.o2.metadata.console.infra.entity.Carrier;
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
     * @param shopId        网店 id
     * @param warehouseCode 仓库编码
     * @param warehouseName 仓库名称
     * @param tenantId      租户id
     * @return 仓库列表
     */
    List<Warehouse> listUnbindWarehouseList(Long shopId, String warehouseCode, String warehouseName, Long tenantId);

    /**
     * 条件查询仓促
     *
     * @param warehouse 仓库
     * @return list
     */
    List<Warehouse> listWarehouseByCondition(Warehouse warehouse);


    /**
     * 内部接口 条件查询仓库
     *
     * @param innerDTO 入参
     * @param tenantId 租户ID
     * @return list
     */
    List<Warehouse> listWarehouses(WarehouseQueryInnerDTO innerDTO, Long tenantId);

    /**
     * 查询租户下的所有仓库
     *
     * @param tenantId 租户ID
     * @return list
     */
    List<Warehouse> queryAllWarehouseByTenantId(Long tenantId);

    /**
     * 查询有效的仓库
     *
     * @param onlineShopCode 网店编码
     * @param organizationId 租户ID
     * @return list
     */
    List<Warehouse> listActiveWarehouseByShopCode(String onlineShopCode, Long organizationId);

    /**
     * 编码查询仓促
     *
     * @param warehouseCodes 仓库
     * @param tenantId       租户ID
     * @return list
     */
    List<WarehouseCacheBO> listWarehouseByCode(List<String> warehouseCodes, Long tenantId);


    /**
     * 仓库关联承运商
     *
     * @param queryDTO 查询条件
     * @return list
     */
    List<Carrier> listCarriers(WarehouseRelCarrierQueryDTO queryDTO);

    /**
     * 仓库地址
     *
     * @param queryDTO 查询条件
     * @return list
     */
    List<Warehouse> listWarehouseAddr(WarehouseAddrQueryDTO queryDTO);

    /**
     * 仓库关内部
     *
     * @param innerDTO 查询条件
     * @return list
     */
    List<WarehouseCO> pageWarehouses(WarehousePageQueryInnerDTO innerDTO);


    /**
     * 查询可发货仓库
     *
     * @param tenantId 租户id
     * @return 仓库
     */
    List<WarehouseRelAddressCO> selectAllDeliveryWarehouse(final Long tenantId);
}
