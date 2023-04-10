package org.o2.metadata.management.client;

import com.fasterxml.jackson.core.type.TypeReference;
import io.choerodon.core.domain.Page;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.util.ResponseUtils;
import org.o2.core.helper.O2ResponseUtils;
import org.o2.metadata.management.client.domain.co.OnlineShopCO;
import org.o2.metadata.management.client.domain.co.OnlineShopRelWarehouseCO;
import org.o2.metadata.management.client.domain.dto.OnlineShopCatalogVersionDTO;
import org.o2.metadata.management.client.domain.dto.OnlineShopDTO;
import org.o2.metadata.management.client.domain.dto.OnlineShopQueryInnerDTO;
import org.o2.metadata.management.client.domain.dto.OnlineShopRelWarehouseInnerDTO;
import org.o2.metadata.management.client.infra.feign.OnlineShopRemoteService;

import java.util.List;
import java.util.Map;

/**
 * 网店
 *
 * @author yipeng.zhu@hand-china.com 2021-09-23
 **/
public class OnlineShopClient {

    private final OnlineShopRemoteService onlineShopRemoteService;

    public OnlineShopClient(OnlineShopRemoteService onlineShopRemoteService) {
        this.onlineShopRemoteService = onlineShopRemoteService;
    }

    /**
     * 查询单个网店关联有效的仓库
     *
     * @param onlineShopCode 网店编码
     * @param tenantId       租户ID
     * @return map<warehouseCode, OnlineShopRelWarehouseCO>
     */
    public Map<String, OnlineShopRelWarehouseCO> listOnlineShopRelWarehouses(String onlineShopCode, Long tenantId) {
        return ResponseUtils.getResponse(onlineShopRemoteService.listOnlineShopRelWarehouses(onlineShopCode, tenantId),
                new TypeReference<Map<String, OnlineShopRelWarehouseCO>>() {
                });
    }

    /**
     * 查询多个网店关联有效的仓库
     *
     * @param innerDTO 网店编码
     * @param tenantId 租户ID
     * @return map<warehouseCode, OnlineShopRelWarehouseCO>
     */
    public Map<String, List<OnlineShopRelWarehouseCO>> listOnlineShopRelWarehouses(OnlineShopRelWarehouseInnerDTO innerDTO, Long tenantId) {
        return ResponseUtils.getResponse(onlineShopRemoteService.listOnlineShopRelWarehouses(innerDTO, tenantId), new TypeReference<Map<String,
                List<OnlineShopRelWarehouseCO>>>() {
        });
    }

    /**
     * 批量查询网店
     *
     * @param onlineShopQueryInnerDTO 网店
     * @return map 通过名称查询 key:onlineShopName ; 通过code查询 key:onlineShopCode
     */
    public Map<String, OnlineShopCO> listOnlineShops(OnlineShopQueryInnerDTO onlineShopQueryInnerDTO, Long tenantId) {
        return ResponseUtils.getResponse(onlineShopRemoteService.listOnlineShops(onlineShopQueryInnerDTO, tenantId), new TypeReference<Map<String,
                OnlineShopCO>>() {
        });
    }

    /**
     * 批量查询网店-全租户查询
     *
     * @param onlineShopQueryInnerDTO 网店
     * @return map 通过名称查询 key:onlineShopName ; 通过code查询 key:onlineShopCode
     */
    public Map<String, OnlineShopCO> listOnlineShops(OnlineShopQueryInnerDTO onlineShopQueryInnerDTO) {
        // 传递默认租户，无使用
        return ResponseUtils.getResponse(onlineShopRemoteService.listOnlineShopsAllTenant(onlineShopQueryInnerDTO, BaseConstants.DEFAULT_TENANT_ID), new TypeReference<Map<String,
                OnlineShopCO>>() {
        });
    }

    /**
     * 目录版本+ 目录 批量查询网店
     *
     * @param onlineShopCatalogVersionList 网店
     * @return map key:catalogCode-catalogVersionCode
     */
    public Map<String, List<OnlineShopCO>> listOnlineShops(List<OnlineShopCatalogVersionDTO> onlineShopCatalogVersionList, Long tenantId) {
        return ResponseUtils.getResponse(onlineShopRemoteService.listOnlineShops(onlineShopCatalogVersionList, tenantId),
                new TypeReference<Map<String, List<OnlineShopCO>>>() {
                });
    }

    /**
     * 保存网店
     *
     * @param onlineShopDTO 网店
     * @param tenantId      租户id
     * @return 网店
     */
    public OnlineShopCO saveOnlineShop(OnlineShopDTO onlineShopDTO, Long tenantId) {
        return O2ResponseUtils.getResponse(onlineShopRemoteService.saveOnlineShop(onlineShopDTO, tenantId), new TypeReference<OnlineShopCO>() {
        });
    }

    /**
     * 批量更新网店状态
     *
     * @param onlineShopDTOList 网店信息
     * @param tenantId          租户id
     * @return 网店
     */
    public List<OnlineShopCO> batchUpdateShopStatus(List<OnlineShopDTO> onlineShopDTOList, Long tenantId) {
        return ResponseUtils.getResponse(onlineShopRemoteService.batchUpdateShopStatus(onlineShopDTOList, tenantId), new TypeReference<List<OnlineShopCO>>() {
        });
    }

    /**
     * 批量查询网店
     *
     * @param onlineShopQueryInnerDTO 网店
     * @param tenantId                租户id
     * @return 网店
     */
    public Page<OnlineShopCO> queryOnlineShops(Long tenantId, OnlineShopQueryInnerDTO onlineShopQueryInnerDTO) {
        return ResponseUtils.getResponse(onlineShopRemoteService.queryOnlineShops(tenantId, onlineShopQueryInnerDTO), new TypeReference<Page<OnlineShopCO>>() {
        });
    }
}
