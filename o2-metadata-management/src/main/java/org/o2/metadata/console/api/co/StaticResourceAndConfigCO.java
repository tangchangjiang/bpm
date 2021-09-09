package org.o2.metadata.console.api.co;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * description
 *
 * @author zhilin.ren@hand-china.com 2021/09/08 16:50
 */
@Data
public class StaticResourceAndConfigCO {


    @ApiModelProperty(value = "静态资源访问jsonKey", required = true)
    private String jsonKey;

    @ApiModelProperty(value = "静态资源相对路径", required = true)
    private String resourceUrl;

}
