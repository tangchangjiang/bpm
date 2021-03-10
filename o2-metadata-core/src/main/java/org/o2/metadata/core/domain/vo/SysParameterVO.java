package org.o2.metadata.core.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 系统参数业务对象
 *
 * @author mark.bao@hand-china.com 2019-05-30
 */
@Data
public class SysParameterVO implements Serializable {

    @ApiModelProperty("参数编码")
    private String parameterCode;

    @ApiModelProperty("参数说明")
    private String parameterValue;

    @ApiModelProperty("是否激活")
    private Integer activeFlag;

    @ApiModelProperty("租户")
    private Long tenantId;
}
