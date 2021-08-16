package org.o2.metadata.console.app.service;


import org.o2.metadata.console.api.dto.PosAddressQueryInnerDTO;
import org.o2.metadata.console.api.vo.PosAddressVO;
import org.o2.metadata.console.api.vo.PosVO;
import org.o2.metadata.console.infra.entity.Pos;

import java.util.List;

/**
 * 服务点信息应用服务
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface PosService {

    /**
     * 新增一个服务点信息
     *
     * @param pos 可以带有 地址 和 接派单时间 属性
     * @return 带 id 的服务点信息
     */
    Pos create(Pos pos);

    /**
     * 更新服务点信息
     *
     * @param pos 待更新服务点，id 不能为空
     * @return 更新后的服务点
     */
    Pos update(Pos pos);

    /**
     * 查询服务点信息，并获取 redis 中的热数据
     * @param organizationId Long
     * @param posId Long
     * @return Pos
     */
    PosVO getPosWithPropertiesInRedisByPosId(Long organizationId, Long posId);
    
    /**
     * 查询服务地址
     * @param posAddressQueryInnerDTO 服务点地址
     * @param tenantId 租户ID
     * @return  list
     */
    List<PosAddressVO> listPosAddress(PosAddressQueryInnerDTO posAddressQueryInnerDTO, Long tenantId);
}
