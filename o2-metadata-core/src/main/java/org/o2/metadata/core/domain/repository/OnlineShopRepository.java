package org.o2.metadata.core.domain.repository;

import org.o2.metadata.core.domain.entity.OnlineShop;
import org.hzero.mybatis.base.BaseRepository;
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
}
