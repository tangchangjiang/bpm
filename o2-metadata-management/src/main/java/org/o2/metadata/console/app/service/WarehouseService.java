package org.o2.metadata.console.app.service;

import org.o2.metadata.console.api.co.WarehouseCO;
import org.o2.metadata.console.api.co.WarehouseRelAddressCO;
import org.o2.metadata.console.api.dto.WarehouseAddrQueryDTO;
import org.o2.metadata.console.api.dto.WarehousePageQueryInnerDTO;
import org.o2.metadata.console.api.dto.WarehouseQueryInnerDTO;
import org.o2.metadata.console.api.dto.WarehouseRelCarrierQueryDTO;
import org.o2.metadata.console.infra.entity.Carrier;
import org.o2.metadata.console.infra.entity.Warehouse;

import java.util.List;
import java.util.Set;

/**
 * @author NieYong
 * @Description 仓库
 **/

public interface WarehouseService {
    
    /**
     * 批量插入仓库信息
     * @param warehouses 仓库集合
     * @param tenantId 租户ID
     * @return list
     */
    List<Warehouse> createBatch(Long tenantId, List<Warehouse> warehouses);


    /**
     * 批量更新仓库
     * @param warehouses 仓库集合
     * @param tenantId 租户ID
     * @return  list
     */
    List<Warehouse> updateBatch(Long tenantId, List<Warehouse> warehouses);

    /**
     *触发线上可用库存计算
     * @param  oldWarehouses 仓库更新前
     * @param  newWarehouses 仓库更新后
     * @param  tenantId 租户id
     */
    void triggerWhStockCalWithWh(Long tenantId,List<Warehouse> oldWarehouses,List<Warehouse> newWarehouses);


    /**
     * 获取仓库信息
     * @param  innerDTO 仓库
     * @param  tenantId 租户ID
     * @return 仓库
     */
    List<WarehouseCO> listWarehouses(WarehouseQueryInnerDTO innerDTO, Long tenantId);


    /**
     * 仓库快递配送接单量增量更新
     *
     * @param tenantId       租户ID
     * @param warehouseCode  仓库编码
     * @param increment      快递配送接单量增量
     * @return 1 成功 -1 失败
     */
    Long updateExpressValue(String warehouseCode, String increment, Long tenantId);

    /**
     * 仓库自提接单量增量更新
     *
     * @param tenantId      租户ID
     * @param warehouseCode 仓库编码
     * @param increment     自提单量增量
     * @return 1 成功 -1 失败
     */
    Long updatePickUpValue(String warehouseCode, String increment, Long tenantId);

    /**
     * 仓库limit缓存KEY
     *
     * @param tenantId      租户ID
     * @param limit limit
     * @return 仓库limit缓存KEY
     */
    String warehouseLimitCacheKey(String limit, Long tenantId);

    /**
     * 是否仓库快递配送接单量到达上限
     *
     * @param tenantId      租户ID
     * @param warehouseCode 仓库编码
     * @return 结果(true : 到达上限)
     */
    boolean isWarehouseExpressLimit(String warehouseCode, Long tenantId);

    /**
     * 是否仓库自提接单量到达上限
     *
     * @param tenantId       租户ID
     * @param warehouseCode  仓库编码
     * @return 结果(true : 到达上限)
     */
    boolean isWarehousePickUpLimit(String warehouseCode, Long tenantId);

    /**
     * 获取快递配送接单量到达上限的仓库
     * @param tenantId 租户id
     * @return 快递配送接单量到达上限的仓库集合
     */
    Set<String> expressLimitWarehouseCollection(Long tenantId);

    /**
     * 获取自提接单量到达上限的仓库
     * @param tenantId 租户id
     * @return 自提接单量到达上限的仓库集合
     */
    Set<String> pickUpLimitWarehouseCollection(Long tenantId);

    /**
     * 重置仓库快递配送接单量值
     *
     * @param tenantId      租户ID
     * @param warehouseCode 仓库编码
     */
    void resetWarehouseExpressLimit(String warehouseCode, Long tenantId);

    /**
     * 重置仓库自提接单量限制值
     *
     * @param tenantId      租户ID
     * @param warehouseCode 仓库编码
     */
    void resetWarehousePickUpLimit(String warehouseCode, Long tenantId);

    /**
     * 仓库关联承运商
     * @param queryDTO 查询条件
     * @return list
     */
    List<Carrier> listCarriers(WarehouseRelCarrierQueryDTO queryDTO);

    /**
     * 仓库地址
     * @param queryDTO 查询条件
     * @return list
     */
    List<Warehouse> listWarehouseAddr(WarehouseAddrQueryDTO queryDTO);

    /**
     * 仓库关内部
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
