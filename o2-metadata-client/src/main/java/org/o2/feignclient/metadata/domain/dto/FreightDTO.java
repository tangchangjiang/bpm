package org.o2.feignclient.metadata.domain.dto;

import lombok.Data;

import java.util.List;

/**
 *
 * 运费
 *
 * @author yipeng.zhu@hand-china.com 2021-07-14
 **/
@Data
public class FreightDTO {
    /**
     * 地址信息
     */
    private AddressDTO address;
    /**
     * 订单行
     */
    private List<OrderEntryDTO> orderEntryList;
    /**
     * 租户ID
     */
    private Long tenantId;
}
