package org.o2.metadata.core.domain.entity;

import com.google.common.base.Preconditions;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.o2.metadata.core.domain.repository.OnlineShopRelWarehouseRepository;
import org.o2.metadata.core.domain.repository.OnlineShopRepository;
import org.o2.metadata.core.domain.repository.WarehouseRepository;
import org.o2.metadata.core.domain.vo.OnlineShopRelWarehouseVO;
import org.o2.metadata.core.infra.constants.BasicDataConstants;
import org.o2.metadata.core.infra.constants.MetadataConstants;
import org.springframework.util.Assert;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.*;
import java.util.HashMap;
import java.util.Map;

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
@Table(name = "o2md_online_shop_rel_warehouse")
public class OnlineShopRelWarehouse extends AuditDomain {

    public static final String FIELD_ONLINE_SHOP_REL_WAREHOUSE_ID = "onlineShopRelWarehouseId";
    public static final String FIELD_ONLINE_SHOP_ID = "onlineShopId";
    public static final String FIELD_POS_ID = "posId";
    public static final String FIELD_WAREHOUSE_ID = "warehouseId";
    public static final String FIELD_ACTIVE_FLAG = "activeFlag";
    public static final String FIELD_BUSINESS_ACTIVE_FLAG = "businessActiveFlag";
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

    @ApiModelProperty(value = "服务点id,关联o2md_pos.pos_id")
    @NotNull
    private Long posId;

    @ApiModelProperty(value = "仓库id，关联o2md_warehouse.warehouse_id")
    @NotNull
    private Long warehouseId;

    @ApiModelProperty("是否有效")
    @NotNull
    @Max(1)
    @Min(0)
    private Integer activeFlag;

    @ApiModelProperty("是否业务有效（寻源，库存计算 判断关联关系）")
    @NotNull
    @Max(1)
    @Min(0)
    private Integer businessActiveFlag;


    @ApiModelProperty(value = "租户ID")
    @NotNull
    private Long tenantId;



    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public boolean exist(final OnlineShopRelWarehouseRepository relPosRepository) {
        if (this.onlineShopRelWarehouseId != null) {
            return relPosRepository.existsWithPrimaryKey(this);
        }
        final OnlineShopRelWarehouseVO rel = new OnlineShopRelWarehouseVO();
        rel.setPosId(this.posId);
        rel.setWarehouseId(this.warehouseId);
        rel.setOnlineShopId(this.onlineShopId);
        return relPosRepository.selectCount(rel) > 0;
    }

    public void baseValidate(final OnlineShopRepository shopRepository, final WarehouseRepository warehouseRepository) {
        Assert.notNull(this.posId, "pos id must not null");
        Assert.notNull(this.warehouseId, "warehouseId id must not null");
        Assert.isTrue(warehouseRepository.existsWithPrimaryKey(this.warehouseId), "associate warehouse must exist");
        Assert.notNull(this.onlineShopId, "online shop id must not null");
        Assert.isTrue(shopRepository.existsWithPrimaryKey(this.onlineShopId), "associate online shop must exist");
        Preconditions.checkArgument(null != this.tenantId, BasicDataConstants.ErrorCode.BASIC_DATA_TENANT_ID_IS_NULL);
    }

    /**
     * 拼写RedisHashMap
     * @param posCode            posCode
     * @param warehouseCode      warehouseCode
     * @param businessActiveFlag businessActiveFlag
     * @return
     */
    public Map<String, Object> getRedisHashMap(final String posCode,final String warehouseCode,final Integer businessActiveFlag) {
        return new HashMap<String, Object>(3) {
            {
                put(MetadataConstants.OnlineShopRelWarehouse.FIELD_POS_CODE,posCode);
                put(MetadataConstants.OnlineShopRelWarehouse.FIELD_WAREHOUSE_CODE,warehouseCode);
                put(MetadataConstants.OnlineShopRelWarehouse.FIELD_BUSINESS_ACTIVE_FLAG,String.valueOf(businessActiveFlag));

            }
        };
    }

    /**
     * 拼写hashKey
     *
     * @param posCode posCode
     * @param warehouseCode warehouseCode
     * @param posCode posCode
     * @return hashKey
     */
    public String getRedisHashKey(final String posCode,final String warehouseCode) {
        return  String.format(MetadataConstants.OnlineShopRelWarehouse.KEY_ONLINE_SHOP_REL_WAREHOUSE, this.tenantId,posCode,warehouseCode);
    }
}
