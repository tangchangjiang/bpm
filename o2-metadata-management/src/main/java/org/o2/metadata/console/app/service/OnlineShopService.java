package org.o2.metadata.console.app.service;

import org.o2.metadata.console.api.co.OnlineShopCO;
import org.o2.metadata.console.api.dto.OnlineShopCatalogVersionDTO;
import org.o2.metadata.console.api.dto.OnlineShopQueryInnerDTO;
import org.o2.metadata.console.infra.entity.OnlineShop;
import org.o2.metadata.management.client.domain.dto.OnlineShopDTO;

import java.util.List;
import java.util.Map;

/**
 * 网店应用服务
 * @author yipeng.zhu@hand-china.com 2020-06-03 09:50
 **/
public interface OnlineShopService {
    /**
     * 条件查询
     *
     * @param condition 查询条件
     * @return list of 网店
     */
    List<OnlineShop> selectByCondition(OnlineShop condition);
    /**
     * 创建网店
     * @date 2020-06-03
     * @param onlineShop 网店信息
     */
    OnlineShop createOnlineShop(final OnlineShop onlineShop);
    /**
     * 修改网店
     * @date 2020-06-03
     * @param  onlineShop 网店信息
     */
    OnlineShop updateOnlineShop(final  OnlineShop onlineShop);

    /**
     * 批量查询网店
     * @param  onlineShopQueryInnerDTO 网店
     * @param tenantId 租户ID
     * @return list
     */
    Map<String, OnlineShopCO> listOnlineShops(OnlineShopQueryInnerDTO onlineShopQueryInnerDTO, Long tenantId);

    /**
     * 目录版本+目录 批量查询网店
     * @param onlineShopCatalogVersionList 目录版本
     * @param tenantId 租户id
     * @return list
     */
    Map<String,List<OnlineShopCO>> listOnlineShops(List<OnlineShopCatalogVersionDTO> onlineShopCatalogVersionList, Long tenantId);

    /**
     * 保存网店
     * @param onlineShopDTO 网店
     * @return 网店
     */
    OnlineShopDTO saveOnlineShop(OnlineShopDTO onlineShopDTO);
}
