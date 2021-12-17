package org.o2.metadata.client.domain.co;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * description
 *
 * @author zhilin.ren@hand-china.com 2021/09/07 15:10
 */
@Data
@ApiModel
public class StaticResourceConfigCO {

    @ApiModelProperty(value = "静态资源编码", required = true)
    private String resourceCode;
    @ApiModelProperty(value = "静态资源层级", required = true)
    private String resourceLevel;
    @ApiModelProperty(value = "静态资源访问jsonKey", required = true)
    private String jsonKey;
    @ApiModelProperty(value = "租户ID", required = true)
    private Long tenantId;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "是否区分多语言", required = true)
    private Integer differentLangFlag;
    @ApiModelProperty(value = "上传目录")
    private String uploadFolder;
    @ApiModelProperty(value = "来源模块,值集:O2MD.DOMAIN_MODULE")
    private String sourceModuleCode;
    @ApiModelProperty(value = "来源程序")
    private String sourceProgram;

}
