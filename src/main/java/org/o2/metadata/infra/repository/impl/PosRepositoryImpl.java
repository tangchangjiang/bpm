package org.o2.metadata.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.domain.entity.Pos;
import org.o2.metadata.domain.entity.PostTime;
import org.o2.metadata.domain.repository.PosRepository;
import org.o2.metadata.domain.vo.PosVO;
import org.o2.metadata.infra.mapper.PosAddressMapper;
import org.o2.metadata.infra.mapper.PosMapper;
import org.o2.metadata.infra.mapper.PostTimeMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 服务点信息 资源库实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Component
public class PosRepositoryImpl extends BaseRepositoryImpl<Pos> implements PosRepository {
    private final PosMapper posMapper;
    private final PosAddressMapper posAddressMapper;
    private final PostTimeMapper postTimeMapper;

    public PosRepositoryImpl(final PosMapper posMapper, final PosAddressMapper posAddressMapper, final PostTimeMapper postTimeMapper) {
        this.posMapper = posMapper;
        this.posAddressMapper = posAddressMapper;
        this.postTimeMapper = postTimeMapper;
    }

    @Override
    public List<PosVO> listPosWithAddressByCondition(final PosVO pos) {
        return posMapper.listPosWithAddressByCondition(pos);
    }

    @Override
    public Pos getPosWithAddressAndPostTimeByPosId(final Long posId) {
        final Pos pos = posMapper.getPosWithCarrierNameById(posId);
        //Assert.notNull(pos.getAddressId(), "pos must contains a address id");
        if (pos.getAddressId() != null) {
            pos.setAddress(posAddressMapper.selectByPrimaryKey(pos.getAddressId()));
        }

        // 接派单时间
        final PostTime postTime = new PostTime();
        postTime.setPosId(posId);
        final List<PostTime> postTimes = postTimeMapper.select(postTime);
        pos.setPostTimes(postTimes);
        return pos;
    }

    @Override
    public List<Pos> listUnbindPosList(final Long shopId, final String posCode, final String posName) {
        return posMapper.listUnbindPosList(shopId, posCode, posName);
    }

    @Override
    public Pos getPosByCode(final String posCode) {
        final Pos pos = new Pos();
        pos.setPosCode(posCode);
        return posMapper.selectOne(pos);
    }
}
