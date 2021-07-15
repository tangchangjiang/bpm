package org.o2.metadata.app.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * 商品关联运费
 *
 * @author yipeng.zhu@hand-china.com 2021-07-14
 **/
@Data
public class ProductRelFreightBO {
    private String templateCode;
    /**
     * 重量
     */
    private BigDecimal weight;
    private String weightUomCode;
    /**
     * 体积
     */
    private BigDecimal volume;
    private String volumeUomCode;
}
