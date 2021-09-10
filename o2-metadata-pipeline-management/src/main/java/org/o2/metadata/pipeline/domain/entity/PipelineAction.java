package org.o2.metadata.pipeline.domain.entity;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.o2.metadata.pipeline.infra.constants.PipelineConstants;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程器行为
 *
 * @author wenjun.deng01@hand-china.com 2019-12-16 10:36:04
 */
@ApiModel("流程器行为")
@VersionAudit
@ModifyAudit
@Table(name = "o2md_pipeline_action")
@Data
@EqualsAndHashCode(callSuper = true)
public class PipelineAction extends AuditDomain {

    public static final String FIELD_ID = "id";
    public static final String FIELD_CODE = "code";
    public static final String FIELD_SERVICE_CODE = "serviceCode";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_ACTION_TYPE = "actionType";
    public static final String FIELD_BEAN_ID = "beanId";
    public static final String FIELD_SCRIPT = "script";
    public static final String FIELD_TENANT_ID = "tenantId";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public Map<String, String> hashValue() {
        final Map<String, String> hash = new HashMap<>(5);
        hash.put(FIELD_BEAN_ID, this.getBeanId());
        hash.put(FIELD_SCRIPT, this.getScript());
        return hash;
    }

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("表ID，主键")
    @Id
    @GeneratedValue
    private Long id;
    @ApiModelProperty(value = "行为编码")
    @NotNull
    private String code;
    @ApiModelProperty(value = "服务编码")
    @NotNull
    private String serviceCode;
    @ApiModelProperty(value = "标签")
    private String label;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "行为类型，值集 O2MD.ACTION_TYPE（Bean/Script）")
    @LovValue(lovCode = "O2MD.ACTION_TYPE")
    @NotNull
    private String actionType;
    @ApiModelProperty(value = "当前行为(spring_bean_id)")
    private String beanId;
    @ApiModelProperty(value = "脚本")
    private String script;
    @ApiModelProperty(value = "展示类型，值集O2MD.DISPLAY_TYPE")
    @LovValue(lovCode = PipelineConstants.DisplayType.LOV_CODE)
    private String displayTypeCode;
    @ApiModelProperty(value = "是否启用")
    private Integer statusFlag;
    @ApiModelProperty(value = "租户ID")
    @NotNull
    private Long tenantId;


    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @Transient
    private String actionTypeMeaning;
    @Transient
    private String displayTypeMeaning;
    @Transient
    private List<ActionParameter> parameters;

}
