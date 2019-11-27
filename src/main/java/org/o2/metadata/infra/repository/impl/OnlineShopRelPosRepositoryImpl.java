package org.o2.metadata.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.ext.metadata.domain.entity.OnlineShopRelPos;
import org.o2.ext.metadata.domain.repository.OnlineShopRelPosRepository;
import org.o2.ext.metadata.domain.vo.OnlineShopRelPosVO;
import org.o2.ext.metadata.infra.mapper.OnlineShopRelPosMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

/**
 * 网店关联服务点 资源库实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Component
public class OnlineShopRelPosRepositoryImpl extends BaseRepositoryImpl<OnlineShopRelPos> implements OnlineShopRelPosRepository {
    private final OnlineShopRelPosMapper onlineShopRelPosMapper;

    public OnlineShopRelPosRepositoryImpl(final OnlineShopRelPosMapper onlineShopRelPosMapper) {
        this.onlineShopRelPosMapper = onlineShopRelPosMapper;
    }

    @Override
    public List<OnlineShopRelPosVO> listShopPosRelsByOption(final Long onlineShopId, final OnlineShopRelPosVO pos) {
        Assert.notNull(onlineShopId, "online shop id could not be null");
        final Optional<OnlineShopRelPosVO> posOptional = Optional.ofNullable(pos);
        return onlineShopRelPosMapper.listShopPosRelsByOption(onlineShopId,
                posOptional.map(OnlineShopRelPosVO::getPosCode).orElse(null),
                posOptional.map(OnlineShopRelPosVO::getPosName).orElse(null));
    }
}
