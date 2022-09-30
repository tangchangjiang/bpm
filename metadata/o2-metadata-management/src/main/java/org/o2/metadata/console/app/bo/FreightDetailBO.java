package org.o2.metadata.console.app.bo;

import lombok.Data;

import java.math.BigDecimal;


/**
 * 运费模板明细业务对象
 *
 * @author peng.xu@hand-china.com 2019-06-18
 */
@Data
public class FreightDetailBO  {
    /**
     * 运费模板明细ID
     */
    private Long templateDetailId;

    /**
     * 目的地
     */
    private String regionCode;

    /**
     * 是否默认
     */
    private Integer defaultFlag;

    /**
     * 关联运费模板编码
     */
    private String templateCode;

    /**
     * tenantId
     */
    private Long tenantId;

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
