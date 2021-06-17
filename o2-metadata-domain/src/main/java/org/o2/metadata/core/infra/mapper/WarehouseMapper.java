package org.o2.metadata.core.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.o2.metadata.core.domain.entity.Warehouse;

import java.util.List;

/**
 * 仓库信息Mapper
 *
 * @author yuying.shi@hand-china.com 2020/3/2
 */
public interface WarehouseMapper extends BaseMapper<Warehouse> {

    /**
     * 主键查询
     * @param tenantId
     * @param posId 服务点 id
     * @return 带详细地址和接派单时间的仓库信息
     */
    Warehouse getWarehouseWithCarrierNameById(Long tenantId,Long posId);

    /**
     * 查询未与网店关联的仓库
     *
     * @param onlineShopId 网店 id
     * @param warehouseCode
     * @param warehouseName
     * @param tenantId
     * @return 仓库列表
     */
    List<Warehouse> listUnbindWarehouseList(@Param(value = "onlineShopId") Long onlineShopId,
                                @Param(value = "warehouseCode") String warehouseCode,
                                @Param(value = "warehouseName") String warehouseName,
                                @Param(value = "tenantId") Long tenantId);

    List<Warehouse> listWarehouseByCondition(Warehouse warehouse);


    /**
     * 查询租户下的所有仓库
     * @param tenantId 租户ID
     * @return
     */
    List<Warehouse> queryAllWarehouseByTenantId (@Param("tenantId") final Long tenantId);
}