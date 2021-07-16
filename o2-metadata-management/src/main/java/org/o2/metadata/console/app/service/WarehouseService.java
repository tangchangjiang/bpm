package org.o2.metadata.console.app.service;

import org.o2.metadata.console.api.vo.WarehouseVO;
import org.o2.metadata.console.infra.entity.Warehouse;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author NieYong
 * @Title WarehouseService
 * @Description
 * @date 2020/3/4 13:29
 **/

public interface WarehouseService {

    Integer create(Long tenantId, Warehouse warehouse);

    List<Warehouse> createBatch(Long tenantId, List<Warehouse> warehouses);

    Integer update(Warehouse warehouse);

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
     * @param  warehouseCode 仓库编码
     * @param  tenantId 租户ID
     * @return
     */
    WarehouseVO getWarehouse(String warehouseCode,Long tenantId);


    /**
     * 保存
     * @param hashMap       map
     * @param warehouseCode 仓库编码
     * @param tenantId      租户Id
     */
    void saveWarehouse (String warehouseCode, Map<String,Object> hashMap, Long tenantId);


    /**
     * 更新
     * @param warehouseCode
     * @param hashMap
     * @param tenantId
     */
    void updateWarehouse (String warehouseCode,Map<String,Object> hashMap, Long tenantId);

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
     * 仓库缓存KEY
     *
     * @param tenantId      租户ID
     * @param warehouseCode 仓库编码
     * @return 仓库缓存KEY
     */
    String warehouseCacheKey(String warehouseCode, Long tenantId);

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
     * 查询有效的仓库
     * @param onlineShopCode 网店编码
     * @param organizationId 租户ID
     * @return
     */
    List<WarehouseVO> listActiveWarehouses(String onlineShopCode, Long organizationId);
}
