package org.o2.metadata.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.app.service.PosRelCarrierService;
import org.o2.metadata.config.MetadataSwagger;
import org.o2.metadata.domain.entity.PosRelCarrier;
import org.o2.metadata.domain.repository.PosRelCarrierRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 服务点关联承运商 管理 API
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@RestController("posRelCarrierController.v1")
@RequestMapping("/v1/{organizationId}/pos-rel-carriers")
@Api(tags = MetadataSwagger.POS_REL_CARRIER)
public class PosRelCarrierController extends BaseController {
    private final PosRelCarrierRepository posRelCarrierRepository;
    private final PosRelCarrierService posRelCarrierService;

    public PosRelCarrierController(final PosRelCarrierRepository posRelCarrierRepository,
                                   final PosRelCarrierService posRelCarrierService) {
        this.posRelCarrierRepository = posRelCarrierRepository;
        this.posRelCarrierService = posRelCarrierService;
    }

    @ApiOperation(value = "服务点关联承运商列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<?> list(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, final PosRelCarrier posRelCarrier, @ApiIgnore @SortDefault(value = PosRelCarrier.FIELD_PRIORITY,
            direction = Sort.Direction.ASC) final PageRequest pageRequest) {
        posRelCarrier.setTenantId(organizationId);
        final Page<PosRelCarrier> list = PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(),
                () -> posRelCarrierRepository.listCarrierWithPos(posRelCarrier));
        return Results.success(list);
    }

    @ApiOperation(value = "服务点关联承运商明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{relId}")
    public ResponseEntity<?> detail(@PathVariable final Long relId) {
        final PosRelCarrier posRelCarrier = posRelCarrierRepository.selectByPrimaryKey(relId);
        return Results.success(posRelCarrier);
    }

    @ApiOperation(value = "批量创建或更新服务点关联承运商")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<?> create(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,@RequestBody final List<PosRelCarrier> posRelCarriers) {
        final List<PosRelCarrier> resultList = posRelCarrierService.batchMerge(organizationId,posRelCarriers);
        return Results.success(resultList);
    }

    @ApiOperation(value = "修改服务点关联承运商")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<?> update(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,@RequestBody final PosRelCarrier posRelCarrier) {
        SecurityTokenHelper.validToken(posRelCarrier);
        posRelCarrier.setTenantId(organizationId);
        posRelCarrierRepository.updateByPrimaryKeySelective(posRelCarrier);
        return Results.success(posRelCarrier);
    }

    @ApiOperation(value = "批量删除服务点关联承运商")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody final List<PosRelCarrier> posRelCarrieies) {
        SecurityTokenHelper.validToken(posRelCarrieies);
        posRelCarrierRepository.batchDeleteByPrimaryKey(posRelCarrieies);
        return Results.success();
    }

}
