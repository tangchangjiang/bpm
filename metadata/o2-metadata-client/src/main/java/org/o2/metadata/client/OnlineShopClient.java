package org.o2.metadata.client;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.metadata.client.domain.co.OnlineShopCO;
import org.o2.metadata.client.infra.feign.OnlineShopRemoteService;

import java.util.List;
import java.util.Map;

/**
 * 网店
 *
 * @author yipeng.zhu@hand-china.com 2021-11-15
 **/
public class OnlineShopClient {
    private final OnlineShopRemoteService onlineShopRemoteService;

    public OnlineShopClient(OnlineShopRemoteService onlineShopRemoteService) {
        this.onlineShopRemoteService = onlineShopRemoteService;
    }

    /**
     * 查询网店
     *
     * @param onlineShopCode 网店编码
     * @return 网店
     */
    public OnlineShopCO getOnlineShop(String onlineShopCode) {
        return ResponseUtils.getResponse(onlineShopRemoteService.getOnlineShopByCode(onlineShopCode), OnlineShopCO.class);
    }

    /**
     * 批量查询网店
     *
     * @param onlineShopCodes 网店code
     * @return List<OnlineShopCO>
     */
    public List<OnlineShopCO> queryOnlineShop(List<String> onlineShopCodes) {
        return ResponseUtils.getResponse(onlineShopRemoteService.queryOnlineShop(onlineShopCodes), new TypeReference<List<OnlineShopCO>>() {
        });
    }

    /**
     * 批量查询网店-根据网店类型
     *
     * @param onlineShopType 网店类型
     * @return List<OnlineShopCO>
     */
    public List<OnlineShopCO> queryOnlineShopByType(String tenantId, String onlineShopType) {
        return ResponseUtils.getResponse(onlineShopRemoteService.queryOnlineShopByType(tenantId, onlineShopType), new TypeReference<List<OnlineShopCO>>() {
        });
    }

    /**
     * 查询网店
     *
     * @param onlineShopCode 网店编码
     * @return 网店
     */
    @Deprecated
    public OnlineShopCO getOnlineShop(String onlineShopCode, String tenantId) {
        return ResponseUtils.getResponse(onlineShopRemoteService.getOnlineShop(onlineShopCode, tenantId), OnlineShopCO.class);
    }

    /**
     * 多租户查询网店
     *
     * @param onlineShopTenantMap 租户Id关联网店 map<tenantId, List<onlineShopCodes>>
     * @return Map<tenantId, List<OnlineShopCO>>
     */
    @Deprecated
    public Map<Long, List<OnlineShopCO>> queryOnlineShops(Map<Long, List<String>> onlineShopTenantMap) {
        return ResponseUtils.getResponse(onlineShopRemoteService.queryOnlineShopBatchTenant(onlineShopTenantMap), new TypeReference<Map<Long, List<OnlineShopCO>>>() {
        });
    }

    /**
     * 批量查询网店-传租户ID
     * @param tenantId
     * @param onlineShopCodes
     * @return
     */
    @Deprecated
    public List<OnlineShopCO> batchQueryOnlineShop(String tenantId, List<String> onlineShopCodes) {
        return ResponseUtils.getResponse(onlineShopRemoteService.batchQueryOnlineShop(tenantId, onlineShopCodes), new TypeReference<List<OnlineShopCO>>() {
        });
    }

}
