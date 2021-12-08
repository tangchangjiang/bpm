package org.o2.metadata.console.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.console.api.dto.OnlineShopRelWarehouseDTO;
import org.o2.metadata.console.infra.entity.OnlineShopRelWarehouse;
import org.o2.metadata.console.infra.repository.OnlineShopRelWarehouseRepository;
import org.o2.metadata.console.api.vo.OnlineShopRelWarehouseVO;
import org.o2.metadata.console.infra.mapper.OnlineShopRelWarehouseMapper;
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
    public List<OnlineShopRelWarehouseVO> listShopPosRelsByOption(final Long onlineShopId, final OnlineShopRelWarehouseDTO onlineShopRelWarehouseDTO) {
        Assert.notNull(onlineShopId, "online shop id could not be null");
        final Optional<OnlineShopRelWarehouseDTO> posOptional = Optional.ofNullable(onlineShopRelWarehouseDTO);
        return onlineShopRelWarehouseMapper.listShopWarehouseRelsByOption(onlineShopId,
                posOptional.map(OnlineShopRelWarehouseDTO::getWarehouseCode).orElse(null),
                posOptional.map(OnlineShopRelWarehouseDTO::getWarehouseName).orElse(null),onlineShopRelWarehouseDTO.getTenantId());
    }

    @Override
    public List<OnlineShopRelWarehouse> queryAllShopRelWarehouseByTenantId(Long tenantId) {
        return onlineShopRelWarehouseMapper.queryAllShopRelWarehouseByTenantId(tenantId);
    }

    @Override
    public List<OnlineShopRelWarehouse> selectByShopIdAndWareIdAndPosId(List<OnlineShopRelWarehouse> query, Long tenantId) {
        return onlineShopRelWarehouseMapper.selectByShopIdAndWareId(query,tenantId);
    }
}
