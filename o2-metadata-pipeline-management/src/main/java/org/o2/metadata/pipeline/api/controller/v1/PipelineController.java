package org.o2.metadata.pipeline.api.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseController;
import org.hzero.core.message.MessageAccessor;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.pipeline.api.dto.PipelineDTO;
import org.o2.metadata.pipeline.app.service.PipelineRedisService;
import org.o2.metadata.pipeline.app.service.PipelineService;
import org.o2.metadata.pipeline.config.EnablePipelineManager;
import org.o2.metadata.pipeline.domain.entity.Pipeline;
import org.o2.metadata.pipeline.domain.repository.PipelineRepository;
import org.o2.metadata.pipeline.infra.constants.PipelineConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * 流程器接口
 *
 * @author huizhen.liu@hand-china.com 2019-01-09
 */
@RestController("pipelineController.vPipelineController1")
@RequestMapping("/v1/{organizationId}/pipeline")
@Api(tags = EnablePipelineManager.PIPELINE)
public class PipelineController extends BaseController {
    private static final String APPLICATION_OCTET_STREAM_CHARSET_UTF_8 = "application/octet-stream; charset=utf-8";

    private final PipelineRepository pipelineRepository;
    private final PipelineService pipelineService;
    private final PipelineRedisService pipelineRedisService;

    public PipelineController(final PipelineService pipelineService, final PipelineRepository pipelineRepository, final PipelineRedisService pipelineRedisService) {
        this.pipelineRedisService = pipelineRedisService;
        this.pipelineRepository = pipelineRepository;
        this.pipelineService = pipelineService;
    }

    @ApiOperation(value = "流程器列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<?> list(final Pipeline pipeline, @ApiIgnore @SortDefault(value = Pipeline.FIELD_ID) final PageRequest pageRequest, @PathVariable Long organizationId) {
        pipeline.setTenantId(organizationId);
        return Results.success(PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(), () -> pipelineRepository.listPipeline(pipeline)));
    }

    @ApiOperation(value = "流程器明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable final Long id, @PathVariable Long organizationId) {
        Pipeline pipeline = new Pipeline();
        pipeline.setTenantId(organizationId);
        pipeline.setId(id);
        List<Pipeline> pipelines = pipelineRepository.select(pipeline);
        if (CollectionUtils.isNotEmpty(pipelines) && pipelines.size() == 1) {
            return Results.success(pipelines.get(0));
        } else {
            return Results.error();
        }
    }

    @ApiOperation(value = "批量新增流程器")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<?> create(@RequestBody final List<Pipeline> pipelines, @PathVariable Long organizationId) {
        return createOrUpdatePipelines(pipelines, organizationId);
    }

    private ResponseEntity<?> createOrUpdatePipelines(@RequestBody List<Pipeline> pipelines, @PathVariable Long organizationId) {
        pipelines.forEach(pipeline -> pipeline.setTenantId(organizationId));
        int errorCount = pipelineService.batchMerge(pipelines);
        if (0 == errorCount) {
            return Results.success();
        } else {
            String resultString = MessageAccessor.getMessage(PipelineConstants.Message.PIPELINE_SUCCESS_NUM, String.valueOf((pipelines.size() - errorCount))).desc();
            return Results.success(Collections.singletonList(resultString));
        }
    }

    @ApiOperation(value = "批量修改流程器")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<?> update(@RequestBody final List<Pipeline> pipelines, @PathVariable Long organizationId) {
        return createOrUpdatePipelines(pipelines, organizationId);
    }

    @ApiOperation(value = "批量删除流程器")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody final List<Pipeline> pipelines, @PathVariable Long organizationId) {
        SecurityTokenHelper.validToken(pipelines);
        pipelines.forEach(pipeline -> pipeline.setTenantId(organizationId));
        pipelineRepository.batchDeleteByPrimaryKey(pipelines);
        return Results.success();
    }

    @ApiOperation(value = "流程器Redis缓存信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/cache/{code}")
    public ResponseEntity<?> getRedisConfigInfo(@PathVariable final String code, @PathVariable Long organizationId) {
        return Results.success(pipelineRedisService.getPipelineConf(organizationId,code));
    }

    @ApiOperation(value = "流程器yaml导入")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/yaml/upload", headers = "content-type=multipart/form-data")
    public ResponseEntity<?> uploadPipelineYaml(@ApiParam(value = "上传的文件", required = true) @RequestParam(value = "yamlFiles") List<MultipartFile> multipartFiles, @PathVariable Long organizationId) {
        if (CollectionUtils.isEmpty(multipartFiles)) {
            return new ResponseEntity<>(getExceptionResponse(PipelineConstants.Message.PIPELINE_FILE_IS_NULL), HttpStatus.OK);
        }
        boolean blankFilename = multipartFiles.stream().map(MultipartFile::getOriginalFilename).anyMatch(StringUtils::isBlank);
        if (blankFilename) {
            return new ResponseEntity<>(getExceptionResponse(PipelineConstants.Message.PIPELINE_FILENAME_IS_NULL), HttpStatus.OK);
        }
        pipelineService.savePipeline(multipartFiles);

        return Results.success(MessageAccessor.getMessage(PipelineConstants.Message.PIPELINE_UPLOAD_SUCCESS).desc());
    }

    @ApiOperation(value = "流程器yaml导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/yaml/download/{id}")
    public ResponseEntity<?> downloadPipelineYaml(@PathVariable final Long id, final HttpServletResponse response, @PathVariable Long organizationId) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        PipelineDTO pipelineDTO = pipelineService.getPipelineDTO(id);
        try {
            response.reset(); // 必要地清除response中的缓存信息
            response.setContentType(APPLICATION_OCTET_STREAM_CHARSET_UTF_8);
            mapper.writeValue(response.getOutputStream(), pipelineDTO);
        } catch (IOException e) {
            return Results.error(e);
        }
        return Results.success();
    }


}
