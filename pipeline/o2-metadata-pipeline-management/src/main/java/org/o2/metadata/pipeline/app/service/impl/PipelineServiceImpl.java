package org.o2.metadata.pipeline.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.choerodon.core.exception.CommonException;
import org.hzero.core.message.MessageAccessor;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.core.helper.TransactionalHelper;
import org.o2.metadata.pipeline.api.dto.PipelineDTO;
import org.o2.metadata.pipeline.api.dto.PipelineNodeDTO;
import org.o2.metadata.pipeline.api.vo.PipelineCreatedResultVO;
import org.o2.metadata.pipeline.app.service.PipelineNodeService;
import org.o2.metadata.pipeline.app.service.PipelineRedisService;
import org.o2.metadata.pipeline.app.service.PipelineService;
import org.o2.metadata.pipeline.domain.entity.Pipeline;
import org.o2.metadata.pipeline.domain.entity.PipelineNode;
import org.o2.metadata.pipeline.domain.repository.PipelineNodeRepository;
import org.o2.metadata.pipeline.domain.repository.PipelineRepository;
import org.o2.metadata.pipeline.infra.constants.PipelineConstants;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author huizhen.liu@hand-china.com 2019-01-09
 */
@Service
public class PipelineServiceImpl implements PipelineService {

    private final PipelineRepository pipelineRepository;
    private final PipelineNodeRepository pipelineNodeRepository;
    private final TransactionalHelper transactionalHelper;
    private final PipelineNodeService pipelineNodeService;
    private final PipelineRedisService pipelineRedisService;

    public PipelineServiceImpl(final PipelineRepository pipelineRepository,
                               final TransactionalHelper transactionalHelper,
                               final PipelineRedisService pipelineRedisService,
                               final PipelineNodeRepository pipelineNodeRepository,
                               final PipelineNodeService pipelineNodeService) {
        this.pipelineRepository = pipelineRepository;
        this.pipelineNodeRepository = pipelineNodeRepository;
        this.pipelineRedisService = pipelineRedisService;
        this.transactionalHelper = transactionalHelper;
        this.pipelineNodeService = pipelineNodeService;
    }

    @Override
    public List<PipelineCreatedResultVO> batchMerge(final List<Pipeline> pipelines) {
        List<PipelineCreatedResultVO> result = new ArrayList<>(pipelines.size());
        for (Pipeline pipeline : pipelines) {
            // 非空字段校验
            pipeline.validate();
            PipelineCreatedResultVO vo = new PipelineCreatedResultVO();
            vo.setCode(pipeline.getCode());
            // 更新
            if (pipeline.getId() != null) {
                Pipeline original = pipelineRepository.selectByPrimaryKey(pipeline);
                if (original.getCode().equals(pipeline.getCode())){
                    this.transactionalHelper.transactionOperation(() -> {
                        pipelineRepository.updateByPrimaryKey(pipeline);
                        pipelineRedisService.saveRedisPipelineNodeConf(pipeline.getId(), pipeline.getTenantId());
                    });
                    vo.setSuccess(true);
                } else {
                    vo.setSuccess(false);
                    vo.setMsg(MessageAccessor.getMessage(PipelineConstants.ErrorCode.PIPELINE_CODE_FORBID_UPDATE).getDesc());
                }
            } else {
                // 新建
                boolean exist = validPipelineCode(pipeline);
                if (exist) {
                    vo.setSuccess(false);
                    vo.setMsg(MessageAccessor.getMessage(PipelineConstants.ErrorCode.PIPELINE_CODE_NOT_UNIQUE).getDesc());
                } else {
                    this.transactionalHelper.transactionOperation(() -> {
                        pipelineRepository.insertSelective(pipeline);
                        pipelineRedisService.createRedisPipelineNodeConf(pipeline.getId(), pipeline.getTenantId());
                    });
                    vo.setSuccess(true);
                }
            }
            result.add(vo);
        }
        return result;
    }

    private boolean validPipelineCode(Pipeline pipeline) {
        Sqls sql = Sqls.custom();
        sql.andEqualTo(Pipeline.FIELD_CODE, pipeline.getCode());
        sql.andEqualTo(Pipeline.FIELD_TENANT_ID,pipeline.getTenantId());
        return   pipelineRepository.selectCountByCondition(Condition.builder(Pipeline.class).andWhere(sql).build()) > 0;
    }

    @Override
    public PipelineDTO getPipelineDTO(Long id) {
        final Pipeline pipeline = pipelineRepository.selectByPrimaryKey(id);
        final List<PipelineNode> pipelineNodes = pipelineNodeRepository.select(PipelineNode.FIELD_PIPELINE_ID, id);
        if (null == pipeline) {
            throw new CommonException(PipelineConstants.ErrorCode.PIPELINE_NOT_FIND);
        }
        return this.buildPipelineDTO(pipeline, pipelineNodes);
    }

    @Override
    public void savePipeline(List<MultipartFile> multipartFiles) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Pipeline pipeline;
        final BeanCopier pipelineCopier = BeanCopier.create(PipelineDTO.class, Pipeline.class, false);
        final BeanCopier pipelineNodeCopier = BeanCopier.create(PipelineNodeDTO.class, PipelineNode.class, false);
        for (MultipartFile file : multipartFiles) {
            try {
                // 直接读取流，转化为对象，不存储文件
                PipelineDTO pipelineDTO = mapper.readValue(file.getInputStream(), PipelineDTO.class);
                pipeline = new Pipeline();
                pipelineCopier.copy(pipelineDTO, pipeline, null);
                List<Pipeline> pipelines = new ArrayList<>(1);
                pipelines.add(pipeline);
                this.batchMerge(pipelines);
                if (null == pipeline.getId()) {
                    throw new CommonException(PipelineConstants.ErrorCode.PIPELINE_ALREADY_EXISTS, pipeline.getCode());
                }
                Map<String, PipelineNodeDTO> pipelineDtoNodes = pipelineDTO.getPipelineNodes();
                List<PipelineNode> pipelineNodes = new ArrayList<>(pipelineDtoNodes.size());
                Pipeline finalPipeline = pipeline;
                pipelineDtoNodes.forEach((key, pipelineNodeDTO) -> {
                    PipelineNode pipelineNode = new PipelineNode();
                    pipelineNodeCopier.copy(pipelineNodeDTO, pipelineNode, null);
                    pipelineNode.setPipelineId(finalPipeline.getId());
                    pipelineNodes.add(pipelineNode);
                });
                pipelineNodeService.batchMerge(pipelineNodes);
            } catch (IOException e) {
                throw new CommonException(PipelineConstants.ErrorCode.PIPELINE_UPLOAD_ERROR, e.getMessage());
            }
        }
    }

    private PipelineDTO buildPipelineDTO(final Pipeline pipeline, final List<PipelineNode> pipelineNodes) {
        final PipelineDTO pipelineDTO = new PipelineDTO();
        pipelineDTO.setStartAction(pipeline.getStartAction());
        pipelineDTO.setEndAction(pipeline.getEndAction());
        pipelineDTO.setCode(pipeline.getCode());
        pipelineDTO.setActiveFlag(pipeline.getActiveFlag());
        pipelineDTO.setTenantId(pipeline.getTenantId());
        for (PipelineNode pipelineNode : pipelineNodes) {
            PipelineNodeDTO pipelineNodeDTO = new PipelineNodeDTO();
            pipelineNodeDTO.setCurAction(pipelineNode.getCurAction());
            pipelineNodeDTO.setNextAction(pipelineNode.getNextAction());
            pipelineNodeDTO.setCurDescription(pipelineNode.getCurDescription());
            pipelineNodeDTO.setTenantId(pipeline.getTenantId());
            pipelineNodeDTO.setStrategyType(pipelineNode.getStrategyType());
            pipelineDTO.getPipelineNodes().put(pipelineNodeDTO.uniqueKey(), pipelineNodeDTO);
        }

        return pipelineDTO;
    }
}
