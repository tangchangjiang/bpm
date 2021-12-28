package org.o2.metadata.management.client.domain.dto;

import lombok.Data;

/**
 *
 * 分页查询
 *
 * @author yipeng.zhu@hand-china.com 2021-09-13
 **/
@Data
public class WarehousePageQueryInnerDTO {
    /**
     * 仓库ID 支持批量。批量用英文逗号分隔
     */
    private String warehouseId;

    /**
     * 值为0禁用，1启用
     */
    private  Integer activeFlag;
    /**
     *  仓库编码 支持批量。批量用英文逗号分隔
     */
    private String warehouseCode;

    private Integer page;
    private Integer pageSize;
}
