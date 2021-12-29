package org.o2.metadata.client;

import org.hzero.core.util.ResponseUtils;
import org.o2.metadata.client.domain.co.OnlineShopCO;
import org.o2.metadata.client.infra.feign.OnlineShopRemoteService;

/**
 *
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
     * @date 2021-08-10
     * @param onlineShopCode  网店编码
     * @return 网店
     */
    public OnlineShopCO getOnlineShop(String onlineShopCode,String tenantId) {
        return ResponseUtils.getResponse(onlineShopRemoteService.getOnlineShop(onlineShopCode,tenantId), OnlineShopCO.class);
    }
}
