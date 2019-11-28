package org.o2.metadata.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.o2.metadata.domain.entity.OnlineShop;

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
}
