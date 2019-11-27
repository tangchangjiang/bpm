package org.o2.metadata.domain.entity;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.ext.metadata.domain.repository.PosRelCarrierRepository;
import org.springframework.util.Assert;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * 服务点关联承运商
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("服务点关联承运商")
@VersionAudit
@ModifyAudit
@Table(name = "o2md_pos_rel_carrier")
public class PosRelCarrier extends AuditDomain {

    public static final String FIELD_POS_REL_CARRIER_ID = "posRelCarrierId";
    public static final String FIELD_POS_ID = "posId";
    public static final String FIELD_CARRIER_ID = "carrierId";
    public static final String FIELD_IS_ACTIVE = "isActive";
    public static final String FIELD_IS_DEFAULT = "isDefault";
    public static final String FIELD_PRIORITY = "priority";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public boolean exist(final PosRelCarrierRepository posRelCarrierRepository) {
        final Sqls sqls = Sqls.custom();
        if (this.getPosRelCarrierId() != null) {
            sqls.andNotEqualTo(PosRelCarrier.FIELD_POS_REL_CARRIER_ID, this.getPosRelCarrierId());
        }
        return posRelCarrierRepository.selectCountByCondition(Condition.builder(PosRelCarrier.class)
                .andWhere(sqls.andEqualTo(PosRelCarrier.FIELD_CARRIER_ID, this.getCarrierId())
                        .andEqualTo(PosRelCarrier.FIELD_POS_ID, this.getPosId())).build()) > 0;
    }

    public void baseValidate() {
        Assert.notNull(this.carrierId, "承运商ID不能为空");
        Assert.notNull(this.posId, "服务点ID不能为空");
        Assert.notNull(this.isActive, "状态不能为空");
        Assert.notNull(this.priority, "优先级不能为空");
        Assert.notNull(this.isDefault, "默认值不能为空");
    }
    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @GeneratedValue
    private Long posRelCarrierId;

    @ApiModelProperty(value = "服务点")
    private Long posId;

    @ApiModelProperty(value = "承运商")
    private Long carrierId;

    @ApiModelProperty(value = "是否激活")
    @NotNull
    private Integer isActive;

    @ApiModelProperty(value = "是否默认值")
    @NotNull
    private Integer isDefault;

    @ApiModelProperty(value = "优先级")
    private Integer priority;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty(value = "承运商名称")
    @Transient
    private String carrierName;

    @ApiModelProperty(value = "服务点")
    @Transient
    private Pos pos;
}
