package org.o2.metadata.console.app.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * 网店redis
 *
 * @author yipeng.zhu@hand-china.com 2021-08-06
 **/
@Data
public class OnlineShopCacheBO {
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


    private Integer pickedUpFlag;
    private Integer returnedFlag;
    private Integer exchangedFlag;
    private Integer enableSplitFlag;
    private Long tenantId;
    private Integer isDefault;
    private Integer sourcedFlag;
    @ApiModelProperty(value = "是否有效")
    private Integer activeFlag;
}
