package org.o2.metadata.console.api.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @auther: changjiang.tang
 * @date: 2023/01/03/15:58
 * @since: V1.7.0
 */
@Data
public class InsideAddressMappingDTO {

    @NotNull
    private List<String> reginCodes;
    @NotNull
    private String platformCode;

    private Long tenantId;
}
