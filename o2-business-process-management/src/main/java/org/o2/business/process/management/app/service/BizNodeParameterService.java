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
    List<BizNodeParameter> batchSave(List<BizNodeParameter> bizNodeParameterList, boolean insertFlag);


    /**
     * 保存业务节点参数表
     *
     * @param bizNodeParameter 业务节点参数表对象
     * @return 业务节点参数表对象
     */
    BizNodeParameter save(BizNodeParameter bizNodeParameter);


    /**
     * 根据beanIdList 查询参数信息
     *
     * @param beanIdList beanIdList
     * @param tenantId   租户id
     * @return 参数列表
     */
    List<BizNodeParameter> getBizNodeParameterList(List<String> beanIdList, Long tenantId);
}
