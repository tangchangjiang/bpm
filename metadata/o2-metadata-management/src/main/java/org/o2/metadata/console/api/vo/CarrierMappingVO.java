package org.o2.metadata.console.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.mybatis.domian.SecurityToken;
import org.o2.metadata.console.infra.entity.CarrierMapping;


/**
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@ApiModel("承运商匹配表视图")
@Data
@EqualsAndHashCode(callSuper = true)
public class CarrierMappingVO extends CarrierMapping {

    @ApiModelProperty("承运商编码")
    private String carrierCode;
    @ApiModelProperty("承运商名称")
    private String carrierName;

    @Override
    public Class<? extends SecurityToken> associateEntityClass() {
        return CarrierMapping.class;
    }
}
