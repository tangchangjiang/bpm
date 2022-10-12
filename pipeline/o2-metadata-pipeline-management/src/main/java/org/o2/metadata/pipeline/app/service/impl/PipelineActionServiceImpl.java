package org.o2.metadata.pipeline.app.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.pipeline.app.service.PipelineActionService;
import org.o2.metadata.pipeline.domain.entity.ActionParameter;
import org.o2.metadata.pipeline.domain.entity.PipelineAction;
import org.o2.metadata.pipeline.domain.repository.ActionParameterRepository;
import org.o2.metadata.pipeline.domain.repository.PipelineActionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 流程器行为应用服务默认实现
 *
 * @author wenjun.deng01@hand-china.com 2019-12-16 10:36:04
 */
@Service
public class PipelineActionServiceImpl implements PipelineActionService {

    private final PipelineActionRepository pipelineActionRepository;
    private final ActionParameterRepository actionParameterRepository;

    public PipelineActionServiceImpl(final PipelineActionRepository pipelineActionRepository,
                                     final ActionParameterRepository actionParameterRepository) {
        this.pipelineActionRepository = pipelineActionRepository;
        this.actionParameterRepository = actionParameterRepository;
    }

    @Override
    public void savePipelineAction(PipelineAction pipelineAction, Long organizationId) {
        pipelineAction.setTenantId(organizationId);
        pipelineActionRepository.insertSelective(pipelineAction);
        pipelineActionRepository.cache(pipelineAction);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePipelineAction(PipelineAction pipelineAction, Long organizationId) {
        SecurityTokenHelper.validToken(pipelineAction, false);
        pipelineAction.setTenantId(organizationId);
        pipelineActionRepository.updateByPrimaryKey(pipelineAction);
        pipelineActionRepository.cache(pipelineAction);

        //进行保存(create,delete,update)
        if (CollectionUtils.isNotEmpty(pipelineAction.getParameters())) {
            for (ActionParameter actionParameter : pipelineAction.getParameters()) {
                actionParameter.setTenantId(organizationId);
                actionParameter.save(actionParameterRepository);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PipelineAction pipelineAction, Long organizationId) {
        final List<ActionParameter> actionParameters = actionParameterRepository.selectByCondition(Condition.builder(ActionParameter.class)
                .andWhere(Sqls.custom().
                        andEqualTo(ActionParameter.FIELD_ACTION_ID, pipelineAction.getId()).
                        andEqualTo(ActionParameter.FIELD_TENANT_ID, organizationId)).build());
        pipelineActionRepository.deleteByPrimaryKey(pipelineAction);
        pipelineActionRepository.removeCache(pipelineAction);
        actionParameterRepository.batchDeleteByPrimaryKey(actionParameters);
    }
}
