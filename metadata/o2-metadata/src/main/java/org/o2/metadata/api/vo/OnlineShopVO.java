package org.o2.metadata.api.vo;

import lombok.Data;

/**
 * 网店vo
 *
 * @author chao.yang05@hand-china.com 2023-04-18
 */
@Data
public class OnlineShopVO {

    /**
     * 网店编码
     */
    private String onlineShopCode;

    /**
     * 网店名称
     */
    private String onlineShopName;

    /**
     * logo
     */
    private String logoUrl;

    /**
     * 店铺图片
     */
    private String shopMediaUrl;

    /**
     * 是否自营：1-自营，0-非自营
     */
    private Integer selfSalesFlag;

    /**
     * 租户Id
     */
    private String tenantId;

    /**
     * 关注数量
     */
    private Long followNum;

    /**
     * 关注标识
     */
    private Integer followFlag;
}
