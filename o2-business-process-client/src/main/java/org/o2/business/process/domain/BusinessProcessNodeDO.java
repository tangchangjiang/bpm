package org.o2.business.process.domain;

import lombok.Data;

/**
 * 流程器节点VO
 *
 * @author mark.bao@hand-china.com 2019-03-22
 */
@Data
public class BusinessProcessNodeDO {
    private Long sequenceNum;
    private String beanId;
    private String nodeCode;
    private String args;
    private String script;
}
