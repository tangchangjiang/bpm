package org.o2.metadata.infra.repository.impl;

import com.google.common.base.Preconditions;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.domain.entity.OnlineShop;
import org.o2.metadata.domain.repository.OnlineShopRepository;
import org.o2.metadata.infra.constants.BasicDataConstants;
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
        Preconditions.checkArgument(null != condition.getTenantId(), BasicDataConstants.ErrorCode.BASIC_DATA_TENANT_ID_IS_NULL);
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
}
