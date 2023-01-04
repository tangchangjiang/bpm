package org.o2.metadata.management.client.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @auther: changjiang.tang
 * @date: 2023/01/03/16:14
 * @since: V1.7.0
 */
@Data
public class InsideAddressMappingDTO {

    @NotNull
    private List<String> reginCodes;

    @NotNull
    private String platformCode;

}
