package org.o2.metadata.console.app.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 运费价格业务对象
 *
 * @author peng.xu@hand-china.com 2019-06-18
 */
@Data
public class FreightPriceBO {
    /**
     * 首件/千克设置
     */
    private BigDecimal firstPieceWeight;

    /**
     * 首件/千克价格
     */
    private BigDecimal firstPrice;

    /**
     * 续件/千克设置
     */
    private BigDecimal nextPieceWeight;

    /**
     * 续件/千克价格
     */
    private BigDecimal nextPrice;
}
