package org.o2.metadata.console.infra.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.api.dto.OnlineShopDTO;
import org.o2.metadata.console.api.vo.OnlineShopVO;
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
    OnlineShop selectById(final OnlineShop condition);

    /**
     * 查询网店(多语言)
     * @param condition 查询条件
     * @return  网店列表
     */
    List<OnlineShop> selectShop(final OnlineShop condition);
    /**
     * 更新默认网店值为空
     * @date 2020-06-03
     * @param tenantId 租户ID
\    */
    void updateDefaultShop(final Long tenantId);
    
    /**
     * 批量查询网店
     * @param onlineShopDTO 网店
     * @param tenantId 租户ID
     * @return list
     */
    List<OnlineShop> listOnlineShops(OnlineShopDTO onlineShopDTO, Long tenantId);

    /**
     * 根据网店名称查询网店code
     * @param onlineShop
     * @return List<OnlineShop> 结果
     */
    List<OnlineShop> getShopCode(OnlineShop onlineShop);
}
