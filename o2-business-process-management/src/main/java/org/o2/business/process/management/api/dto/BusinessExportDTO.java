package org.o2.business.process.management.api.dto;

import lombok.Data;

import java.util.List;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/8/23 17:01
 */
@Data
public class BusinessExportDTO {

    private List<String> processCodes;

    private Long tenantId;

}
