package org.o2.feignclient.metadata.domain.dto;

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
    List<String> warehouseCodes;
}
