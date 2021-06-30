package org.o2.metadata.console.api.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * PlatformUomVO
 *
 * @author peng.xu@hand-china.com 2019-07-09
 */
@ApiModel("平台值集")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PlatformUomVO {

    @ApiModelProperty("子值集编码")
    private String value;

    @ApiModelProperty("子值集描述")
    private String meaning;

    @ApiModelProperty("父值集编码")
    private String parentValue;

}
