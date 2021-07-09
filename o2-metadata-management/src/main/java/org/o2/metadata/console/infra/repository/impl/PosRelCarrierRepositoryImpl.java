package org.o2.metadata.console.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.console.infra.entity.PosRelCarrier;
import org.o2.metadata.console.infra.repository.PosRelCarrierRepository;
import org.o2.metadata.console.infra.mapper.PosRelCarrierMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 服务点关联承运商 资源库实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Component
public class PosRelCarrierRepositoryImpl extends BaseRepositoryImpl<PosRelCarrier> implements PosRelCarrierRepository {
    private final PosRelCarrierMapper posRelCarrierMapper;

    public PosRelCarrierRepositoryImpl(final PosRelCarrierMapper posRelCarrierMapper) {
        this.posRelCarrierMapper = posRelCarrierMapper;
    }

    @Override
    public List<PosRelCarrier> listCarrierWithPos(final PosRelCarrier posRelCarrier) {
        return posRelCarrierMapper.listCarrierWithPos(posRelCarrier);
    }

    @Override
    public int isExist(final PosRelCarrier posRelCarrier) {
        return posRelCarrierMapper.isExist(posRelCarrier);
    }

    @Override
    public int updateIsDefault(final Long relId, final Long posId,final Integer defaultFlag) {
        return posRelCarrierMapper.updateIsDefault(relId, posId,defaultFlag);
    }

}
