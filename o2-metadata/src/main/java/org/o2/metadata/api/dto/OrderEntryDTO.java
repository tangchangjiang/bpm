package org.o2.metadata.api.dto;

import lombok.Data;

import java.math.BigDecimal;


/**
 *
 * 订单行
 *
 * @author yipeng.zhu@hand-china.com 2021-07-14
 **/
@Data
public class OrderEntryDTO {
    /**
     * 订单行号
     */
    private String entryCode;
    /**
     * 平台sku编码
     */
    private String platformSkuCode;
    /**
     * 数量
     */
    private Integer quantity;
    /**
     * 单位
     */
    private String unit;

    /**
     * 运费模版
     */
    private String freightTemplateCode;

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
