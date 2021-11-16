package org.o2.metadata.console.api.dto;

import lombok.Data;

import java.util.List;

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

    private List<Long> warehouseIdList;

    /**
     * 值为0禁用，1启用
     */
    private  Integer activeFlag;
    /**
     *  仓库编码 支持批量。批量用英文逗号分隔
     */
    private String warehouseCode;

    private List<String> warehouseCodeList;

    private String warehouseName;

    private Long tenantId;
}
