package org.o2.business.process.management.app.service.impl;

import io.choerodon.core.exception.CommonException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.UniqueHelper;
import org.hzero.mybatis.util.Sqls;
import org.o2.business.process.management.app.service.BusinessNodeService;
import org.o2.business.process.management.domain.entity.BizNodeParameter;
import org.o2.business.process.management.domain.entity.BusinessNode;
import org.o2.business.process.management.domain.repository.BizNodeParameterRepository;
import org.o2.business.process.management.domain.repository.BusinessNodeRepository;
import org.o2.business.process.management.domain.repository.BusinessProcessRedisRepository;
import org.o2.business.process.management.infra.constant.BusinessProcessRedisConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 业务流程节点表应用服务默认实现
 *
 * @author zhilin.ren@hand-china.com 2022-08-10 14:31:01
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessNodeServiceImpl implements BusinessNodeService {

    private final BusinessNodeRepository businessNodeRepository;

    private final BizNodeParameterRepository bizNodeParameterRepository;

    private final BusinessProcessRedisRepository businessProcessRedisRepository;

    @Override
    public BusinessNode detail(Long bizNodeId) {
        // 查询节点信息
        BusinessNode businessNode = businessNodeRepository.selectByPrimaryKey(bizNodeId);

        if (Objects.isNull(businessNode)) {
            log.error("BusinessNodeService->detail result is null,bizNodeId:{}", bizNodeId);
            throw new CommonException(BaseConstants.ErrorCode.DATA_NOT_EXISTS);
        }
        // 查询对应节点的参数信息
        List<BizNodeParameter> paramList = bizNodeParameterRepository.selectByCondition(Condition.builder(BizNodeParameter.class)
                .andWhere(Sqls.custom().andEqualTo(BizNodeParameter.FIELD_BEAN_ID, businessNode.getBeanId())
                        .andEqualTo(BizNodeParameter.FIELD_TENANT_ID, businessNode.getTenantId()))
                .build());
        businessNode.setParamList(paramList);
        return businessNode;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BusinessNode save(BusinessNode businessNode) {
        //保存业务流程节点表&参数
        UniqueHelper.isUnique(businessNode, BusinessNode.O2BPM_BUSINESS_NODE_U1);
        if (businessNode.getBizNodeId() == null) {
            businessNodeRepository.insertSelective(businessNode);
        } else {
            businessNodeRepository.updateOptional(businessNode,
                    BusinessNode.FIELD_BEAN_ID,
                    BusinessNode.FIELD_NODE_NAME,
                    BusinessNode.FIELD_DESCRIPTION,
                    BusinessNode.FIELD_NODE_TYPE,
                    BusinessNode.FIELD_SCRIPT,
                    BusinessNode.FIELD_ENABLED_FLAG,
                    BusinessNode.FIELD_BUSINESS_TYPE,
                    BusinessNode.FIELD_SUB_BUSINESS_TYPE,
                    BusinessNode.FIELD_TENANT_ID
            );
        }
        // 更新redis 业务流程节点状态
        String nodeStatusKey = BusinessProcessRedisConstants.BusinessNode.getNodeStatusKey(businessNode.getTenantId());
        businessProcessRedisRepository.updateNodeStatus(nodeStatusKey, businessNode.getBeanId(), businessNode.getEnabledFlag());
        return businessNode;
    }
}
