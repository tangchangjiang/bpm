package org.o2.feignclient;

import com.fasterxml.jackson.core.type.TypeReference;
import io.choerodon.core.domain.Page;
import org.hzero.core.util.ResponseUtils;
import org.o2.feignclient.metadata.domain.co.WarehouseCO;
import org.o2.feignclient.metadata.domain.dto.WarehousePageQueryInnerDTO;
import org.o2.feignclient.metadata.domain.dto.WarehouseQueryInnerDTO;
import org.o2.feignclient.metadata.infra.feign.WarehouseRemoteService;

import java.util.Map;
import java.util.Set;

/**
 *
 * 仓库
 *
 * @author yipeng.zhu@hand-china.com 2021-09-23
 **/
public class WarehouseClient {

    private final WarehouseRemoteService warehouseRemoteService;

    public WarehouseClient(WarehouseRemoteService warehouseRemoteService) {
        this.warehouseRemoteService = warehouseRemoteService;
    }

    /**
     * 查询仓库
     *
     * @param innerDTO 入参
     * @param tenantId       租户ID
     */
    public Map<String, WarehouseCO> listWarehouses(WarehouseQueryInnerDTO innerDTO, Long tenantId) {
        return ResponseUtils.getResponse(warehouseRemoteService.listWarehouses(innerDTO, tenantId), new TypeReference<Map<String, WarehouseCO>>() {
        });
    }

    /**
     * 页面查询仓库
     *
     * @param innerDTO 入参
     * @param tenantId       租户ID
     */
    public Page<WarehouseCO> pageWarehouses(WarehousePageQueryInnerDTO innerDTO, Long tenantId) {
        return ResponseUtils.getResponse(warehouseRemoteService.pageWarehouses(tenantId, innerDTO), new TypeReference<Page<WarehouseCO>>() {
        });
    }
    /**
     * 仓库快递配送接单量增量更新
     *
     * @param organizationId 租户ID
     * @param warehouseCode  仓库编码
     * @param increment      快递配送接单量增量 1 成功 -1 失败
     */
    public Integer updateExpressValue(final Long organizationId, final String warehouseCode, final String increment) {
        return ResponseUtils.getResponse(warehouseRemoteService.updateExpressValue(organizationId, warehouseCode, increment),Integer.class);
    }

    /**
     * 获取自提接单量到达上限的仓库
     *
     * @param organizationId 租户id
     * @return 自提接单量到达上限的仓库集合
     */
    public Set<String> pickUpLimitWarehouseCollection(final Long organizationId) {
        return ResponseUtils.getResponse(warehouseRemoteService.pickUpLimitWarehouseCollection(organizationId), new TypeReference<Set<String>>() {
        });
    }

    /**
     * 重置仓库快递配送接单量值
     *
     * @param organizationId 租户ID
     * @param warehouseCode  仓库编码
     */
    public Boolean resetWarehouseExpressLimit(final Long organizationId, final String warehouseCode) {
        return ResponseUtils.isFailed(warehouseRemoteService.resetWarehouseExpressLimit(organizationId, warehouseCode));
    }

    /**
     * 重置仓库自提接单量限制值
     *
     * @param organizationId 租户ID
     * @param warehouseCode  仓库编码
     */
    public Boolean resetWarehousePickUpLimit(final Long organizationId, final String warehouseCode) {
        return ResponseUtils.isFailed(warehouseRemoteService.resetWarehousePickUpLimit(organizationId, warehouseCode));
    }

    /**
     * 获取快递配送接单量到达上限的仓库
     *
     * @param organizationId 租户id
     * @return 快递配送接单量到达上限的仓库集合
     */
    public Set<String> expressLimitWarehouseCollection(final Long organizationId) {
        return ResponseUtils.getResponse(warehouseRemoteService.expressLimitWarehouseCollection(organizationId), new TypeReference<Set<String>>() {
        });
    }
}