package org.o2.metadata.infra.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * 系统参数
 *
 * @author hongyun.wang01@hand-china.com 2020-02-13
 */
@ApiModel("系统参数")
@Data
public class SystemParameter  {
    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("表ID，主键")

    private Long paramId;
    @ApiModelProperty(value = "编码", required = true)
    @NotBlank
    private String paramCode;
    @ApiModelProperty(value = "参数名称")
    private String paramName;
    @ApiModelProperty(value = "值集，KV（key-value） LIST(重复) SET（不重复）", required = true)
    private String paramTypeCode;
    @ApiModelProperty(value = "是否启用", required = true)
    @NotNull
    private Integer activeFlag;
    @ApiModelProperty(value = "备注说明")
    private String remark;
    @ApiModelProperty(value = "默认值")
    private String defaultValue;
    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;

    @ApiModelProperty("参数类型")
    private String paramTypeMeaning;
    private Set<SystemParamValue> setSystemParamValue;


}
