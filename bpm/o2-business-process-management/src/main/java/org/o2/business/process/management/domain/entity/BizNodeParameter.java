package org.o2.business.process.management.domain.entity;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.mybatis.annotation.Unique;
import org.o2.business.process.management.infra.constant.BusinessProcessConstants;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Comparator;

/**
 * 业务节点参数表
 *
 * @author zhilin.ren@hand-china.com 2022-08-10 14:31:01
 */
@ApiModel("业务节点参数表")
@VersionAudit
@ModifyAudit
@Table(name = "o2bpm_biz_node_parameter")
@Data
@EqualsAndHashCode(callSuper = true)
public class BizNodeParameter extends AuditDomain {

    public static final String FIELD_BIZ_NODE_PARAMETER_ID = "bizNodeParameterId";
    public static final String FIELD_PARAM_CODE = "paramCode";
    public static final String FIELD_PARAM_NAME = "paramName";
    public static final String FIELD_BEAN_ID = "beanId";
    public static final String FIELD_PARAM_FORMAT_CODE = "paramFormatCode";
    public static final String FIELD_PARAM_EDIT_TYPE_CODE = "paramEditTypeCode";
    public static final String FIELD_NOTNULL_FLAG = "notnullFlag";
    public static final String FIELD_BUSINESS_MODEL = "businessModel";
    public static final String FIELD_VALUE_FILED_FROM = "valueFiledFrom";
    public static final String FIELD_VALUE_FILED_TO = "valueFiledTo";
    public static final String FIELD_SHOW_FLAG = "showFlag";
    public static final String FIELD_ENABLED_FLAG = "enabledFlag";
    public static final String FIELD_DEFAULT_VALUE = "defaultValue";
    public static final String FIELD_DEFAULT_MEANING = "defaultMeaning";
    public static final String FIELD_PARENT_FIELD = "parentField";
    public static final String FIELD_DEFAULT_VALUE_TYPE = "defaultValueType";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String O2BPM_BIZ_NODE_PARAMETER_U1 = "o2bpm_biz_node_parameter_u1";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public static Comparator<BizNodeParameter> defaultComparator(){
        return Comparator.comparing(BizNodeParameter::getCreationDate);
    }

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("表ID,主键")
    @Id
    @GeneratedValue
    private Long bizNodeParameterId;
    @ApiModelProperty(value = "参数编码", required = true)
    @NotBlank
    @Unique(O2BPM_BIZ_NODE_PARAMETER_U1)
    private String paramCode;
    @ApiModelProperty(value = "参数名称", required = true)
    @NotBlank
    private String paramName;
    @ApiModelProperty(value = "BeanId", required = true)
    @NotBlank
    @Unique(O2BPM_BIZ_NODE_PARAMETER_U1)
    private String beanId;
    @ApiModelProperty(value = "参数格式，HSDR.PARAM_FORMAT", required = true)
    @LovValue(value = BusinessProcessConstants.LovCode.PARAM_FORMAT)
    @NotBlank
    private String paramFormatCode;
    @ApiModelProperty(value = "编辑类型，HSDR.PARAM_EDIT_TYPE", required = true)
    @LovValue(value = BusinessProcessConstants.LovCode.PARAM_EDIT_TYPE)
    @NotBlank
    private String paramEditTypeCode;
    @ApiModelProperty(value = "是否必须")
    private Integer notnullFlag;
    @ApiModelProperty(value = "业务模型")
    private String businessModel;
    @ApiModelProperty(value = "字段值从")
    private String valueFiledFrom;
    @ApiModelProperty(value = "字段值至")
    private String valueFiledTo;
    @ApiModelProperty(value = "是否展示", required = true)
    @NotNull
    private Integer showFlag;
    @ApiModelProperty(value = "启用标识", required = true)
    @NotNull
    private Integer enabledFlag;
    @ApiModelProperty(value = "默认值")
    private String defaultValue;
    @ApiModelProperty(value = "默认展示值")
    private String defaultMeaning;
    @ApiModelProperty(value = "级联父级字段")
    private String parentField;
    @ApiModelProperty(value = "默认值类型")
    private String defaultValueType;
    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    @Unique(O2BPM_BIZ_NODE_PARAMETER_U1)
    private Long tenantId;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------
    //

    @ApiModelProperty(value = "参数格式含义")
    @Transient
    private String paramFormatMeaning;
    @ApiModelProperty(value = "编辑类型含义")
    @Transient
    private String paramEditTypeMeaning;

}

