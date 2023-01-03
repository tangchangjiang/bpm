package org.o2.metadata.console.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 网店
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
@ApiModel("网店基础设置")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OnlineShopQueryInnerDTO {
    /**
     * 查询条件==
     * onlineShopCodes  or  onlineShopNames
     */

    @ApiModelProperty(value = "网点编码")
    private List<String> onlineShopCodes;

    @ApiModelProperty(value = "网店名称")
    private List<String> onlineShopNames;

    @ApiModelProperty(value = "平台编码")
    private String platformCode;

    @ApiModelProperty(value = "平台网店编码")
    private List<String> platformShopCodes;

    @ApiModelProperty(value = "网店类型")
    private String onlineShopType;

    /**
     * 业务类型
     */
    private String businessTypeCode;

    /**
     * 排除的网店编码
     */
    private List<String> excludeOnlineShopCodes;

    /**
     * 网店编码
     */
    private String onlineShopCode;

    /**
     * 网店名称
     */
    private String onlineShopName;

    /**
     * 有效
     */
    private Integer activeFlag;

    private Integer page;

    private Integer size;
}
