package org.o2.metadata.console.app.bo;

import lombok.Data;

/**
 *
 * 仓库派送量限制
 *
 * @author yipeng.zhu@hand-china.com 2021-09-24
 **/
@Data
public class WarehouseLimitBO {
    private  Integer pickUpQuantity;
    private  Integer expressedQuantity;
    private  Boolean limitFlag;

}
