package org.o2.metadata.console.app.service;

import org.o2.metadata.console.api.co.OnlineShopRelWarehouseCO;
import org.o2.metadata.console.api.dto.OnlineShopRelWarehouseDTO;
import org.o2.metadata.console.api.dto.OnlineShopRelWarehouseInnerDTO;
import org.o2.metadata.console.api.vo.OnlineShopRelWarehouseVO;
import org.o2.metadata.console.infra.entity.OnlineShopRelWarehouse;

import java.util.List;
import java.util.Map;

/**
 * 网店关联服务点应用服务
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface OnlineShopRelWarehouseService {

    /**
     * 更新之前先进行校验
     * <p>
     * 数据库不能有相同的关联关系
     * 数据库必须存在对应的网店和仓库
     * </p>
     *
     * @param relationships  待创建关联关系
     * @param organizationId 租户ID
     * @return 成功后返回新列表
     */
    List<OnlineShopRelWarehouse> batchInsertSelective(Long organizationId, List<OnlineShopRelWarehouse> relationships);

    /**
     * 校验后批量更新
     * <p>
     * 数据库必须存在对应的关联关系
     * 数据库必须存在对应的网店和仓库
     * </p>
     *
     * @param relationships  待更新关联关系
     * @param organizationId 租户ID
     * @return 更新后的列表
     */
    List<OnlineShopRelWarehouse> batchUpdateByPrimaryKey(Long organizationId, List<OnlineShopRelWarehouse> relationships);

    /**
     * 查询网店关联有效仓库
     *
     * @param onlineShopCode 网店编码
     * @param tenantId       租户ID
     * @return list
     */
    List<OnlineShopRelWarehouseCO> listOnlineShopRelWarehouses(String onlineShopCode, Long tenantId);

    /**
     * 查询网店关联有效仓库
     *
     * @param innerDTO 查询参数
     * @param tenantId 租户ID
     * @return list
     */
    Map<String, List<OnlineShopRelWarehouseCO>> listOnlineShopRelWarehouses(OnlineShopRelWarehouseInnerDTO innerDTO, Long tenantId);

    /**
     * 条件查询
     *
     * @param onlineShopId              网店 id
     * @param onlineShopRelWarehouseDTO 服务点查询条件，可为空
     * @return 查询列表
     */
    List<OnlineShopRelWarehouseVO> listShopPosRelsByOption(Long onlineShopId, OnlineShopRelWarehouseDTO onlineShopRelWarehouseDTO);

    /**
     * 条件查询
     *
     * @param query 服务点查询条件，可为空
     * @return 查询列表
     */
    List<OnlineShopRelWarehouse> listByCondition(OnlineShopRelWarehouse query);

    /**
     * 条件查询网店关联仓库
     *
     * @param onlineShopRelWarehouseDTO 查询条件
     * @return 网店关联仓库
     */
    List<OnlineShopRelWarehouseVO> listShopRelWarehouse(OnlineShopRelWarehouseDTO onlineShopRelWarehouseDTO);
}
