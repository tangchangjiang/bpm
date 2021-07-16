package org.o2.metadata.pipeline.infra.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.pipeline.domain.entity.PipelineAction;
import org.o2.metadata.pipeline.domain.repository.PipelineActionRepository;
import org.o2.metadata.pipeline.infra.constants.PipelineConstants;
import org.o2.metadata.pipeline.infra.mapper.PipelineActionMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 流程器行为 资源库实现
 *
 * @author wenjun.deng01@hand-china.com 2019-12-16 10:36:04
 */
@Repository
@Slf4j
public class PipelineActionRepositoryImpl extends BaseRepositoryImpl<PipelineAction> implements PipelineActionRepository {

    private final RedisCacheClient redisCacheClient;
    private final PipelineActionMapper pipelineActionMapper;

    public PipelineActionRepositoryImpl(final RedisCacheClient redisCacheClient,
                                        final PipelineActionMapper pipelineActionMapper) {
        this.redisCacheClient = redisCacheClient;
        this.pipelineActionMapper = pipelineActionMapper;
    }

    @Override
    public List<PipelineAction> listPipelineAction(PipelineAction pipelineAction) {
        return pipelineActionMapper.listPipelineAction(pipelineAction);
    }

    @Override
    public List<PipelineAction> allWithParameters(PipelineAction pipelineAction) {
        return pipelineActionMapper.allWithParameters(pipelineAction);
    }

    @Override
    public void cache(PipelineAction pipelineAction) {
        final String key = cacheKey(pipelineAction);
        final Map<String, String> hash = pipelineAction.hashValue();

        if (log.isDebugEnabled()) {
            log.debug("cache key:[{}], hash:[{}]", key, hash);
        }
        redisCacheClient.opsForHash().putAll(key, hash);
    }

    @Override
    public void removeCache(PipelineAction pipelineAction) {
        final String key = cacheKey(pipelineAction);
        final Object[] keys = {PipelineAction.FIELD_BEAN_ID, PipelineAction.FIELD_SCRIPT};

        if (log.isDebugEnabled()) {
            log.debug("cache key:[{}], keys:[{}]", key, keys);
        }
        redisCacheClient.opsForHash().delete(key, keys);
    }

    @Override
    public String cacheKey(PipelineAction pipelineAction) {
        return String.format(PipelineConstants.Redis.PIPELINE_ACTION, pipelineAction.getTenantId(), pipelineAction.getServiceCode(), pipelineAction.getId());
    }
}
