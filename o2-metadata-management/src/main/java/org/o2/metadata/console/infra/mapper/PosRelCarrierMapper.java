package org.o2.metadata.console.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.o2.metadata.console.domain.entity.PosRelCarrier;

import java.util.List;

/**
 * 服务点关联承运商Mapper
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface PosRelCarrierMapper extends BaseMapper<PosRelCarrier> {
    /**
     * 服务点关联承运商查询
     *
     * @param posRelCarrier 服务点关联承运商资源库
     * @return 服务点关联承运商列表
     */
    List<PosRelCarrier> listCarrierWithPos(PosRelCarrier posRelCarrier);

    /**
     * 服务点关联承运商查重
     *
     * @param posRelCarrier 服务点关联承运商资源库
     * @return 满足条件条数
     */
    int isExist(PosRelCarrier posRelCarrier);

    /**
     * 更新其他服务点默认值
     *
     * @param relId relId
     * @param posId posId
     * @param defaultFlag defaultFlag
     * @return 更新条数
     */
    int updateIsDefault(@Param("relId") Long relId, @Param("posId") Long posId, @Param("defaultFlag") Integer defaultFlag);
}
