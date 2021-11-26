package org.o2.metadata.console.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 仓库关联服务点
 * @author zhilin.ren@hand-china.com 2021/11/26 14:41
 */
@Data
public class WarehouseRelPosVO {

    /**
     * 仓库ID
     */
    @ApiModelProperty(value = "仓库ID")
    private Long warehouseId;
    /**
     * 仓库编码
     */
    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    /**
     * 仓库名称
     */
    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String contact;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobilePhone;


    /**
     * 服务点编码
     */
    @ApiModelProperty(value = "服务点编码")
    private String postcode;

    /**
     * 国家
     */
    @ApiModelProperty(value = "国家")
    private String countryCode;
    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    private String regionCode;

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private String cityCode;
    /**
     * 区
     */
    @ApiModelProperty(value = "区")
    private String districtCode;
    /**
     * 国家
     */
    @ApiModelProperty(value = "国家")
    private String countryName;
    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    private String regionName;
    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private String cityName;
    /**
     * 区
     */
    @ApiModelProperty(value = "区")
    private String districtName;
    /**
     * 街道,门牌号
     */
    @ApiModelProperty(value = "街道,门牌号")
    private String streetName;

    /**
     * 租户ID
     */
    @ApiModelProperty(value = "租户ID")
    private Long tenantId;





}
