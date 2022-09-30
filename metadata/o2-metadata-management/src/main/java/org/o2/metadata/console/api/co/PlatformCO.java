package org.o2.metadata.console.api.co;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * description 平台匹配结果
 *
 * @author zhilin.ren@hand-china.com 2021/08/02 21:52
 */
@Data
public class PlatformCO {
    @ApiModelProperty(value = "平台名称")
    private String platformName;

    @ApiModelProperty(value = "平台编码", required = true)
    private String platformCode;

    private List<PlatformInfoMappingCO> platformInfoMappings;

}
