package org.o2.metadata.console.infra.repository.impl;

import com.google.common.base.Preconditions;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.console.api.dto.OnlineShopCatalogVersionDTO;
import org.o2.metadata.console.api.dto.OnlineShopQueryInnerDTO;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.entity.OnlineShop;
import org.o2.metadata.console.infra.repository.OnlineShopRepository;
import org.o2.metadata.console.infra.mapper.OnlineShopMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 网店 Repository 默认实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Repository
class OnlineShopRepositoryImpl extends BaseRepositoryImpl<OnlineShop> implements OnlineShopRepository {
    private final OnlineShopMapper onlineShopMapper;

    public OnlineShopRepositoryImpl(final OnlineShopMapper onlineShopMapper) {
        this.onlineShopMapper = onlineShopMapper;
    }

    @Override
    public List<OnlineShop> selectByCondition(final OnlineShop condition) {
        Preconditions.checkArgument(null != condition.getTenantId(), MetadataConstants.ErrorCode.BASIC_DATA_TENANT_ID_IS_NULL);
        return onlineShopMapper.findByCondition(condition);
    }

    /**
     * 校验网店是否已存在
     * @param condition 查询条件
     * @return the return
     * @throws RuntimeException exception description
     */
    @Override
    public List<OnlineShop> existenceDecide(OnlineShop condition) {
        return onlineShopMapper.existenceDecide(condition);
    }

    @Override
    public OnlineShop selectById(final OnlineShop condition) {
        OnlineShop shop = new OnlineShop();
        List<OnlineShop> list = onlineShopMapper.findByCondition(condition);
        if (CollectionUtils.isNotEmpty(list)) {
            shop = list.get(0);
        }
        return shop;
    }

    @Override
    public List<OnlineShop> selectShop(final OnlineShop condition) {
        return onlineShopMapper.selectShop(condition);
    }

    @Override
    public void updateDefaultShop(final Long tenantId) {
        onlineShopMapper.updateDefaultShop(tenantId);
    }

    @Override
    public List<OnlineShop> listOnlineShops(OnlineShopQueryInnerDTO onlineShopQueryInnerDTO, Long tenantId) {
        return onlineShopMapper.listOnlineShops(onlineShopQueryInnerDTO,tenantId);
    }

    @Override
    public List<OnlineShop> listOnlineShops(List<OnlineShopCatalogVersionDTO> onlineShopCatalogVersions, Long tenantId) {
        return onlineShopMapper.listOnlineShopByCatalogCodes(onlineShopCatalogVersions,tenantId);
    }

}
