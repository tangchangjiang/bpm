package org.o2.metadata.console.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.mybatis.domian.SecurityToken;
import org.o2.metadata.console.infra.constant.WarehouseConstants;
import org.o2.metadata.console.infra.entity.OnlineShopRelWarehouse;

import java.util.Date;

/**
 * 网店关联仓库
 *
 * @author yuying.shi@hand-china.com 2020-03-02
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("网店关联仓库")
@Data
public class OnlineShopRelWarehouseVO extends OnlineShopRelWarehouse {

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "仓库类型")
    @LovValue(lovCode = WarehouseConstants.WarehouseType.LOV_CODE)
    private String warehouseTypeCode;

    private String warehouseTypeMeaning;

    @ApiModelProperty(value = "仓库状态")
    @LovValue(lovCode = WarehouseConstants.WarehouseStatus.LOV_CODE)
    private String warehouseStatusCode;

    private String warehouseStatusMeaning;

    @ApiModelProperty(value = "失效日期")
    private Date activedDateTo;

    @ApiModelProperty(value = "网店编码")
    private String onlineShopCode;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    /**
     * 网店是否启用
     */
    @ApiModelProperty(value = "网店是否启用")
    private Integer onlineShopActiveFlag;

    /**
     * 仓库是否启用
     */
    @ApiModelProperty(value = "仓库是否启用")
    private Integer warehouseActiveFlag;

    /**
     *  是否业务有效（寻源，库存计算 判断关联关系
     */
    @ApiModelProperty(value = "是否业务有效（寻源，库存计算 判断关联关系）")
    private Integer businessActiveFlag;

    @Override
    public Class<? extends SecurityToken> associateEntityClass() {
        return OnlineShopRelWarehouse.class;
    }

}
