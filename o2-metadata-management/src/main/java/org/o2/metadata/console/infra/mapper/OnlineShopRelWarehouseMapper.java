package org.o2.metadata.console.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.o2.metadata.console.infra.entity.OnlineShopRelWarehouse;
import org.o2.metadata.console.api.vo.OnlineShopRelWarehouseVO;

import java.util.List;

/**
 * 网店关联仓库Mapper
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface OnlineShopRelWarehouseMapper extends BaseMapper<OnlineShopRelWarehouse> {


    /**
     * 条件查询
     *
     * @param onlineShopId 网点 id，不能为空
     * @param warehouseCode      仓库编码，可为空
     * @param warehouseName      仓库名称，可为空
     * @param tenantId      租户ID，可为空
     * @return 仓库网店关联关系列表
     */
    List<OnlineShopRelWarehouseVO> listShopWarehouseRelsByOption(@Param("onlineShopId") Long onlineShopId,
                                                                 @Param("warehouseCode") String warehouseCode,
                                                                 @Param("warehouseName") String warehouseName,
                                                                 @Param("tenantId") Long tenantId);

    /**
     * 查询所有的关联
     * @param tenantId 租户ID
     * @return
     */
    List<OnlineShopRelWarehouse> queryAllShopRelWarehouseByTenantId(@Param("tenantId") Long tenantId);
    /**
     * 查询网店关联有效的仓库
     * @param onlineShopCode 网店编码
     * @param tenantId 租户ID
     * @return 网店关联有效的仓库集合
     */
    List<OnlineShopRelWarehouse> listOnlineShopRelWarehouses(String onlineShopCode, Long tenantId);

    /**
     *  通过网店，仓库，服务点 id 查询
     * @param query 查询条件
     * @param tenantId 租户ID
     * @return  list
     */
    List<OnlineShopRelWarehouse> selectByShopIdAndWareIdAndPosId(@Param("query")List<OnlineShopRelWarehouse> query, @Param("tenantId") Long tenantId);
}
