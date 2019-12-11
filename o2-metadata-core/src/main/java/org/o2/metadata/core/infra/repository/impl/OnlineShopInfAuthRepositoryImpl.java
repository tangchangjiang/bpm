package org.o2.metadata.core.infra.repository.impl;

import org.o2.metadata.core.domain.entity.OnlineShopInfAuth;
import org.o2.metadata.core.infra.mapper.OnlineShopInfAuthMapper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.core.domain.entity.OnlineShop;
import org.o2.metadata.core.domain.repository.OnlineShopInfAuthRepository;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 网店接口表 资源库实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Component
public class OnlineShopInfAuthRepositoryImpl extends BaseRepositoryImpl<OnlineShopInfAuth> implements OnlineShopInfAuthRepository {
    private final OnlineShopInfAuthMapper onlineShopInfAuthMapper;

    public OnlineShopInfAuthRepositoryImpl(final OnlineShopInfAuthMapper onlineShopInfAuthMapper) {
        this.onlineShopInfAuthMapper = onlineShopInfAuthMapper;
    }

    @Override
    public OnlineShopInfAuth listOnlineShopInfAuthByOption(final Long onlineShopId) {
        return onlineShopInfAuthMapper.listOnlineShopInfAuthByOption(onlineShopId);
    }

    @Override
    public List<OnlineShopInfAuth> listInfAuthByOnlineShop(final OnlineShop onlineShop) {
        return onlineShopInfAuthMapper.listInfAuthByOnlineShop(onlineShop);
    }
}
