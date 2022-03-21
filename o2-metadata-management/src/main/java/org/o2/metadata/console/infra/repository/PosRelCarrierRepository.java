package org.o2.metadata.console.infra.repository;


import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.infra.entity.PosRelCarrier;

import java.util.List;

/**
 * 服务点关联承运商资源库
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface PosRelCarrierRepository extends BaseRepository<PosRelCarrier> {

    /**
     * 服务点关联承运商查询
     *
     * @param posRelCarrier 服务点关联承运商资源库
     * @return 结果集
     */
    List<PosRelCarrier> listCarrierWithPos(PosRelCarrier posRelCarrier);

    /**
     * 服务点关联承运商查重
     *
     * @param posRelCarrier 服务点关联承运商资源库
     * @return 结果集
     */
    int isExist(PosRelCarrier posRelCarrier);

    /**
     * 更新其他服务点默认值
     *
     * @param relId relId
     * @param posId posId
      @param defaultFlag defaultFlag
      @param tenantId 租户ID
     * @return 更新条数
     */
    int updateIsDefault(Long relId, Long posId, final Integer defaultFlag,Long tenantId );
}
