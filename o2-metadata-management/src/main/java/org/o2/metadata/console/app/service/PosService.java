package org.o2.metadata.console.app.service;


import org.o2.metadata.console.domain.entity.Pos;

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
    Pos getPosWithPropertiesInRedisByPosId(Long organizationId, Long posId);

}
