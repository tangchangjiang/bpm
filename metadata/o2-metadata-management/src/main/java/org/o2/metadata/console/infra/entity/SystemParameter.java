package org.o2.metadata.console.infra.entity;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.o2.annotation.annotation.AnnotationValue;
import org.o2.annotation.infra.contants.O2AnnotationCoreConstants;
import org.o2.metadata.console.infra.constant.SystemParameterConstants;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * 系统参数
 *
 * @author hongyun.wang01@hand-china.com 2020-02-13
 */
@ApiModel("系统参数")
@VersionAudit
@ModifyAudit
@Table(name = "o2md_system_parameter")
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemParameter extends AuditDomain {

    public static final String FIELD_PARAM_ID = "paramId";
    public static final String FIELD_PARAM_CODE = "paramCode";
    public static final String FIELD_PARAM_NAME = "paramName";
    public static final String FIELD_PARAM_TYPE = "paramTypeCode";
    public static final String FIELD_ACTIVE_FLAG = "activeFlag";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_DEFAULT_VALUE = "defaultValue";
    public static final String FIELD_TENANT_ID = "tenantId";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("表ID，主键")
    @Id
    @GeneratedValue
    private Long paramId;
    @ApiModelProperty(value = "编码", required = true)
    @NotBlank
    private String paramCode;
    @ApiModelProperty(value = "参数名称")
    private String paramName;
    @ApiModelProperty(value = "值集，KV（key-value） LIST(重复) SET（不重复）", required = true)
    @LovValue(SystemParameterConstants.ParamType.LOV_CODE)
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
    @AnnotationValue(type = O2AnnotationCoreConstants.Type.TENANT,name = "tenantName")
    private Long tenantId;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("租户名称")
    @Transient
    private String tenantName;

    @Transient
    @ApiModelProperty("参数类型")
    private String paramTypeMeaning;
    @Transient
    private Set<SystemParamValue> setSystemParamValue;


}
