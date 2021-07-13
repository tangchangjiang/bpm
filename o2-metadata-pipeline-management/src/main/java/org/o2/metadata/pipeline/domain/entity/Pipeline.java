package org.o2.metadata.pipeline.domain.entity;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.pipeline.domain.repository.PipelineRepository;
import org.springframework.util.Assert;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author huizhen.liu@hand-china.com 2019-01-09
 */
@ApiModel("流程器")
@VersionAudit
@ModifyAudit
@Table(name = "o2md_pipeline")
@Data
@EqualsAndHashCode(callSuper = true)
public class Pipeline extends AuditDomain {
    public static final String FIELD_ID = "id";
    public static final String FIELD_CODE = "code";
    public static final String FIELD_START_ACTION = "startAction";
    public static final String FIELD_END_ACTION = "endAction";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_ACTIVE_FLAG = "activeFlag";
    public static final String FIELD_TENANT_ID = "tenantId";

    //
    // 业务方法 (按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public boolean exist(PipelineRepository pipelineRepository) {
        Sqls sql = Sqls.custom();
        if (this.id != null) {
            sql.andNotEqualTo(Pipeline.FIELD_ID, this.id);
        }
        if (StringUtils.isNotBlank(this.code)) {
            sql.andEqualTo(Pipeline.FIELD_CODE, this.code);
        }
        if (this.activeFlag != null) {
            sql.andEqualTo(Pipeline.FIELD_ACTIVE_FLAG, this.activeFlag);
        }
        return pipelineRepository.selectCountByCondition(Condition.builder(Pipeline.class).andWhere(sql).build()) > 0;
    }

    public void validate() {
        Assert.notNull(this.code, "流水线编码不能为空");
        Assert.notNull(this.startAction, "开始行为不能为空");
        Assert.notNull(this.activeFlag, "是否有效不能为空");
    }

    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @GeneratedValue
    private Long id;
    @ApiModelProperty(value = "流水线编码")
    @NotNull
    private String code;
    @ApiModelProperty(value = "开始行为")
    @NotNull
    private Long startAction;
    @ApiModelProperty(value = "结束行为")
    private Long endAction;
    @ApiModelProperty(value = "流水线描述")
    private String description;
    @ApiModelProperty(value = "是否有效")
    private Integer activeFlag;
    @ApiModelProperty("租户ID")
    @NotNull
    private Long tenantId;

    @Transient
    @ApiModelProperty(value = "当前流水线节点描述")
    private String startDescription;
    @Transient
    @ApiModelProperty(value = "下个流水线节点描述")
    private String endDescription;
    @Transient
    private List<PipelineNode> pipelineNodes;
    @Transient
    @ApiModelProperty(value = "开始流水线节点Bean")
    private String startBeanId;
    @Transient
    @ApiModelProperty(value = "结束流水线节点描述")
    private String endBeanId;
    @Transient
    @ApiModelProperty(value = "开始流水线节点脚本")
    private String startScript;
    @Transient
    @ApiModelProperty(value = "结束流水线节点脚本")
    private String endScript;
}
