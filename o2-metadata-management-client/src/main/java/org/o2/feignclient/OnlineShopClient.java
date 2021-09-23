package org.o2.feignclient;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.feignclient.metadata.domain.co.OnlineShopCO;
import org.o2.feignclient.metadata.domain.co.OnlineShopRelWarehouseCO;
import org.o2.feignclient.metadata.domain.dto.OnlineShopCatalogVersionDTO;
import org.o2.feignclient.metadata.domain.dto.OnlineShopQueryInnerDTO;
import org.o2.feignclient.metadata.infra.feign.OnlineShopRemoteService;

import java.util.List;
import java.util.Map;

/**
 *
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
     * 从redis查询网店关联有效的仓库
     *
     * @param onlineShopCode 网店编码
     * @param tenantId        租户ID
     * @return map<warehouseCode, OnlineShopRelWarehouseCO>
     */
    public Map<String, OnlineShopRelWarehouseCO> listOnlineShopRelWarehouses(String onlineShopCode, Long tenantId) {
        return ResponseUtils.getResponse(onlineShopRemoteService.listOnlineShopRelWarehouses(onlineShopCode, tenantId), new TypeReference<Map<String, OnlineShopRelWarehouseCO>>() {
        });
    }

    /**
     * 批量查询网店
     *
     * @param onlineShopQueryInnerDTO 网店
     * @return map 通过名称查询 key:onlineShopName ; 通过code查询 key:onlineShopCode
     */
    public Map<String, OnlineShopCO> listOnlineShops(OnlineShopQueryInnerDTO onlineShopQueryInnerDTO, Long tenantId) {
        return ResponseUtils.getResponse(onlineShopRemoteService.listOnlineShops(onlineShopQueryInnerDTO, tenantId), new TypeReference<Map<String, OnlineShopCO>>() {
        });
    }

    /**
     * 目录版本+ 目录 批量查询网店
     *
     * @param onlineShopCatalogVersionList 网店
     * @return map key:catalogCode-catalogVersionCode
     */
    public Map<String, List<OnlineShopCO>> listOnlineShops(List<OnlineShopCatalogVersionDTO> onlineShopCatalogVersionList, Long tenantId) {
        return ResponseUtils.getResponse(onlineShopRemoteService.listOnlineShops(onlineShopCatalogVersionList, tenantId), new TypeReference<Map<String,  List<OnlineShopCO>>>() {
        });
    }
}
