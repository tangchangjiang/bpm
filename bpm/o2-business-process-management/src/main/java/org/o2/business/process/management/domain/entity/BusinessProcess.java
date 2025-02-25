package org.o2.business.process.management.domain.entity;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.mybatis.annotation.Unique;
import org.o2.annotation.annotation.AnnotationValue;
import org.o2.annotation.infra.contants.O2AnnotationCoreConstants;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 业务流程定义表
 *
 * @author youlong.peng@hand-china.com 2022-08-10 14:23:57
 */
@ApiModel("业务流程定义表")
@VersionAudit
@ModifyAudit
@Table(name = "o2bpm_business_process")
@Data
public class BusinessProcess extends AuditDomain {

    public static final String FIELD_BIZ_PROCESS_ID = "bizProcessId";
    public static final String FIELD_PROCESS_CODE = "processCode";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_ENABLED_FLAG = "enabledFlag";
    public static final String FIELD_PROCESS_JSON = "processJson";
    public static final String FIELD_VIEW_JSON = "viewJson";
    public static final String FIELD_BUSINESS_TYPE_CODE = "businessTypeCode";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String O2BPM_BUSINESS_PROCESS_U1 = "o2bpm_business_process_u1";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("表ID，主键")
    @Id
    @GeneratedValue
    private Long bizProcessId;
    @ApiModelProperty(value = "业务流程编码", required = true)
    @NotBlank
    @Unique(O2BPM_BUSINESS_PROCESS_U1)
    private String processCode;
    @ApiModelProperty("实体编码")
    private String entityCode;
    @ApiModelProperty(value = "业务流程描述")
    private String description;
    @ApiModelProperty(value = "1-启用/0-禁用", required = true)
    @NotNull
    private Integer enabledFlag;
    @ApiModelProperty(value = "业务流程json")
    private String processJson;
    @ApiModelProperty(value = "画布渲染json")
    private String viewJson;
    @ApiModelProperty(value = "业务类型", required = true)
    @LovValue(lovCode = "O2BPM.BUSINESS_TYPE")
    @NotBlank
    private String businessTypeCode;
    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    @AnnotationValue(type = O2AnnotationCoreConstants.Type.TENANT,name = "tenantName")
    @Unique(O2BPM_BUSINESS_PROCESS_U1)
    private Long tenantId;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("租户名称")
    @Transient
    private String tenantName;

    @Transient
    @ApiModelProperty("业务类型含义")
    private String businessTypeMeaning;

    @Transient
    @ApiModelProperty("创建人")
    private String createdOperator;

    @Transient
    @ApiModelProperty("更新人")
    private String updatedOperator;
}

