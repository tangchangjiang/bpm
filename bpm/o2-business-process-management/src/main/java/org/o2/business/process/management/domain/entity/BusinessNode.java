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
import java.util.List;

/**
 * 业务流程节点表
 *
 * @author zhilin.ren@hand-china.com 2022-08-10 14:31:01
 */
@ApiModel("业务流程节点表")
@VersionAudit
@ModifyAudit
@Table(name = "o2bpm_business_node")
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessNode extends AuditDomain {

    public static final String FIELD_BIZ_NODE_ID = "bizNodeId";
    public static final String FIELD_BEAN_ID = "beanId";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_NODE_NAME = "nodeName";
    public static final String FIELD_NODE_TYPE = "nodeType";
    public static final String FIELD_SCRIPT = "script";
    public static final String FIELD_ENABLED_FLAG = "enabledFlag";
    public static final String FIELD_BUSINESS_TYPE = "businessTypeCode";
    public static final String FIELD_SUB_BUSINESS_TYPE = "subBusinessTypeCode";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String O2BPM_BUSINESS_NODE_U1 = "o2bpm_business_node_u1";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("表ID,主键")
    @Id
    @GeneratedValue
    private Long bizNodeId;
    @ApiModelProperty(value = "业务流程节点bean", required = true)
    @NotBlank
    @Unique(O2BPM_BUSINESS_NODE_U1)
    private String beanId;
    @ApiModelProperty(value = "业务节点名称")
    private String nodeName;
    @ApiModelProperty(value = "业务节点描述")
    private String description;
    @ApiModelProperty(value = "节点类型", required = true)
    @NotBlank
    private String nodeType;
    @ApiModelProperty(value = "脚本")
    private String script;
    @ApiModelProperty(value = "1-启用/0-禁用", required = true)
    @NotNull
    private Integer enabledFlag;
    @ApiModelProperty(value = "业务类型(O2MD.BUSINESS_TYPE)", required = true)
    @LovValue(value = BusinessProcessConstants.LovCode.BUSINESS_TYPE_CODE)
    @NotBlank
    private String businessTypeCode;
    @ApiModelProperty(value = "二级业务类型(O2MD.SUB_BUSINESS_TYPE)")
    @LovValue(value = BusinessProcessConstants.LovCode.SUB_BUSINESS_TYPE_CODE)
    private String subBusinessTypeCode;
    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    @Unique(O2BPM_BUSINESS_NODE_U1)
    private Long tenantId;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty(value = "业务节点参数列表")
    @Transient
    private List<BizNodeParameter> paramList;

    @Transient
    @ApiModelProperty(value = "业务类型含义")
    private String businessTypeMeaning;
    @Transient
    @ApiModelProperty(value = "二级业务类型含义")
    private String subBusinessTypeMeaning;
}

