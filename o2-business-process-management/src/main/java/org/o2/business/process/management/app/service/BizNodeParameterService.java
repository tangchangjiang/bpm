package org.o2.business.process.management.app.service;

import org.o2.business.process.management.domain.entity.BizNodeParameter;
import java.util.List;


/**
 * 业务节点参数表应用服务
 *
 * @author zhilin.ren@hand-china.com 2022-08-10 14:31:01
 */
public interface BizNodeParameterService {

    
    /**
     * 批量保存业务节点参数表
     *
     * @param bizNodeParameterList 业务节点参数表对象列表
     * @return 业务节点参数表对象列表
     */
    List<BizNodeParameter> batchSave(List<BizNodeParameter> bizNodeParameterList);


    /**
     * 保存业务节点参数表
     *
     * @param bizNodeParameter 业务节点参数表对象
     * @return 业务节点参数表对象
     */
    BizNodeParameter save(BizNodeParameter bizNodeParameter);
}
