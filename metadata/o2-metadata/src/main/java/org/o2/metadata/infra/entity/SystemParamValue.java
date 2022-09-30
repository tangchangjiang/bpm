package org.o2.metadata.infra.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import javax.validation.constraints.NotNull;

/**
 * 系统参数值
 *
 * @author hongyun.wang01@hand-china.com 2020-02-13
 */
@ApiModel("系统参数值")

@Data
public class SystemParamValue {

    public static final String FIELD_VALUE_ID = "valueId";
    public static final String FIELD_PARAM_ID = "paramId";
    public static final String FIELD_PARAM_VALUE = "paramValue";
    public static final String FIELD_PARAM1 = "param1";
    public static final String FIELD_PARAM2 = "param2";
    public static final String FIELD_PARAM3 = "param3";
    public static final String FIELD_TENANT_ID = "tenantId";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("表ID，主键")
    private Long valueId;
    @ApiModelProperty(value = "关联参数表，o2ext_system_param.param_id", required = true)
    @NotNull
    private Long paramId;
    @ApiModelProperty(value = "值")
    private String paramValue;
    @ApiModelProperty(value = "拓展字段1")
    private String param1;
    @ApiModelProperty(value = "拓展字段2")
    private String param2;
    @ApiModelProperty(value = "拓展字段3")
    private String param3;
    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "键")
    private String paramKey;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

}
