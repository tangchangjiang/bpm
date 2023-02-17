package org.o2.metadata.management.client.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 值集内部查询DTO
 */
@Data
public class LovQueryInnerDTO {
    @ApiModelProperty(value = "值集编码")
    @NotBlank
    private String lovCode;
    @ApiModelProperty(value = "语言")
    private List<String> languageList;
}
