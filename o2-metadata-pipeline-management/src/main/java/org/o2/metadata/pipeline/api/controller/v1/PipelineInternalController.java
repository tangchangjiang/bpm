package org.o2.metadata.pipeline.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.util.Results;
import org.o2.core.helper.FastJsonHelper;
import org.o2.metadata.pipeline.api.vo.PipelineVO;
import org.o2.metadata.pipeline.app.service.PipelineRedisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程器内部feign调用
 *
 * @author miao.chen01@hand-china.com 2021-07-23
 */
@RestController("pipelineInternalController.v1")
@RequestMapping("v1/{organizationId}/pipeline")
public class PipelineInternalController {

    private final PipelineRedisService pipelineRedisService;

    public PipelineInternalController(PipelineRedisService pipelineRedisService) {
        this.pipelineRedisService = pipelineRedisService;
    }

    @ApiOperation(value = "获取流程信息")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{code}")
    public ResponseEntity<String> getPipelineByCode(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                    @PathVariable(value = "code") @ApiParam(value = "流程器编码", required = true) String code) {
        return Results.success(pipelineRedisService.getPipelineConf(organizationId, code));
    }
}
