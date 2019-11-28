package org.o2.metadata.domain.entity;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.o2.metadata.domain.repository.OnlineShopRelPosRepository;
import org.o2.metadata.domain.repository.OnlineShopRepository;
import org.o2.metadata.domain.repository.PosRepository;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 网店关联服务点
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("网店关联服务点")
@VersionAudit
@ModifyAudit
@Table(name = "o2md_online_shop_rel_pos")
public class OnlineShopRelPos extends AuditDomain {

    public static final String FIELD_ONLINE_SHOP_REL_POS_ID = "onlineShopRelPosId";
    public static final String FIELD_ONLINE_SHOP_ID = "onlineShopId";
    public static final String FIELD_POS_ID = "posId";
    public static final String FIELD_IS_ACTIVE = "activeFlag";
    public static final String FIELD_IS_INV_CALCULATED = "is_inv_calculated";


    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public boolean exist(final OnlineShopRelPosRepository relPosRepository) {
        if (this.onlineShopRelPosId != null) {
            return relPosRepository.existsWithPrimaryKey(this);
        }
        final OnlineShopRelPos rel = new OnlineShopRelPos();
        rel.setPosId(this.posId);
        rel.setOnlineShopId(this.onlineShopId);
        return relPosRepository.selectCount(rel) > 0;
    }

    public void baseValidate(final OnlineShopRepository shopRepository, final PosRepository posRepository) {
        Assert.notNull(this.posId, "pos id must not null");
        Assert.isTrue(posRepository.existsWithPrimaryKey(this.posId), "associate POS must exist");
        Assert.notNull(this.onlineShopId, "online shop id must not null");
        Assert.isTrue(shopRepository.existsWithPrimaryKey(this.onlineShopId), "associate online shop must exist");
    }
    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @GeneratedValue
    private Long onlineShopRelPosId;

    @ApiModelProperty(value = "网店id")
    @NotNull
    private Long onlineShopId;

    @ApiModelProperty(value = "服务点id")
    @NotNull
    private Long posId;

    @ApiModelProperty(value = "是否有效", required = true)
    @NotNull
    @Column(name = "active_flag")
    private Integer activeFlag;

    @ApiModelProperty(value = "是否计算库存", required = true)
    @Column(name = "business_active_flag")
    private Integer businessActiveFlag;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty(value = "网店")
    @Transient
    private OnlineShop onlineShop;

    @ApiModelProperty(value = "服务点")
    @Transient
    private Pos pos;
}