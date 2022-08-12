package org.o2.business.process.management.domain;

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

    private List<BusinessProcessNodeDO> allNodeAction;
    private Integer enabledFlag;
    private Long tenantId;
}
