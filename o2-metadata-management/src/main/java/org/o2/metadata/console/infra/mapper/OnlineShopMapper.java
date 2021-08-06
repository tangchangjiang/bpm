package org.o2.metadata.console.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.o2.metadata.console.api.dto.OnlineShopDTO;
import org.o2.metadata.console.infra.entity.OnlineShop;

import java.util.List;

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
     * @param condition 查询条件
     * @return the return
     * @throws RuntimeException exception description
     */
    List<OnlineShop> existenceDecide(OnlineShop condition);

    /**
     * 查询网店(多语言)
     * @date 2020-04-26
     * @param  condition 查询条件
     * @return 网店列表
     */
    List<OnlineShop> selectShop(final OnlineShop condition);
    /**
     * 更新默认网店值为空
     * @date 2020-06-03
     * @param tenantId 租户ID
     */
    void updateDefaultShop(@Param("tenantId") final Long tenantId);

    /**
     * 批量查询网店
     * @param  onlineShopDTO 网店
     * @param tenantId 租户ID
     * @return list
     */
    List<OnlineShop> listOnlineShops(@Param("onlineShopDTO") OnlineShopDTO onlineShopDTO,@Param("tenantId") Long tenantId);

    /**
     * 根据网店名称查询网店code
     * @param onlineShop 查询条件
     * @return List<OnlineShop> 结果
     */
    List<OnlineShop> getShopCode(OnlineShop onlineShop);
}
