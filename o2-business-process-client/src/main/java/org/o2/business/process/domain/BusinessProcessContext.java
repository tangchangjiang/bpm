package org.o2.business.process.domain;

import lombok.Data;

import java.util.List;

/**
 * 流程器VO
 *
 * @author mark.bao@hand-china.com
 * @date 2019-03-22
 */
@Data
public class BusinessProcessContext {

    private final List<BusinessProcessNodeDO> allNodeAction;
    private final Integer enabledFlag;
    private String pipelineCode;
    private Long tenantId;
}
