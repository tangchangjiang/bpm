/*
 * Copyright (C) HAND Enterprise Solutions Company Ltd.
 */

package org.o2.metadata.console.api.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.o2.core.O2CoreConstants;

import javax.persistence.Transient;
import java.util.List;

/**
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@ApiModel("地区父子关系视图")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class RegionTreeChildVO {


    @ApiModelProperty(value = "region 关联")
    private String regionCode;

    @ApiModelProperty(value = "地址类型.值集:O2MD.ADDRESS_TYPE")
    @LovValue(lovCode = O2CoreConstants.AddressType.LOV_CODE)
    private String addressTypeCode;



    @ApiModelProperty(value = "外部区域代码")
    private String externalCode;

    @ApiModelProperty(value = "外部区域名称")
    private String externalName;

    @ApiModelProperty(value = "是否启用")
    private Integer activeFlag;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;



    @ApiModelProperty(value = "查询条件 内部区域名称")
    private String regionName;

    @ApiModelProperty(value = "平台类型含义", hidden = true)
    private String platformTypeMeaning;

    @Transient
    @ApiModelProperty(value = "地址类型含义", hidden = true)
    private String addressTypeMeaning;



    @ApiModelProperty(value = "版本编码")
    private String platformCode;

    @ApiModelProperty(value = "版本名称",required = true)
    private String platformName;
    /**
     * 地区父节点id
     */
    private String parentRegionCode;

    private Long addressMappingId;

    /**
     * 地区子节点集合
     */
    private List<RegionTreeChildVO> children;

    private String levelPath;

    private String _token;

}
