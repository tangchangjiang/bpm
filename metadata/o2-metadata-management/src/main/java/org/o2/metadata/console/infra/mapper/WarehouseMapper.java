package org.o2.metadata.console.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.o2.metadata.console.api.co.WarehouseCO;
import org.o2.metadata.console.api.co.WarehouseRelAddressCO;
import org.o2.metadata.console.api.dto.*;
import org.o2.metadata.console.api.vo.WarehouseRelPosVO;
import org.o2.metadata.console.app.bo.WarehouseCacheBO;
import org.o2.metadata.console.infra.entity.Carrier;
import org.o2.metadata.console.infra.entity.Warehouse;

import java.util.List;

/**
 * 仓库信息Mapper
 *
 * @author yuying.shi@hand-china.com 2020/3/2
 */
public interface WarehouseMapper extends BaseMapper<Warehouse> {


    /**
     * 查询未与网店关联的仓库
     *
     * @param onlineShopId  网店 id
     * @param warehouseCode 仓库编码
     * @param warehouseName 仓库名称
     * @param tenantId      租户ID
     * @return 仓库列表
     */
    List<Warehouse> listUnbindWarehouseList(@Param(value = "onlineShopId") Long onlineShopId,
                                            @Param(value = "warehouseCode") String warehouseCode,
                                            @Param(value = "warehouseName") String warehouseName,
                                            @Param(value = "tenantId") Long tenantId);

    /**
     * 条件查询仓促
     *
     * @param warehouse 仓库
     * @return list
     */
    List<Warehouse> listWarehouseByCondition(Warehouse warehouse);


    /**
     * 查询租户下的所有仓库
     *
     * @param tenantId 租户ID
     * @return list
     */
    List<Warehouse> queryAllWarehouseByTenantId(@Param("tenantId") final Long tenantId);

    /**
     * 查询有效的仓库
     *
     * @param onlineShopCode 网店编码
     * @param organizationId 租户ID
     * @return list
     */
    List<Warehouse> listActiveWarehouseByShopCode(@Param("onlineShopCode") String onlineShopCode, @Param("tenantId") Long organizationId);

    /**
     * 编码查询仓促
     *
     * @param warehouseCodes 仓库
     * @param tenantId       租户ID
     * @return list
     */
    List<WarehouseCacheBO> listWarehouseByCode(@Param("warehouseCodes") List<String> warehouseCodes, @Param("tenantId") Long tenantId);

    /**
     * 查询仓库
     *
     * @param innerDTO 网店编码
     * @param tenantId 租户ID
     * @return list
     */
    List<Warehouse> listWarehouses(@Param("innerDTO") WarehouseQueryInnerDTO innerDTO, @Param("tenantId") Long tenantId);


    /**
     * 仓库关联承运商
     *
     * @param queryDTO 查询条件
     * @return list
     */
    List<Carrier> listCarriers(WarehouseRelCarrierQueryDTO queryDTO);

    /**
     * 仓库地址
     *
     * @param queryDTO 查询条件
     * @return list
     */
    List<Warehouse> listWarehouseAddr(WarehouseAddrQueryDTO queryDTO);

    /**
     * 仓库关内部
     *
     * @param innerDTO 查询条件
     * @return list
     */
    List<WarehouseCO> pageWarehouses(WarehousePageQueryInnerDTO innerDTO);

    /**
     * 查询所有服务点
     *
     * @param tenantId 租户ID
     * @return 仓库
     */
    List<WarehouseRelAddressCO> selectAllDeliveryWarehouse(@Param(value = "tenantId") final Long tenantId);


    /**
     * 查询仓库关联服务点
     * @param warehouseRelPosDTO 条件
     * @return 结果
     */
    List<WarehouseRelPosVO> listWarehouseRelPos(WarehouseRelPosDTO warehouseRelPosDTO);

    /**
     * 服务点查询仓库
     * @param posCodes 服务点编码
     * @param tenantId 租户id
     * @return 仓库
     */
    List<Warehouse> listWarehousesByPosCode(List<String> posCodes, Long tenantId);
}
