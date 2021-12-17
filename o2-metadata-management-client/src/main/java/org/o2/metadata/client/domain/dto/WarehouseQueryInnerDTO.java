package org.o2.metadata.client.domain.dto;

import lombok.Data;

import java.util.List;

/**
 *
 * 仓库
 *
 * @author yipeng.zhu@hand-china.com 2021-08-13
 **/
@Data
public class WarehouseQueryInnerDTO {
    /**
     * 仓库编码
     */
    private List<String> warehouseCodes;

    /**
     * 是否查询数据 默认查sql
     */
    private Boolean dbFlag;

    /**
     * 仓库名称
     */
    private List<String> warehouseNames;

    private String onlineShopCode;
    /**
     * 是否有效
     */
    private Integer activeFlag;


    private String warehouseTypeCode;

    private String warehouseName;
}
