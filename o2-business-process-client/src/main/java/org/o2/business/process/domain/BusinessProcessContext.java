package org.o2.business.process.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程器VO
 *
 * @author mark.bao@hand-china.com 2019-03-22
 */
@Data
public class BusinessProcessContext {

    private final List<BusinessProcessNodeDO> allNodeAction;
    private final Integer enabledFlag;
    private String pipelineCode;

    public BusinessProcessContext(Integer enabledFlag) {
        this.enabledFlag = enabledFlag;
        this.allNodeAction = new ArrayList<>();
    }
}
