package org.o2.metadata.core.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.mybatis.domian.SecurityToken;
import org.o2.metadata.core.domain.entity.OnlineShopRelWarehouse;
import org.o2.metadata.core.infra.constants.MetadataConstants;


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
    private String warehouseStatus;

    private String warehouseStatusMeaning;

    @Override
    public Class<? extends SecurityToken> associateEntityClass() {
        return OnlineShopRelWarehouse.class;
    }

}
