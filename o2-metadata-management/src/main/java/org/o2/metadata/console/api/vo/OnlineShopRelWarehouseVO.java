package org.o2.metadata.console.api.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.mybatis.domian.SecurityToken;
import org.o2.metadata.console.domain.entity.OnlineShop;
import org.o2.metadata.console.domain.entity.OnlineShopRelWarehouse;
import org.o2.metadata.console.domain.entity.Pos;
import org.o2.metadata.console.domain.entity.Warehouse;
import org.o2.metadata.core.infra.constants.MetadataConstants;

import java.util.Date;


/**
 * 网店关联仓库列表视图对象
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@ApiModel("网点服务点关联关系视图")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class OnlineShopRelWarehouseVO extends OnlineShopRelWarehouse {

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "仓库类型")
    @LovValue(lovCode = MetadataConstants.WarehouseType.LOV_CODE)
    private String warehouseTypeCode;

    private String warehouseTypeMeaning;

    @ApiModelProperty(value = "仓库状态")
    @LovValue(lovCode = MetadataConstants.WarehouseStatus.LOV_CODE)
    private String warehouseStatusCode;

    private String warehouseStatusMeaning;

    @ApiModelProperty(value = "失效日期")
    private Date activedDateTo;

    @ApiModelProperty(value = "网店编码")
    private String onlineShopCode;

    @Override
    public Class<? extends SecurityToken> associateEntityClass() {
        return OnlineShopRelWarehouse.class;
    }


    public OnlineShopRelWarehouseVO buildOnlineShopRelWarehouseVO (final Pos pos, final Warehouse warehouse, final OnlineShop onlineShop, final OnlineShopRelWarehouse onlineShopRelWarehouse) {
        OnlineShopRelWarehouseVO onlineShopRelWarehouseVO = new OnlineShopRelWarehouseVO();
        onlineShopRelWarehouseVO.setActivedDateTo(warehouse.getActivedDateTo());
        onlineShopRelWarehouseVO.setActiveFlag(onlineShopRelWarehouse.getActiveFlag());
        onlineShopRelWarehouseVO.setTenantId(onlineShopRelWarehouse.getTenantId());
        onlineShopRelWarehouseVO.setWarehouseCode(warehouse.getWarehouseCode());
        onlineShopRelWarehouseVO.setOnlineShopCode(onlineShop.getOnlineShopCode());
        onlineShopRelWarehouseVO.setBusinessActiveFlag(onlineShopRelWarehouse.getBusinessActiveFlag());
        return onlineShopRelWarehouseVO;
    }
}
