package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.helper.UniqueHelper;
import org.o2.metadata.console.api.vo.PosVO;
import org.o2.metadata.console.app.service.PosService;
import org.o2.metadata.console.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.console.infra.entity.Pos;
import org.o2.metadata.console.infra.repository.PosRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 服务点信息 管理 API
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */

@RestController("posController.v1")
@RequestMapping("/v1/{organizationId}/poses")
@Api(tags = MetadataManagementAutoConfiguration.POS)
public class PosController extends BaseController {

    private final PosRepository posRepository;
    private final PosService posService;

    public PosController(final PosRepository posRepository, final PosService posService) {
        this.posRepository = posRepository;
        this.posService = posService;
    }

    @ApiOperation(value = "服务点信息列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping
    public ResponseEntity<?> list(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                            final PosVO pos,
                                            @ApiIgnore final PageRequest pageRequest) {
        pos.setTenantId(organizationId);
        final Page<PosVO> posList = PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(),
                () -> posRepository.listPosWithAddressByCondition(pos));
        return Results.success(posList);
    }

    @ApiOperation(value = "服务点信息明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping("/{posId}")
    public ResponseEntity<?> detail(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                      @PathVariable final Long posId) {
        final Pos pos = posService.getPosWithPropertiesInRedisByPosId(organizationId,posId);
        return Results.success(pos);
    }

    @ApiOperation(value = "创建服务点信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<?> create(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody final Pos pos) {
        pos.setTenantId(organizationId);
        validObject(pos);
        if (!UniqueHelper.valid(pos)) {
            return new ResponseEntity<>(getExceptionResponse(BaseConstants.ErrorCode.DATA_EXISTS), HttpStatus.OK);
        }
        posService.create(pos);
        return Results.success(pos);
    }

    @ApiOperation(value = "修改服务点信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<?> update(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody final Pos pos) {
        SecurityTokenHelper.validToken(pos, true, true);
        this.validObject(pos);
        pos.setTenantId(organizationId);
        posService.update(pos);
        //触发网店关联服务点更新
        return Results.success(pos);
    }

    @ApiOperation(value = "通过posCode服务点信息明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping("/by/{posCode}")
    public ResponseEntity<?> detailByPosCode(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @PathVariable final String posCode) {
        final Pos pos = posRepository.getPosByCode(organizationId, posCode);
        return Results.success(pos);
    }

}
