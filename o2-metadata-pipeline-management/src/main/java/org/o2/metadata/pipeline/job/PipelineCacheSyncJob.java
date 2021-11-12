package org.o2.metadata.pipeline.job;

import org.apache.commons.collections.CollectionUtils;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.hzero.core.message.MessageAccessor;
import org.o2.core.helper.JsonHelper;
import org.o2.metadata.pipeline.api.vo.PipelineCreatedResultVO;
import org.o2.metadata.pipeline.app.service.PipelineService;
import org.o2.metadata.pipeline.domain.entity.Pipeline;
import org.o2.metadata.pipeline.domain.repository.PipelineRepository;
import org.o2.metadata.pipeline.infra.constants.PipelineConstants;

import java.util.List;
import java.util.Map;


/**
 * 流程器缓存同步
 *
 * @author wenjun.deng01@hand-china.com 2020-05-26
 */
@JobHandler("pipelineCacheSyncJob")
public class PipelineCacheSyncJob implements IJobHandler {

    private PipelineRepository pipelineRepository;


    private PipelineService pipelineService;

    public PipelineCacheSyncJob(PipelineRepository pipelineRepository, PipelineService pipelineService) {
        this.pipelineRepository = pipelineRepository;
        this.pipelineService = pipelineService;
    }

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {

        List<Pipeline> pipelines = pipelineRepository.selectAll();
        if (CollectionUtils.isEmpty(pipelines)) {
            tool.warn(MessageAccessor.getMessage(PipelineConstants.Message.PIPELINE_NOT_FOUND).desc());
        }
        List<PipelineCreatedResultVO> errorCount = pipelineService.batchMerge(pipelines);
        if (CollectionUtils.isNotEmpty(errorCount)) {
            tool.info(JsonHelper.objectToString(errorCount));
        }
        return ReturnT.SUCCESS;
    }
}
