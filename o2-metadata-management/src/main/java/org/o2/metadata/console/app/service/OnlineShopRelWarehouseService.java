package org.o2.metadata.console.app.service;

import org.o2.metadata.console.domain.entity.OnlineShopRelWarehouse;

import java.util.List;

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
     * @param relationships 待创建关联关系
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
     * @param relationships 待更新关联关系
     * @param organizationId 租户ID
     * @return 更新后的列表
     */
    List<OnlineShopRelWarehouse> batchUpdateByPrimaryKey(Long organizationId, List<OnlineShopRelWarehouse> relationships);


    /**
     * 重新设置'是否计算库存'字段
     * <p>
     * 满足以下条件，设置为1
     * 1.网店关联POS有效
     * 2.网店有效
     * 3.POS状态为正常
     * 4.如果POS为门店，需要满足POS可快递发货且接单量未达到上限
     * </p>
     *
     * @param onlineShopCode 网店
     * @param warehouseCode  仓库
     * @param tenantId      租户ID
     * @return 更新后的列表
     */
    List<OnlineShopRelWarehouse> resetIsInvCalculated(final String onlineShopCode, final String warehouseCode, final Long tenantId);

    void updateByShop(Long onlineShopId, String onlineShopCode, Integer activeFlag, Long tenantId);
}
