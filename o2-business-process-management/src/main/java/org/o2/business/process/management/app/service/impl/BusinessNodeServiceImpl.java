package org.o2.business.process.management.app.service.impl;

import io.choerodon.mybatis.domain.AuditDomain;
import lombok.RequiredArgsConstructor;
import org.hzero.mybatis.helper.UniqueHelper;
import org.o2.business.process.management.app.service.BusinessNodeService;
import org.o2.business.process.management.domain.entity.BusinessNode;
import org.o2.business.process.management.domain.repository.BusinessNodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 业务流程节点表应用服务默认实现
 *
 * @author zhilin.ren@hand-china.com 2022-08-10 14:31:01
 */
@Service
@RequiredArgsConstructor
public class BusinessNodeServiceImpl implements BusinessNodeService {

    private final BusinessNodeRepository businessNodeRepository;


    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<BusinessNode> batchSave(List<BusinessNode> businessNodeList) {
        Map<AuditDomain.RecordStatus, List<BusinessNode>> statusMap = businessNodeList.stream().collect(Collectors.groupingBy(BusinessNode::get_status));
        // 删除
        if (statusMap.containsKey(AuditDomain.RecordStatus.delete)) {
            List<BusinessNode> deleteList = statusMap.get(AuditDomain.RecordStatus.delete);
            businessNodeRepository.batchDeleteByPrimaryKey(deleteList);
        }
        // 更新
        if (statusMap.containsKey(AuditDomain.RecordStatus.update)) {
            List<BusinessNode> updateList = statusMap.get(AuditDomain.RecordStatus.update);
            updateList.forEach(item -> {
                // TODO: 唯一性校验
                UniqueHelper.valid(item,BusinessNode.O2BPM_BUSINESS_NODE_U1);
                businessNodeRepository.updateByPrimaryKeySelective(item);
            });
        }
        // 新增
        if (statusMap.containsKey(AuditDomain.RecordStatus.create)) {
            List<BusinessNode> createList = statusMap.get(AuditDomain.RecordStatus.create);
            createList.forEach(item -> {
                // TODO: 唯一性校验
                UniqueHelper.valid(item,BusinessNode.O2BPM_BUSINESS_NODE_U1);
                businessNodeRepository.insertSelective(item);
            });
        }
        return businessNodeList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public BusinessNode save(BusinessNode businessNode) {
        //保存业务流程节点表
        UniqueHelper.valid(businessNode,BusinessNode.O2BPM_BUSINESS_NODE_U1);
        if (businessNode.getBizNodeId() == null) {
            businessNodeRepository.insertSelective(businessNode);
        } else {
            businessNodeRepository.updateOptional(businessNode,
                    BusinessNode.FIELD_BEAN_ID,
                    BusinessNode.FIELD_DESCRIPTION,
                    BusinessNode.FIELD_NODE_TYPE,
                    BusinessNode.FIELD_SCRIPT,
                    BusinessNode.FIELD_ENABLED_FLAG,
                    BusinessNode.FIELD_BUSINESS_TYPE,
                    BusinessNode.FIELD_SUB_BUSINESS_TYPE,
                    BusinessNode.FIELD_TENANT_ID
            );
        }

        return businessNode;
    }
}
