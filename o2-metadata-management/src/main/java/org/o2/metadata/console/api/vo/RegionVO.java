package org.o2.metadata.console.api.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.o2.metadata.console.domain.entity.Region;

import javax.persistence.Transient;

/**
 * @author tingting@hand-china.com
 * @date 2019/4/30
 */

@ApiModel("区域视图")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegionVO extends Region {

    @ApiModelProperty(value = "子节点数量")
    @Transient
    private Integer childrenCount;
}
