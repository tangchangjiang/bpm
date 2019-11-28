package org.o2.metadata.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.domain.entity.OnlineShop;
import org.o2.metadata.domain.repository.OnlineShopRepository;
import org.o2.metadata.infra.mapper.OnlineShopMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 网店 Repository 默认实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Repository
public class OnlineShopRepositoryImpl extends BaseRepositoryImpl<OnlineShop> implements OnlineShopRepository {
    private final OnlineShopMapper onlineShopMapper;

    public OnlineShopRepositoryImpl(final OnlineShopMapper onlineShopMapper) {
        this.onlineShopMapper = onlineShopMapper;
    }

    @Override
    public List<OnlineShop> selectByCondition(final OnlineShop condition) {
        return onlineShopMapper.findByCondition(condition);
    }
}
