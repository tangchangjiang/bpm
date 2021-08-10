package org.o2.metadata.console.infra.redis;

/**
 *
 * 网店
 *
 * @author yipeng.zhu@hand-china.com 2021-08-06
 **/
public interface OnlineShopRedis {
     /**
      * 更新网店
      * @param onlineShopCode 网店编码
      * @param tenantId 租户ID
      */
     void batchUpdateRedis(String onlineShopCode,Long tenantId);
}
