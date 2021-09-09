package org.o2.metadata.api.co;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yong.nie@hand-china.com
 * @date 2020/6/10 14:57
 **/
@Data
public class SystemParamValueCO {

    private String paramValue;

    private String param1;

    private String param2;

    private String param3;

    @ApiModelProperty(value = "键")
    private String paramKey;


}
