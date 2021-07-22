package org.o2.metadata.domain.warehouse.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 仓库
 *
 * @author yuying.shi@hand-china.com 2020-03-02
 */
@ApiModel("仓库表")
public class WarehouseDO  {

    @ApiModelProperty("表ID，主键，供其他表做外键")
    private Long warehouseId;


    @ApiModelProperty(value = "服务点id，关联到 o2md_pos.pos_id")
    private Long posId;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "仓库状态,值集：O2MD.WAREHOUSE_STATUS")
    private String warehouseStatusCode;

    @ApiModelProperty(value = "仓库类型,值集: O2MD.WAREHOUSE_TYPE （良品仓/不良品仓/退货仓）")
    private String warehouseTypeCode;

    @ApiModelProperty(value = "自提发货接单量")
    private Long pickUpQuantity;

    @ApiModelProperty(value = "配送发货接单量")
    private Long expressedQuantity;

    @ApiModelProperty("仓库自提标识")
    private Integer pickedUpFlag;

    @ApiModelProperty("仓库快递发货")
    private Integer expressedFlag;

    @ApiModelProperty(value = "仓库评分")
    private Long score;

    @ApiModelProperty(value = "有效日期从")
    private Date activedDateFrom;

    @ApiModelProperty(value = "有效日期从")
    private Date activedDateTo;

    @ApiModelProperty(value = "库存组织编码(物权归属)")
    private String invOrganizationCode;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "生效状态")
    private Integer activeFlag;


    private String posCode;

    private String posName;

    private String warehouseStatusMeaning;

    private String warehouseTypeMeaning;

    private String expressLimitValue;

    private String pickUpLimitValue;

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Long getPosId() {
        return posId;
    }

    public void setPosId(Long posId) {
        this.posId = posId;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getWarehouseStatusCode() {
        return warehouseStatusCode;
    }

    public void setWarehouseStatusCode(String warehouseStatusCode) {
        this.warehouseStatusCode = warehouseStatusCode;
    }

    public String getWarehouseTypeCode() {
        return warehouseTypeCode;
    }

    public void setWarehouseTypeCode(String warehouseTypeCode) {
        this.warehouseTypeCode = warehouseTypeCode;
    }

    public Long getPickUpQuantity() {
        return pickUpQuantity;
    }

    public void setPickUpQuantity(Long pickUpQuantity) {
        this.pickUpQuantity = pickUpQuantity;
    }

    public Long getExpressedQuantity() {
        return expressedQuantity;
    }

    public void setExpressedQuantity(Long expressedQuantity) {
        this.expressedQuantity = expressedQuantity;
    }

    public Integer getPickedUpFlag() {
        return pickedUpFlag;
    }

    public void setPickedUpFlag(Integer pickedUpFlag) {
        this.pickedUpFlag = pickedUpFlag;
    }

    public Integer getExpressedFlag() {
        return expressedFlag;
    }

    public void setExpressedFlag(Integer expressedFlag) {
        this.expressedFlag = expressedFlag;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Date getActivedDateFrom() {
        return activedDateFrom;
    }

    public void setActivedDateFrom(Date activedDateFrom) {
        this.activedDateFrom = activedDateFrom;
    }

    public Date getActivedDateTo() {
        return activedDateTo;
    }

    public void setActivedDateTo(Date activedDateTo) {
        this.activedDateTo = activedDateTo;
    }

    public String getInvOrganizationCode() {
        return invOrganizationCode;
    }

    public void setInvOrganizationCode(String invOrganizationCode) {
        this.invOrganizationCode = invOrganizationCode;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(Integer activeFlag) {
        this.activeFlag = activeFlag;
    }

    public String getPosCode() {
        return posCode;
    }

    public void setPosCode(String posCode) {
        this.posCode = posCode;
    }

    public String getPosName() {
        return posName;
    }

    public void setPosName(String posName) {
        this.posName = posName;
    }

    public String getWarehouseStatusMeaning() {
        return warehouseStatusMeaning;
    }

    public void setWarehouseStatusMeaning(String warehouseStatusMeaning) {
        this.warehouseStatusMeaning = warehouseStatusMeaning;
    }

    public String getWarehouseTypeMeaning() {
        return warehouseTypeMeaning;
    }

    public void setWarehouseTypeMeaning(String warehouseTypeMeaning) {
        this.warehouseTypeMeaning = warehouseTypeMeaning;
    }

    public String getExpressLimitValue() {
        return expressLimitValue;
    }

    public void setExpressLimitValue(String expressLimitValue) {
        this.expressLimitValue = expressLimitValue;
    }

    public String getPickUpLimitValue() {
        return pickUpLimitValue;
    }

    public void setPickUpLimitValue(String pickUpLimitValue) {
        this.pickUpLimitValue = pickUpLimitValue;
    }

    @Override
    public String toString() {
        return "WarehouseDO{" +
                "warehouseId=" + warehouseId +
                ", posId=" + posId +
                ", warehouseCode='" + warehouseCode + '\'' +
                ", warehouseName='" + warehouseName + '\'' +
                ", warehouseStatusCode='" + warehouseStatusCode + '\'' +
                ", warehouseTypeCode='" + warehouseTypeCode + '\'' +
                ", pickUpQuantity=" + pickUpQuantity +
                ", expressedQuantity=" + expressedQuantity +
                ", pickedUpFlag=" + pickedUpFlag +
                ", expressedFlag=" + expressedFlag +
                ", score=" + score +
                ", activedDateFrom=" + activedDateFrom +
                ", activedDateTo=" + activedDateTo +
                ", invOrganizationCode='" + invOrganizationCode + '\'' +
                ", tenantId=" + tenantId +
                ", activeFlag=" + activeFlag +
                ", posCode='" + posCode + '\'' +
                ", posName='" + posName + '\'' +
                ", warehouseStatusMeaning='" + warehouseStatusMeaning + '\'' +
                ", warehouseTypeMeaning='" + warehouseTypeMeaning + '\'' +
                ", expressLimitValue='" + expressLimitValue + '\'' +
                ", pickUpLimitValue='" + pickUpLimitValue + '\'' +
                '}';
    }
}
