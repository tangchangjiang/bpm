package org.o2.metadata.management.client.domain.dto;

import lombok.Data;

import java.util.List;

/**
 *
 * 目录或目录版本
 *
 * @author yipeng.zhu@hand-china.com 2021-07-30
 **/
@Data
public class CatalogVersionQueryInnerDTO {
    /**
     * 目录编码
     */
    private List<String> catalogCodes;

    /**
     * 目录版本编码
     */
    private List<String> catalogCodeVersionCodes;
}
