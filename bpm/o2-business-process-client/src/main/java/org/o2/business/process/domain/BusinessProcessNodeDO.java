package org.o2.business.process.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 流程器节点VO
 *
 * @author mark.bao@hand-china.com
 * @date 2019-03-22
 */
@Data
public class BusinessProcessNodeDO {
    @ApiModelProperty("序号")
    private Long sequenceNum;
    @ApiModelProperty("执行bean的id")
    private String beanId;
    @ApiModelProperty("节点是否生效")
    private Integer enabledFlag;
    @ApiModelProperty("流程节点参数")
    private String args;
    @ApiModelProperty("脚本")
    private String script;
}
