package org.o2.metadata.console.infra.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.o2.metadata.console.infra.repository.OnlineShopRelWarehouseRepository;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * 网店关联仓库
 *
 * @author yuying.shi@hand-china.com 2020-03-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("网店关联仓库")
@VersionAudit
@ModifyAudit
@Table(name = "o2md_shop_rel_warehouse")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OnlineShopRelWarehouse extends AuditDomain {

    public static final String FIELD_ONLINE_SHOP_REL_WAREHOUSE_ID = "onlineShopRelWarehouseId";
    public static final String FIELD_ONLINE_SHOP_ID = "onlineShopId";
    public static final String FIELD_POS_ID = "posId";
    public static final String FIELD_WAREHOUSE_ID = "warehouseId";
    public static final String FIELD_ACTIVE_FLAG = "activeFlag";
    public static final String FIELD_TENANT_ID = "tenantId";

    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @GeneratedValue
    private Long onlineShopRelWarehouseId;

    @ApiModelProperty(value = "网店id，关联o2md_online_shop.online_shop_id")
    @NotNull
    private Long onlineShopId;

    @ApiModelProperty(value = "仓库id，关联o2md_warehouse.warehouse_id")
    @NotNull
    private Long warehouseId;

    @ApiModelProperty("是否有效")
    @NotNull
    @Max(1)
    @Min(0)
    private Integer activeFlag;
    
    @ApiModelProperty(value = "租户ID")
    @NotNull
    private Long tenantId;

    @ApiModelProperty(value = "仓库编码")
    @Transient
    private String warehouseCode;

    @Transient
    private String warehouseStatus;

    @Transient
    private String onlineShopCode;

    @Transient
    private List<String> onlineShopCodes;

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public boolean exist(final OnlineShopRelWarehouseRepository relPosRepository) {
        if (this.onlineShopRelWarehouseId != null) {
            return relPosRepository.existsWithPrimaryKey(this);
        }
        final OnlineShopRelWarehouse rel = new OnlineShopRelWarehouse();
        rel.setWarehouseId(this.warehouseId);
        rel.setOnlineShopId(this.onlineShopId);
        return relPosRepository.selectCount(rel) > 0;
    }

}
