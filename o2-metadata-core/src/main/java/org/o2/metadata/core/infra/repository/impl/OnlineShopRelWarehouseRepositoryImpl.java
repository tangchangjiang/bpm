package org.o2.metadata.core.infra.repository.impl;

import org.o2.metadata.core.domain.entity.OnlineShopRelWarehouse;
import org.o2.metadata.core.domain.vo.OnlineShopRelWarehouseVO;
import org.o2.metadata.core.infra.mapper.OnlineShopRelWarehouseMapper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.core.domain.repository.OnlineShopRelWarehouseRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

/**
 * 网店关联仓库 资源库实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Component
public class OnlineShopRelWarehouseRepositoryImpl extends BaseRepositoryImpl<OnlineShopRelWarehouse> implements OnlineShopRelWarehouseRepository {
    private final OnlineShopRelWarehouseMapper onlineShopRelWarehouseMapper;

    public OnlineShopRelWarehouseRepositoryImpl(final OnlineShopRelWarehouseMapper onlineShopRelWarehouseMapper) {
        this.onlineShopRelWarehouseMapper = onlineShopRelWarehouseMapper;
    }

    @Override
    public List<OnlineShopRelWarehouseVO> listShopPosRelsByOption(final Long onlineShopId, final OnlineShopRelWarehouseVO warehouse) {
        Assert.notNull(onlineShopId, "online shop id could not be null");
        final Optional<OnlineShopRelWarehouseVO> posOptional = Optional.ofNullable(warehouse);
        return onlineShopRelWarehouseMapper.listShopWarehouseRelsByOption(onlineShopId,
                posOptional.map(OnlineShopRelWarehouseVO::getWarehouseCode).orElse(null),
                posOptional.map(OnlineShopRelWarehouseVO::getWarehouseName).orElse(null),warehouse.getTenantId());
    }
}