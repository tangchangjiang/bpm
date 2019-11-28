package org.o2.metadata.app.bo;

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
     * 快递公司
     */
    private String carrierCode;

    /**
     * 目的地
     */
    private String regionCode;

    /**
     * 是否默认
     */
    private Integer isDefault;

    /**
     * 关联运费模板编码
     */
    private String templateCode;
}
