package org.o2.metadata.console.api.dto;

import lombok.Data;

/**
 *
 * 目录&目录版本
 *
 * @author yipeng.zhu@hand-china.com 2021-08-24
 **/
@Data
public class CatalogRelVersionQueryDTO {
    private  Long tenantId;

    private  Long catalogId;

    private Long catalogVersionId;

    private String catalogCode;

    private String catalogVersionCode;

    private Integer containMasterFlag;

    private String catalogVersionName;
}
