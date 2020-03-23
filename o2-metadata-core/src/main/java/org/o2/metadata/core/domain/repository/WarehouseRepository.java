package org.o2.metadata.core.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.core.domain.entity.Warehouse;

import java.util.List;


/**
 * 仓库信息资源库
 *
 * @author yuying.shi@hand-china.com 2020/3/2
 */
public interface WarehouseRepository extends BaseRepository<Warehouse> {
//    /**
//     * 主键查询
//     * @param tenantId
//     * @param posId 服务点 id
//     * @return 带详细地址和接派单时间的服务点信息
//     */
//    Warehouse getWarehouseWithCarrierNameById(Long tenantId, Long posId);

    /**
     * 查询未与网店关联的仓库
     *
     * @param shopId   网店 id
     * @param warehouseCode  仓库编码
     * @param warehouseName  仓库名称
     * @param tenantId 租户id
     * @return 仓库列表
     */
    List<Warehouse> listUnbindWarehouseList(Long shopId, String warehouseCode, String warehouseName, Long tenantId);

    List<Warehouse> listWarehouseByCondition(Warehouse warehouse);
}
