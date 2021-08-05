package org.o2.metadata.console.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import org.o2.core.response.BatchResponse;
import org.o2.metadata.console.infra.entity.MallLangPrompt;
import org.o2.metadata.console.infra.repository.MallLangPromptRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.console.app.service.MallLangPromptService;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;
import io.swagger.annotations.ApiParam;
import java.util.List;

/**
 * 商城前端多语言内容维护表 管理 API
 *
 * @author changjiang.tang@hand-china.com 2021-08-05 09:57:27
 */
@RestController("mallLangPromptController.v1")
@RequestMapping("/v1/{organizationId}/mall-lang-prompts")
public class MallLangPromptController extends BaseController {

    private final MallLangPromptRepository mallLangPromptRepository;

    private final MallLangPromptService mallLangPromptService;

    public MallLangPromptController(MallLangPromptRepository mallLangPromptRepository,
                                    MallLangPromptService mallLangPromptService){
        this.mallLangPromptService = mallLangPromptService;
        this.mallLangPromptRepository = mallLangPromptRepository;
    }

    @ApiOperation(value = "商城前端多语言内容维护表维护-分页查询商城前端多语言内容维护表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<MallLangPrompt>> page(@PathVariable(value = "organizationId") Long organizationId,
                                                            MallLangPrompt mallLangPrompt,
                                                            @ApiIgnore @SortDefault(value = MallLangPrompt.FIELD_LANG_PROMPT_ID,
                                                                     direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<MallLangPrompt> list = mallLangPromptRepository.pageAndSort(pageRequest, mallLangPrompt);
        return Results.success(list);
    }

    @ApiOperation(value = "商城前端多语言内容维护表维护-查询商城前端多语言内容维护表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{langPromptId}")
    public ResponseEntity<MallLangPrompt> detail(@PathVariable(value = "organizationId") Long organizationId,
                                                        @ApiParam(value = "商城前端多语言内容维护表ID", required = true) @PathVariable Long langPromptId) {
        MallLangPrompt mallLangPrompt = mallLangPromptRepository.selectByPrimaryKey(langPromptId);
        return Results.success(mallLangPrompt);
    }

    @ApiOperation(value = "商城前端多语言内容维护表维护-创建商城前端多语言内容维护表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<MallLangPrompt> create(@PathVariable(value = "organizationId") Long organizationId,
                                                       @RequestBody MallLangPrompt mallLangPrompt) {
        validObject(mallLangPrompt);
        mallLangPromptService.save(mallLangPrompt);
        return Results.success(mallLangPrompt);
    }

    @ApiOperation(value = "商城前端多语言内容维护表维护-修改商城前端多语言内容维护表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<MallLangPrompt> update(@PathVariable(value = "organizationId") Long organizationId,
                                                       @RequestBody MallLangPrompt mallLangPrompt) {
        SecurityTokenHelper.validToken(mallLangPrompt);
        mallLangPromptService.save(mallLangPrompt);
        return Results.success(mallLangPrompt);
    }

    @ApiOperation(value = "商城前端多语言内容维护表维护-批量保存商城前端多语言内容维护表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/batch-saving")
    public ResponseEntity<List<MallLangPrompt>> batchSave(@PathVariable(value = "organizationId") Long organizationId,
                                                       @RequestBody List<MallLangPrompt> mallLangPromptList) {
        SecurityTokenHelper.validToken(mallLangPromptList);
        mallLangPromptService.batchSave(mallLangPromptList);
        return Results.success(mallLangPromptList);
    }

    @ApiOperation(value = "商城前端多语言内容维护表维护-删除商城前端多语言内容维护表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<Void> remove(@PathVariable(value = "organizationId") Long organizationId,
                                       @RequestBody MallLangPrompt mallLangPrompt) {
        SecurityTokenHelper.validToken(mallLangPrompt);
        mallLangPromptRepository.deleteByPrimaryKey(mallLangPrompt);
        return Results.success();
    }


    @ApiOperation(value = "商城前端多语言内容维护表维护-批量保存商城前端多语言内容维护表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/release")
    public ResponseEntity release(@PathVariable(value = "organizationId") Long organizationId,
                                  @RequestBody List<MallLangPrompt> mallLangPromptList){
        SecurityTokenHelper.validToken(mallLangPromptList);
        BatchResponse<MallLangPrompt> batchResponse = mallLangPromptService.release(mallLangPromptList);
        return batchResponse.getResponseEntity();
    }

}
