package org.o2.metadata.console.api.dto;

import lombok.Data;

import java.util.List;

/**
 * 承运商运费模板查询
 *
 * @author zhilin.ren@hand-china.com 2022/10/12 13:55
 */
@Data
public class CarrierFreightDTO {
    private Long tenantId;
    private List<String> carrierCodeList;
}
