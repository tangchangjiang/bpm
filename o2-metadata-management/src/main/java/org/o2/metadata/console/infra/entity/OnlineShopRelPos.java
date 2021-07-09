package org.o2.metadata.console.infra.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OnlineShopRelPos extends AuditDomain {

    public static final String FIELD_ONLINE_SHOP_REL_POS_ID = "onlineShopRelPosId";
    public static final String FIELD_ONLINE_SHOP_ID = "onlineShopId";
    public static final String FIELD_POS_ID = "posId";
    public static final String FIELD_IS_ACTIVE = "activeFlag";
    public static final String FIELD_BUSINESS_ACTIVE_FLAG = "businessActiveFlag";


    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------
//
//    public boolean exist(final OnlineShopRelWarehouseRepository relPosRepository) {
//        if (this.onlineShopRelPosId != null) {
//            return relPosRepository.existsWithPrimaryKey(this);
//        }
//        final OnlineShopRelPos rel = new OnlineShopRelPos();
//        rel.setPosId(this.posId);
//        rel.setOnlineShopId(this.onlineShopId);
//        return relPosRepository.selectCount(rel) > 0;
//    }
//
//    public void baseValidate(final OnlineShopRepository shopRepository, final WarehouseRepository warehouseRepository) {
//        Assert.notNull(this.posId, "pos id must not null");
//        Assert.isTrue(warehouseRepository.existsWithPrimaryKey(this.posId), "associate POS must exist");
//        Assert.notNull(this.onlineShopId, "online shop id must not null");
//        Assert.isTrue(shopRepository.existsWithPrimaryKey(this.onlineShopId), "associate online shop must exist");
//        Preconditions.checkArgument(null != this.tenantId, BasicDataConstants.ErrorCode.BASIC_DATA_TENANT_ID_IS_NULL);
//    }
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

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
}
