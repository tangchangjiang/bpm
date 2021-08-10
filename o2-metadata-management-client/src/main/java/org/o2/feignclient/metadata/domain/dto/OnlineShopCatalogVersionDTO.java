package org.o2.feignclient.metadata.domain.dto;

import lombok.Data;

/**
 *
 * 网店版本
 *
 * @author yipeng.zhu@hand-china.com 2021-08-10
 **/
@Data
public class OnlineShopCatalogVersionDTO {
    private String catalogCode;

    private String catalogVersionCode;
}
