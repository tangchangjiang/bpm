package org.o2.metadata.console.domain.repository;


import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.infra.entity.Carrier;

import java.util.List;

/**
 * 承运商资源库
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface CarrierRepository extends BaseRepository<Carrier> {

    /**
     * 承运商资源库查询
     *
     * @param carrier 承运商资源库
     * @return 承运商资源库
     */
    List<Carrier> listCarrier(Carrier carrier);
}
