package org.o2.metadata.console.infra.mapper;

import org.apache.ibatis.annotations.Param;
import org.o2.metadata.console.api.co.OnlineShopCO;
import org.o2.metadata.console.api.dto.OnlineShopCatalogVersionDTO;
import org.o2.metadata.console.api.dto.OnlineShopQueryInnerDTO;
import org.o2.metadata.console.infra.entity.OnlineShop;
import java.util.List;

import io.choerodon.mybatis.common.BaseMapper;

/**
 * 网店 Mapper
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface OnlineShopMapper extends BaseMapper<OnlineShop> {
    /**
     * 查询满足条件的网店集合
     *
     * @param onlineShop 网店
     * @return list of onlineShop
     */
    List<OnlineShop> findByCondition(OnlineShop onlineShop);

    /**
     * 校验网店是否已存在
     *
     * @param condition 查询条件
     * @return the return
     * @throws RuntimeException exception description
     */
    List<OnlineShop> existenceDecide(OnlineShop condition);

    /**
     * 查询网店(多语言)
     *
     * @param condition 查询条件
     * @return 网店列表
     * @date 2020-04-26
     */
    List<OnlineShop> selectShop(OnlineShop condition);

    /**
     * 更新默认网店值为空
     *
     * @param tenantId 租户ID
     * @date 2020-06-03
     */
    void updateDefaultShop(@Param("tenantId") Long tenantId);

    /**
     * 批量查询网店
     *
     * @param onlineShopQueryInnerDTO 网店
     * @param tenantId                租户ID
     * @param siteFlag                是否站点级查询
     * @return list
     */
    List<OnlineShop> listOnlineShops(@Param("onlineShopDTO") OnlineShopQueryInnerDTO onlineShopQueryInnerDTO, @Param("tenantId") Long tenantId, @Param("siteFlag") Integer siteFlag);

    /**
     * 批量查询网店
     *
     * @param onlineShopCatalogVersions 网店
     * @param tenantId                  租户ID
     * @return list
     */
    List<OnlineShop> listOnlineShopByCatalogCodes(@Param("onlineShopCatalogVersions") List<OnlineShopCatalogVersionDTO> onlineShopCatalogVersions,
                                                  @Param("tenantId") Long tenantId);

    /**
     * 查询网店排除条件
     *
     * @param onlineShopQueryInnerDTO 查询条件
     * @param tenantId 租户id
     * @return OnlineShopCO
     */
    List<OnlineShopCO> queryOnlineShops(@Param("tenantId") Long tenantId, @Param("onlineShopQueryInnerDTO") OnlineShopQueryInnerDTO onlineShopQueryInnerDTO);
}
