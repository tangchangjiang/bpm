package org.o2.metadata.pipeline.app.service.impl;

import io.choerodon.core.convertor.ApplicationContextHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.o2.core.helper.FastJsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.pipeline.api.vo.PipelineNodeVO;
import org.o2.metadata.pipeline.api.vo.PipelineVO;
import org.o2.metadata.pipeline.app.service.PipelineRedisService;
import org.o2.metadata.pipeline.domain.entity.Pipeline;
import org.o2.metadata.pipeline.domain.entity.PipelineNode;
import org.o2.metadata.pipeline.domain.repository.PipelineNodeRepository;
import org.o2.metadata.pipeline.domain.repository.PipelineRepository;
import org.o2.metadata.pipeline.infra.constants.PipelineConstants;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 流程器Redis操作
 *
 * @author wenjun.deng@hand-china.com 2019-3-28
 */
@Service
public class PipelineRedisServiceImpl implements PipelineRedisService {

    private final PipelineNodeRepository pipelineNodeRepository;

    private final PipelineRepository pipelineRepository;

    public PipelineRedisServiceImpl(final PipelineNodeRepository pipelineNodeRepository,
                                    final PipelineRepository pipelineRepository) {
        this.pipelineNodeRepository = pipelineNodeRepository;
        this.pipelineRepository = pipelineRepository;
    }

    @Override
    public void createRedisPipelineNodeConf(Long pipelineId, Long tenantId) {
        Pipeline pipelineParam = new Pipeline();
        pipelineParam.setId(pipelineId);
        pipelineParam.setTenantId(tenantId);
        final List<Pipeline> pipelineList = pipelineRepository.listPipeline(pipelineParam);
        if (CollectionUtils.isNotEmpty(pipelineList) && pipelineList.size() == 1) {
            Pipeline pipeline = pipelineList.get(0);
            PipelineNode pipelineNode = new PipelineNode();
            pipelineNode.setTenantId(tenantId);
            pipelineNode.setPipelineId(pipelineId);
            final List<PipelineNode> pipelineNodes =
                    pipelineNodeRepository.listPipelineNode(pipelineNode);
            final String pipelineEntityJson = this.buildPipelineConfJson(pipeline, pipelineNodes);

            final Map<String, String> pipelineEntity = new HashMap<>(2);
            pipelineEntity.put(PipelineConstants.Redis.PIPELINE_NODE_INFO, pipelineEntityJson);
            pipelineEntity.put(PipelineConstants.Redis.PIPELINE_VERSION, pipeline.getObjectVersionNumber().toString());
            final RedisCacheClient redisCacheClient = ApplicationContextHelper.getContext().getBean(RedisCacheClient.class);
            redisCacheClient.opsForHash().putAll(buildInterfaceConfKey(tenantId,pipeline.getCode()),
                    pipelineEntity);
        }
    }

    @Override
    public boolean saveRedisPipelineNodeConf(Long pipelineId, Long tenantId) {
        Pipeline pipelineParam = new Pipeline();
        pipelineParam.setId(pipelineId);
        pipelineParam.setTenantId(tenantId);
        final List<Pipeline> pipelineList = pipelineRepository.listPipeline(pipelineParam);
        if (CollectionUtils.isNotEmpty(pipelineList) && pipelineList.size() == 1) {
            Pipeline pipeline = pipelineList.get(0);
            PipelineNode pipelineNode = new PipelineNode();
            pipeline.setTenantId(tenantId);
            pipelineNode.setPipelineId(pipelineId);
            pipelineNode.setTenantId(tenantId);
            final List<PipelineNode> pipelineNodes =
                    pipelineNodeRepository.listPipelineNode(pipelineNode);
            final String pipelineCode = pipeline.getCode();
            final String interfaceEntityJson = this.buildPipelineConfJson(pipeline, pipelineNodes);
            final String pipelineVersion = pipeline.getObjectVersionNumber().toString();

            return this.executeScript(pipelineCode, interfaceEntityJson, pipelineVersion,tenantId);
        }
        return false;
    }

    @Override
    public void delRedisPipelineConf(Long tenantId,String pipelineCode) {
        final RedisCacheClient redisCacheClient = ApplicationContextHelper.getContext().getBean(RedisCacheClient.class);
        redisCacheClient.expire(buildInterfaceConfKey(tenantId,pipelineCode),
                PipelineConstants.Redis.EXPIRE_TIME_MINUTES, TimeUnit.MINUTES);
    }

    @Override
    public String getPipelineConf(final Long tenantId,final String pipelineConfCode) {
        final RedisCacheClient redisCacheClient = ApplicationContextHelper.getContext().getBean(RedisCacheClient.class);
        final String interfaceConfJson = redisCacheClient.<String, String>opsForHash()
                .get(buildInterfaceConfKey(tenantId,pipelineConfCode), PipelineConstants.Redis.PIPELINE_NODE_INFO);
        if (StringUtils.isBlank(interfaceConfJson)) {
            return "";
        }
        return interfaceConfJson;
    }

    private String buildPipelineConfJson(final Pipeline pipeline, final List<PipelineNode> pipelineNodes) {
        final PipelineVO pipelineVO = new PipelineVO();
        pipelineVO.setStartBeanId(pipeline.getStartBeanId());
        pipelineVO.setEndBeanId(pipeline.getEndBeanId());
        pipelineVO.setPipelineCode(pipeline.getCode());
        pipelineVO.setStartScript(pipeline.getStartScript());
        pipelineVO.setEndScript(pipeline.getEndScript());

        for (PipelineNode pipelineNode : pipelineNodes) {
            PipelineNodeVO pipelineNodeVO = new PipelineNodeVO();
            pipelineNodeVO.setCurBeanId(pipelineNode.getCurBeanId());
            pipelineNodeVO.setNextBeanId(pipelineNode.getNextBeanId());
            pipelineNodeVO.setNodeId(pipelineNode.getId());
            pipelineNodeVO.setStrategyType(pipelineNode.getStrategyType());
            pipelineNodeVO.setCurBeanId(pipelineNode.getCurBeanId());
            pipelineNodeVO.setNextBeanId(pipelineNode.getNextBeanId());
            pipelineNodeVO.setScript(pipelineNode.getNextScript());
            pipelineVO.getAllNodeAction().add(pipelineNodeVO.getCurBeanId());
            pipelineVO.getPipelineNodes().put(pipelineNodeVO.uniqueKey(), pipelineNodeVO);
        }

        return FastJsonHelper.objectToString(pipelineVO);
    }

    private boolean executeScript(final String pipelineCode, final String pipelineEntityJson,
                                  final String pipelineVersion) {
        final DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(PipelineConstants.Redis.PIPELINE_CONF_UPDATE_LUA);
        defaultRedisScript.setResultType(Boolean.class);
        final RedisCacheClient redisCacheClient = ApplicationContextHelper.getContext().getBean(RedisCacheClient.class);
        return redisCacheClient.execute(
                defaultRedisScript, Collections.singletonList(buildInterfaceConfKey(pipelineCode)), pipelineEntityJson,
                pipelineVersion);
    }

    /**
     * @param pipelineCode 流程器配置代码
     * @return redis cache key
     */
    private String buildInterfaceConfKey(final String pipelineCode) {
        return String.format(PipelineConstants.Redis.PIPELINE_KEY, pipelineCode);
    }

}

