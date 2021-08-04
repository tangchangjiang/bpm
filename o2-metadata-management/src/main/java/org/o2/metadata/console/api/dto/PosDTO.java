package org.o2.metadata.console.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.mybatis.domian.SecurityToken;
import org.o2.metadata.console.infra.entity.Pos;

/**
 * @author tingting.wang@hand-china.com 2019-3-25
 */

@ApiModel("服务点视图")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class PosDTO extends Pos {
    @ApiModelProperty("省id")
    private String regionId;

    @ApiModelProperty(value = "省名称", hidden = true)
    private String regionName;

    @ApiModelProperty("市id")
    private String cityId;

    @ApiModelProperty(value = "市名称", hidden = true)
    private String cityName;

    @ApiModelProperty(value = "区id")
    private String districtId;

    @ApiModelProperty(value = "区名称", hidden = true)
    private String districtName;

    @ApiModelProperty(value = "街道地址", hidden = true)
    private String streetName;

    @Override
    public Class<? extends SecurityToken> associateEntityClass() {
        return Pos.class;
    }
}
