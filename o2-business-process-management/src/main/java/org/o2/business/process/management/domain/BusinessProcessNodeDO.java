package org.o2.business.process.management.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 流程器节点VO
 *
 * @author mark.bao@hand-china.com
 * @date 2022-08-10
 */
@Data
public class BusinessProcessNodeDO {
    @ApiModelProperty("序号")
    private Long sequenceNum;
    @ApiModelProperty("执行bean的id")
    private String beanId;
    @ApiModelProperty("流程节点参数")
    private String args;
    @ApiModelProperty("脚本")
    private String script;
}
