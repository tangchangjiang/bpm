package org.o2.business.process.management.app.service;

import org.o2.business.process.management.domain.entity.BusinessNode;

/**
 * 业务流程节点表应用服务
 *
 * @author zhilin.ren@hand-china.com 2022-08-10 14:31:01
 */
public interface BusinessNodeService {

    /**
     * 根据ID查询业务节点与参数
     *
     * @param bizNodeId 主键
     * @return 结果
     */
    BusinessNode detail(Long bizNodeId);

    /**
     * 保存业务流程节点表
     *
     * @param businessNode 业务流程节点表对象
     * @return 业务流程节点表对象
     */
    BusinessNode save(BusinessNode businessNode);
}
