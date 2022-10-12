package org.o2.metadata.pipeline.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.core.helper.JsonHelper;
import org.o2.metadata.pipeline.domain.repository.ActionParameterRepository;
import org.o2.metadata.pipeline.infra.constants.PipelineConstants;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * 行为参数
 *
 * @author wei.cai@hand-china.com 2020-03-18
 */
@ApiModel("行为参数")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "o2md_action_parameter")
@Data
@EqualsAndHashCode(callSuper = true)
public class ActionParameter extends AuditDomain {

    public static final String FIELD_ACTION_PARAMETER_ID = "actionParameterId";
    public static final String FIELD_PARAMETER_NAME = "parameterName";
    public static final String FIELD_ACTION_ID = "actionId";
    public static final String FIELD_DATA_TYPE_CODE = "dataTypeCode";
    public static final String FIELD_TENANT_ID = "tenantId";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public int save(final ActionParameterRepository actionParameterRepository) {
        switch (this.get_status()) {
            case create:
                return actionParameterRepository.insert(this);
            case update:
                SecurityTokenHelper.validToken(this, false);
                return actionParameterRepository.updateOptional(this);
            case delete:
                SecurityTokenHelper.validToken(this, false);
                return actionParameterRepository.delete(this);
            default:
                throw new CommonException("Unsupported status in save.");
        }
    }

    public String convertToJsonString() {
        final Map<String, Object> jsonMap = new HashMap<>(4);
        jsonMap.put(FIELD_PARAMETER_NAME, this.getParameterName());
        jsonMap.put(FIELD_DATA_TYPE_CODE, this.getDataTypeCode());
        jsonMap.put(FIELD_TENANT_ID, this.getTenantId());
        return JsonHelper.mapToString(jsonMap);
    }

    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("表ID，主键")
    @Id
    @GeneratedValue
    private Long actionParameterId;
    @ApiModelProperty(value = "参数编码")
    @NotBlank
    private String parameterCode;
    @ApiModelProperty(value = "参数名称")
    @NotBlank
    private String parameterName;
    @ApiModelProperty(value = "行为，关联表o2ext.action.action_id")
    @NotNull
    private Long actionId;
    @ApiModelProperty(value = "数据类型，值集：O2MD.RULE_DATA_TYPE")
    @LovValue(value = PipelineConstants.RuleDataType.CODE)
    private String dataTypeCode;
    @ApiModelProperty(value = "租户ID")
    @NotNull
    private Long tenantId;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @Transient
    private String dataTypeMeaning;

}
