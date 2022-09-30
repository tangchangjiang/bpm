package org.o2.metadata.console.api.dto;

import lombok.Data;

import java.util.List;

/**
 *
 * 目录或目录版本
 *
 * @author yipeng.zhu@hand-china.com 2021-07-30
 **/
@Data
public class CatalogQueryInnerDTO {
    /**
     * 目录名称
     */
    private List<String> catalogNames;
}
