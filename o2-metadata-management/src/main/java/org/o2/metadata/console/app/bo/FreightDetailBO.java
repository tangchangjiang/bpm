package org.o2.metadata.console.app.bo;

import lombok.Data;


/**
 * 运费模板明细业务对象
 *
 * @author peng.xu@hand-china.com 2019-06-18
 */
@Data
public class FreightDetailBO extends FreightPriceBO {
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
}
