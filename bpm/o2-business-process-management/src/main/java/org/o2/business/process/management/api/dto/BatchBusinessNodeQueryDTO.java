package org.o2.business.process.management.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author zhilin.ren@hand-china.com 2022/08/12 17:52
 */
@Data
public class BatchBusinessNodeQueryDTO {

    @ApiModelProperty(value = "beanIdList")
    @NotEmpty
    private List<String> beanIdList;

}
