package org.o2.metadata.domain.onlineshop.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 网店关联仓库
 *
 * @author yuying.shi@hand-china.com 2020-03-02
 */
@ApiModel("网店关联仓库")
public class OnlineShopRelWarehouseDO {

    @ApiModelProperty("表ID，主键，供其他表做外键")
    private Long onlineShopRelWarehouseId;

    @ApiModelProperty(value = "网店id，关联o2md_online_shop.online_shop_id")
    private Long onlineShopId;

    @ApiModelProperty(value = "服务点id,关联o2md_pos.pos_id")
    private Long posId;

    @ApiModelProperty(value = "仓库id，关联o2md_warehouse.warehouse_id")
    private Long warehouseId;

    @ApiModelProperty("是否有效")
    private Integer activeFlag;

    @ApiModelProperty("是否业务有效（寻源，库存计算 判断关联关系）")
    private Integer businessActiveFlag;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public Long getOnlineShopRelWarehouseId() {
        return onlineShopRelWarehouseId;
    }

    public void setOnlineShopRelWarehouseId(Long onlineShopRelWarehouseId) {
        this.onlineShopRelWarehouseId = onlineShopRelWarehouseId;
    }

    public Long getOnlineShopId() {
        return onlineShopId;
    }

    public void setOnlineShopId(Long onlineShopId) {
        this.onlineShopId = onlineShopId;
    }

    public Long getPosId() {
        return posId;
    }

    public void setPosId(Long posId) {
        this.posId = posId;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Integer getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(Integer activeFlag) {
        this.activeFlag = activeFlag;
    }

    public Integer getBusinessActiveFlag() {
        return businessActiveFlag;
    }

    public void setBusinessActiveFlag(Integer businessActiveFlag) {
        this.businessActiveFlag = businessActiveFlag;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "OnlineShopRelWarehouseDO{" +
                "onlineShopRelWarehouseId=" + onlineShopRelWarehouseId +
                ", onlineShopId=" + onlineShopId +
                ", posId=" + posId +
                ", warehouseId=" + warehouseId +
                ", activeFlag=" + activeFlag +
                ", businessActiveFlag=" + businessActiveFlag +
                ", tenantId=" + tenantId +
                '}';
    }
}
