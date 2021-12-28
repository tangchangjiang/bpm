package org.o2.metadata.management.client.domain.co;

import lombok.Data;

/**
 * 仓库关联地址CO
 *
 * @author miao.chen01@hand-china.com 2021-11-13
 */
@Data
public class WarehouseRelAddressCO {
    private String warehouseCode;
    private String warehouseType;
    private String score;
    private String regionCode;
    private String cityCode;
    private String districtCode;
}
