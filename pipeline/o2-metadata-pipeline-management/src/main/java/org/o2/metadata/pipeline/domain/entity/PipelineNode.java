package org.o2.metadata.pipeline.domain.entity;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.pipeline.domain.repository.PipelineNodeRepository;
import org.springframework.util.Assert;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * @author huizhen.liu@hand-china.com 2019-01-09
 */
@ApiModel("流程器节点")
@VersionAudit
@ModifyAudit
@Table(name = "o2md_pipeline_node")
@Data
@EqualsAndHashCode(callSuper = true)
public class PipelineNode extends AuditDomain {
    public static final String FIELD_ID = "id";
    public static final String FIELD_PIPELINE_ID = "pipelineId";
    public static final String FIELD_CUR_ACTION = "curAction";
    public static final String FIELD_STRATEGY_TYPE = "strategyType";
    public static final String FIELD_NEXT_ACTION = "nextAction";
    public static final String FIELD_CUR_DESCRIPTION = "curDescription";
    public static final String FIELD_TENANT_ID = "tenantId";
    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public boolean exist(PipelineNodeRepository pipelineNodeRepository) {
        Sqls sql = Sqls.custom();
        if (this.id != null) {
            sql.andNotEqualTo(PipelineNode.FIELD_ID, this.id);
        }
        if (this.pipelineId != null) {
            sql.andEqualTo(PipelineNode.FIELD_PIPELINE_ID, this.pipelineId);
        }
        if (this.curAction != null) {
            sql.andEqualTo(PipelineNode.FIELD_CUR_ACTION, this.curAction);
        }
        if (StringUtils.isNotBlank(this.strategyType)) {
            sql.andEqualTo(PipelineNode.FIELD_STRATEGY_TYPE, this.strategyType);
        }
        return pipelineNodeRepository.selectCountByCondition(Condition.builder(PipelineNode.class).andWhere(sql).build()) > 0;
    }

    public void validate() {
        Assert.notNull(this.curAction, "当前行为不能为空");
        Assert.notNull(this.strategyType, "决策类型不能为空");
        Assert.notNull(this.pipelineId, "关联流水线ID不能为空");
    }

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @GeneratedValue
    private Long id;
    @ApiModelProperty(value = "流水线ID")
    private Long pipelineId;
    @ApiModelProperty(value = "当前行为")
    @NotNull
    private Long curAction;
    @ApiModelProperty(value = "下个节点行为")
    @NotNull
    private Long nextAction;
    @ApiModelProperty(value = "决策类型，值集 O2MD.PIPELINE_STRATEGY")
    @LovValue(lovCode = "O2MD.PIPELINE_STRATEGY")
    private String strategyType;
    @ApiModelProperty("租户ID")
    @NotNull
    private Long tenantId;
    @Transient
    private String strategyTypeMeaning;
    @Transient
    @ApiModelProperty(value = "当前流水线节点描述")
    private String curDescription;
    @Transient
    @ApiModelProperty(value = "下个流水线节点描述")
    private String nextDescription;
    @Transient
    @ApiModelProperty(value = "当前流水线节点Bean")
    private String curBeanId;
    @Transient
    @ApiModelProperty(value = "下个流水线节点描述")
    private String nextBeanId;
    @Transient
    @ApiModelProperty(value = "当前流水线节点脚本")
    private String curScript;
    @Transient
    @ApiModelProperty(value = "下个流水线节点脚本")
    private String nextScript;


}
