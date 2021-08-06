package org.o2.metadata.console.app.bo;

import lombok.Data;

/**
 *
 * 网店redis
 *
 * @author yipeng.zhu@hand-china.com 2021-08-06
 **/
@Data
public class OnlineShopRedisBO {
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

    /**
     * 目录
     */
    private String catalogCode;

    /**
     * 目标编码
     */
    private String catalogVersionCode;

    /**
     * 仓库编码
     */
    private String warehouseCode;
}
