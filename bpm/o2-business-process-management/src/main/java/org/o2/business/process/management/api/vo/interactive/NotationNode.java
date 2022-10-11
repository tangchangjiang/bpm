package org.o2.business.process.management.api.vo.interactive;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/9/28 15:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NotationNode extends Cell {

    @ApiModelProperty("坐标")
    private Position position;

    @ApiModelProperty("大小")
    private Size size;

    @ApiModelProperty("流节点")
    private FlowNode data;

    private Integer zIndex;

    private NotationNodeAttr attrs;

}
