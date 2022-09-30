package org.o2.metadata.api.dto;

import lombok.Data;


/**
 *
 * 运费
 *
 * @author yipeng.zhu@hand-china.com 2021-07-14
 **/
@Data
public class FreightDTO {
    /**
     * 地址编码
     */
    private String  regionCode;
    /**
     * 模版编码
     */
    private String templateCode;
    /**
     * 租户ID
     */
    private Long tenantId;

}
