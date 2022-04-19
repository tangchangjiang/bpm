package org.o2.metadata.client.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 门店查询
 *
 * @author chao.yang05@hand-china.com 2022/4/14
 */
@Data
public class StoreQueryDTO {

    @ApiModelProperty(value = "省")
    private String regionCode;

    @ApiModelProperty(value = "市")
    private String cityCode;

    @ApiModelProperty(value = "区")
    private String districtCode;
}
