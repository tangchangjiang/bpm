package org.o2.metadata.pipeline.infra.repository.impl;

import io.choerodon.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.pipeline.domain.entity.ActionParameter;
import org.o2.metadata.pipeline.domain.entity.PipelineAction;
import org.o2.metadata.pipeline.domain.repository.ActionParameterRepository;
import org.o2.metadata.pipeline.domain.repository.PipelineActionRepository;
import org.o2.metadata.pipeline.infra.constants.PipelineConstants;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 行为参数 资源库实现
 *
 * @author wei.cai@hand-china.com 2020-03-18
 */
@Component
@Slf4j
public class ActionParameterRepositoryImpl extends BaseRepositoryImpl<ActionParameter> implements ActionParameterRepository {

    private final PipelineActionRepository pipelineActionRepository;
    private final RedisCacheClient redisCacheClient;

    public ActionParameterRepositoryImpl(final PipelineActionRepository pipelineActionRepository,
                                         final RedisCacheClient redisCacheClient) {
        this.pipelineActionRepository = pipelineActionRepository;
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public void cache(ActionParameter actionParameter) {
        final String key = cacheKey(actionParameter);
        final String value = actionParameter.convertToJsonString();
        if (log.isDebugEnabled()) {
            log.debug("ActionParameter load to Cache, key:[{}], value:[{}]", key, value);
        }
        redisCacheClient.boundHashOps(key).put(String.valueOf(actionParameter.getActionParameterId()), value);
    }

    @Override
    public void removeCache(ActionParameter actionParameter) {
        final String key = cacheKey(actionParameter);
        if (log.isDebugEnabled()) {
            log.debug("ActionParameter remove Cache, key:[{}], index:[{}]", key, actionParameter.getActionParameterId());
        }
        redisCacheClient.boundHashOps(key).delete(String.valueOf(actionParameter.getActionParameterId()));
    }

    @Override
    public String cacheKey(ActionParameter actionParameter) {
        final PipelineAction pipelineAction = pipelineActionRepository.selectByPrimaryKey(actionParameter.getActionId());

        if (Objects.isNull(pipelineAction)) {
            throw new CommonException(BaseConstants.ErrorCode.NOT_NULL);
        }

        return String.format(PipelineConstants.Redis.ACTION_PARAMETER, pipelineAction.getTenantId(), pipelineAction.getServiceCode(), pipelineAction.getId());
    }
}
