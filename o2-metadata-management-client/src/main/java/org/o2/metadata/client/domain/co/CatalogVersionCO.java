package org.o2.metadata.client.domain.co;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * 目录版本
 *
 * @author yipeng.zhu@hand-china.com 2021-07-30
 **/
@Data
public class CatalogVersionCO {
    /**
     * 版本目录编码
     */
    @ApiModelProperty(value = "版本目录编码")
    private String catalogVersionCode;

    /**
     * 版本目录名称
     */
    @ApiModelProperty(value = "版本目录名称")
    private String catalogVersionName;

    /**
     * 是否有效
     */
    private Integer activeFlag;
}
