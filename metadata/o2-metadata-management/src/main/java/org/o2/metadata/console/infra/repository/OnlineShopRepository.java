package org.o2.metadata.console.infra.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.api.co.OnlineShopCO;
import org.o2.metadata.console.api.dto.OnlineShopCatalogVersionDTO;
import org.o2.metadata.console.api.dto.OnlineShopQueryInnerDTO;
import org.o2.metadata.console.infra.entity.OnlineShop;

import java.util.List;

/**
 * 网店 Repository
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface OnlineShopRepository extends BaseRepository<OnlineShop> {

    /**
     * 条件查询
     *
     * @param condition 查询条件
     * @return list of 网店
     */
    List<OnlineShop> selectByCondition(OnlineShop condition);


    /**
     * 校验网店是否已存在
     * @param condition 查询条件
     * @return the return
     * @throws RuntimeException exception description
     */
    List<OnlineShop> existenceDecide(OnlineShop condition);


    /**
     * 详情
     * @param condition 查询条件
     * @return  网店
     */
    OnlineShop selectById(OnlineShop condition);

    /**
     * 查询网店(多语言)
     * @param condition 查询条件
     * @return  网店列表
     */
    List<OnlineShop> selectShop(OnlineShop condition);

    /**
     * 更新默认网店值为空
     * @date 2020-06-03
     * @param tenantId 租户ID
    */
    void updateDefaultShop(Long tenantId);
    
    /**
     * 批量查询网店
     * @param onlineShopQueryInnerDTO 网店
     * @param tenantId 租户ID
     * @return list
     */
    List<OnlineShop> listOnlineShops(OnlineShopQueryInnerDTO onlineShopQueryInnerDTO, Long tenantId);


    /**
     * 目录+ 目录版 批量查询网店
     * @param onlineShopCatalogVersions 目录版本
     * @param tenantId 租户ID
     * @return list
     */
    List<OnlineShop> listOnlineShops(List<OnlineShopCatalogVersionDTO> onlineShopCatalogVersions, Long tenantId);

    /**
     * 查询网店排序条件
     *
     * @param onlineShopQueryInnerDTO 查询条件
     * @param tenantId 租户id
     * @return OnlineShopCO
     */
    List<OnlineShopCO> queryOnlineShops(OnlineShopQueryInnerDTO onlineShopQueryInnerDTO, Long tenantId);
}
