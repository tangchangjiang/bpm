package org.o2.metadata.core.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.o2.metadata.core.domain.entity.Pos;
import org.o2.metadata.core.domain.vo.PosVO;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 服务点信息Mapper
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface PosMapper extends BaseMapper<Pos> {

    /**
     * 查询字段：
     * 编码 | 名称 | 服务点类型 | 门店类型 | 门店发货 | 门点自提 | 省 | 市 | 区
     * <p/>
     *
     * @param pos 服务点信息，包含地址省市区字段
     * @return 带详细地址信息的服务点列表
     */
    List<PosVO> listPosWithAddressByCondition(PosVO pos);

    /**
     * 查询未与网店关联的服务点
     *
     * @param onlineShopId 网店 id
     * @param posCode
     * @param posName
     * @param tenantId
     * @return 服务点列表
     */
    List<Pos> listUnbindPosList(@Param(value = "onlineShopId") Long onlineShopId,
                                @Param(value = "posCode") String posCode,
                                @Param(value = "posName") String posName,
                                @Param(value = "tenantId") Long tenantId);

    /**
     * 主键查询
     *
     * @param posId 服务点 id
     * @return 带详细地址和接派单时间的服务点信息
     */
    Pos getPosWithCarrierNameById(Long posId);

}