package org.o2.metadata.console.app.service;

import org.o2.metadata.console.api.dto.WarehouseAddrQueryDTO;
import org.o2.metadata.console.api.dto.WarehouseQueryInnerDTO;
import org.o2.metadata.console.api.dto.WarehouseRelCarrierQueryDTO;
import org.o2.metadata.console.api.vo.WarehouseCO;
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
     * 批量操作仓库
     * @date 2020-05-22
     * @param tenantId 租户ID
     * @param warehouses  仓库
     * @return  list 仓库集合
     */
    List<Warehouse> batchHandle(Long tenantId, List<Warehouse> warehouses);

    /**
     * 获取仓库信息
     * @param  innerDTO 仓库
     * @param  tenantId 租户ID
     * @return 仓库
     */
    List<WarehouseCO> listWarehouses(WarehouseQueryInnerDTO innerDTO, Long tenantId);


    /**
     * 保存仓库快递配送接单量限制
     *
     * @param tenantId        租户ID
     * @param warehouseCode   仓库编码
     * @param expressQuantity 快递配送接单量限制
     */
    void saveExpressQuantity(String warehouseCode, String expressQuantity, Long tenantId);

    /**
     * 保存仓库自提接单量限制
     *
     * @param tenantId       租户ID
     * @param warehouseCode  仓库编码
     * @param pickUpQuantity 自提单量限制
     */
    void savePickUpQuantity(String warehouseCode, String pickUpQuantity, Long tenantId);

    /**
     * 仓库快递配送接单量增量更新
     *
     * @param tenantId       租户ID
     * @param warehouseCode  仓库编码
     * @param increment      快递配送接单量增量
     */
    void updateExpressValue(String warehouseCode, String increment, Long tenantId);

    /**
     * 仓库自提接单量增量更新
     *
     * @param tenantId      租户ID
     * @param warehouseCode 仓库编码
     * @param increment     自提单量增量
     */
    void updatePickUpValue(String warehouseCode, String increment, Long tenantId);

    /**
     * 获取快递配送接单量限制
     *
     * @param tenantId      租户ID
     * @param warehouseCode 仓库编码
     * @return 快递配送接单量限制
     */
    String getExpressLimit(String warehouseCode, Long tenantId);

    /**
     * 获取自提接单量限制
     *
     * @param tenantId       租户ID
     * @param warehouseCode  仓库编码
     * @return 自提接单量限制
     */
    String getPickUpLimit(String warehouseCode, Long tenantId);

    /**
     * 获取实际快递配送接单量
     *
     * @param tenantId       租户ID
     * @param warehouseCode  仓库编码
     * @return 实际快递配送接单量
     */
    String getExpressValue(String warehouseCode, Long tenantId);

    /**
     * 获取实际自提接单量
     *
     * @param tenantId       租户ID
     * @param warehouseCode  仓库编码
     * @return 实际自提接单量
     */
    String getPickUpValue(String warehouseCode, Long tenantId);

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
}
