package org.o2.metadata.console.api.dto;

import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.mybatis.domian.SecurityToken;
import org.o2.metadata.console.infra.entity.SystemParamValue;


/**
 *
 * 系统参数值DTO
 *
 * @author yipeng.zhu@hand-china.com 2021-07-23
 **/
@Data
public class SystemParamValueDTO extends AuditDomain {
    @ApiModelProperty("表ID，主键")
    private Long valueId;
    @ApiModelProperty(value = "关联参数表，o2ext_system_param.param_id", required = true)
    private Long paramId;
    @ApiModelProperty(value = "值")
    private String paramValue;
    @ApiModelProperty(value = "拓展字段1")
    private String param1;
    @ApiModelProperty(value = "拓展字段2")
    private String param2;
    @ApiModelProperty(value = "拓展字段3")
    private String param3;
    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "key")
    private String paramKey;
    @ApiModelProperty(value = "描述")
    private String description;
    @Override
    public Class<? extends SecurityToken> associateEntityClass() {
        return SystemParamValue.class;
    }
}
