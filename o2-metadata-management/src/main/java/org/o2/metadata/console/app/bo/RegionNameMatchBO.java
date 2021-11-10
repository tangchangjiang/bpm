package org.o2.metadata.console.app.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * 地区名称模糊匹配
 *
 * @author yipeng.zhu@hand-china.com 2021-10-19
 **/
@Data
public class RegionNameMatchBO {
    /**
     * 地区名称
     */
    private String regionName;

    /**
     *  级别
     */
    private Integer levelNumber;

    /**
     *  级别
     */
    @ApiModelProperty(value = "外部区域代码")
    private String externalCode;

    /**
     *  级别
     */
    @ApiModelProperty(value = "外部区域名称")
    private String externalName;
}
