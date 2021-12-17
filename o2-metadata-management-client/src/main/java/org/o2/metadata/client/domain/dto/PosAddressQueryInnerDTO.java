package org.o2.metadata.client.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 详细地址信息
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
@ApiModel("详细地址")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PosAddressQueryInnerDTO {

    @ApiModelProperty("服务点编码")
    private List<String> posCodes;

}
