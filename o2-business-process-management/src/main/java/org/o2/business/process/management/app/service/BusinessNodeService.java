package org.o2.business.process.management.app.service;

import org.o2.business.process.management.domain.entity.BusinessNode;
import java.util.List;


/**
 * 业务流程节点表应用服务
 *
 * @author zhilin.ren@hand-china.com 2022-08-10 14:31:01
 */
public interface BusinessNodeService {

    
    /**
     * 批量保存业务流程节点表
     *
     * @param businessNodeList 业务流程节点表对象列表
     * @return 业务流程节点表对象列表
     */
    List<BusinessNode> batchSave(List<BusinessNode> businessNodeList);


    /**
     * 保存业务流程节点表
     *
     * @param businessNode 业务流程节点表对象
     * @return 业务流程节点表对象
     */
    BusinessNode save(BusinessNode businessNode);
}
