package org.o2.metadata.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.mybatis.domian.SecurityToken;
import org.o2.boot.metadata.constants.MetadataConstants;
import org.o2.ext.metadata.domain.entity.OnlineShopRelPos;

/**
 * 网店关联服务点列表视图对象
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@ApiModel("网点服务点关联关系视图")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class OnlineShopRelPosVO extends OnlineShopRelPos {

    @ApiModelProperty(value = "服务点编码")
    private String posCode;

    @ApiModelProperty(value = "服务点名称")
    private String posName;

    @ApiModelProperty(value = "服务点状态")
    @LovValue(lovCode = MetadataConstants.PosStatus.LOV_CODE)
    private String posStatus;

    @ApiModelProperty(value = "服务点类型")
    @LovValue(lovCode = MetadataConstants.PosType.LOV_CODE)
    private String posType;

    @ApiModelProperty(value = "服务点状态含义")
    private String posStatusMeaning;

    @ApiModelProperty(value = "服务点类型含义")
    private String posTypeMeaning;

    @Override
    public Class<? extends SecurityToken> associateEntityClass() {
        return OnlineShopRelPos.class;
    }
}
