package org.o2.metadata.domain.carrier.domain;


import io.swagger.annotations.ApiModelProperty;


/**
 *
 * 承运商
 *
 * @author yipeng.zhu@hand-china.com 2021-08-05
 **/

public class CarrierDO {
    private Long carrierId;

    @ApiModelProperty(value = "承运商编码")
    private String carrierCode;

    @ApiModelProperty(value = "承运商名称")
     private String carrierName;

    @ApiModelProperty(value = "承运商类型.值集:O2MD.CARRIER_TYPE")
     private String carrierTypeCode;

    @ApiModelProperty(value = "是否有效")
      private Integer activeFlag;


    @ApiModelProperty(value = "组织ID")
    private Long tenantId;

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getCarrierTypeCode() {
        return carrierTypeCode;
    }

    public void setCarrierTypeCode(String carrierTypeCode) {
        this.carrierTypeCode = carrierTypeCode;
    }

    public Long getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(Long carrierId) {
        this.carrierId = carrierId;
    }

    public Integer getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(Integer activeFlag) {
        this.activeFlag = activeFlag;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "CarrierDO{" +
                "carrierId=" + carrierId +
                ", carrierCode='" + carrierCode + '\'' +
                ", carrierName='" + carrierName + '\'' +
                ", carrierTypeCode='" + carrierTypeCode + '\'' +
                ", activeFlag=" + activeFlag +
                ", tenantId=" + tenantId +
                '}';
    }
}
