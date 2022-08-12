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
public class BusinessProcessBO {

    private List<BusinessProcessNodeDO> allNodeAction;
    private Integer enabledFlag;
    private String processCode;
    private Long tenantId;
}
