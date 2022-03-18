package org.o2.metadata.console.api.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 地区发布 DTO
 * @author hanzhu.chen@hand-china.com
 */
@Data
public class AddressReleaseDTO {

    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 平台编码
     */
    @NotBlank
    private String platformCode;
}
