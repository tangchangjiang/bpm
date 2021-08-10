package org.o2.metadata.infra.entity;

import lombok.Data;

/**
 *
 * 网店redis
 *
 * @author yipeng.zhu@hand-china.com 2021-08-06
 **/
@Data
public class OnlineShop {
    /**
     * 平台编码
     */
    private String platformCode;

    /**
     * 网点名称
     */
    private String onlineShopName;

    /**
     * 网点编码
     */
    private String onlineShopCode;

    /**
     * 平台店铺编码
     */
    private String platformShopCode;

    private Long tenantId;


}
