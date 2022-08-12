package org.o2.business.process.management.api.dto;

import lombok.Data;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/8/11 15:24
 */
@Data
public class BusinessProcessQueryDTO {

    private String processCode;

    private String description;

    private String businessTypeCode;

    private Integer enabledFlag;

    private Long tenantId;
}
