package org.o2.metadata.pipeline.app.service;

import org.o2.metadata.pipeline.api.dto.PipelineDTO;
import org.o2.metadata.pipeline.domain.entity.Pipeline;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author huizhen.liu@hand-china.com 2019-01-09
 */
public interface PipelineService {

    /**
     * 批量更新
     *
     * @param pipelines 已修改流水线头
     * @return 更新的条数
     */
    int batchMerge(List<Pipeline> pipelines);

    /**
     * 通过code获取yaml
     *
     * @param id 流程器主键
     * @return 通过id获取Pipeline
     */
    PipelineDTO getPipelineDTO(Long id);

    /**
     * 批量解析并保存yaml流程器
     *
     * @param multipartFiles
     */
    void savePipelineDTOS(List<MultipartFile> multipartFiles);
}
