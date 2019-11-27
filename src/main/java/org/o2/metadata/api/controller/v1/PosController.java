package org.o2.metadata.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.ext.metadata.app.service.OnlineShopRelPosService;
import org.o2.ext.metadata.app.service.PosService;
import org.o2.ext.metadata.config.EnableMetadataClientConsole;
import org.o2.ext.metadata.domain.entity.Pos;
import org.o2.ext.metadata.domain.repository.PosRepository;
import org.o2.ext.metadata.domain.vo.PosVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 服务点信息 管理 API
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */

@RestController("posController.v1")
@RequestMapping("/v1/poses")
@Api(tags = EnableMetadataClientConsole.POS)
public class PosController extends BaseController {
    private final PosRepository posRepository;
    private final PosService posService;
    private final OnlineShopRelPosService onlineShopRelPosService;

    private static final Logger LOG = LoggerFactory.getLogger(PosController.class);

    public PosController(final PosRepository posRepository, final PosService posService,
                         final OnlineShopRelPosService onlineShopRelPosService) {
        this.posRepository = posRepository;
        this.posService = posService;
        this.onlineShopRelPosService = onlineShopRelPosService;
    }

    @ApiOperation(value = "服务点信息列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping
    public ResponseEntity<Page<PosVO>> list(final PosVO pos,
                                            @ApiIgnore final PageRequest pageRequest) {
        final Page<PosVO> posList = PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(),
                () -> posRepository.listPosWithAddressByCondition(pos));
        return Results.success(posList);
    }

    @ApiOperation(value = "服务点信息明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping("/{posId}")
    public ResponseEntity<Pos> detail(@PathVariable final Long posId) {
        final Pos pos = posService.getPosWithPropertiesInRedisByPosId(posId);
        return Results.success(pos);
    }

    @ApiOperation(value = "创建服务点信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<Pos> create(@RequestBody final Pos pos) {
        this.validObject(pos);
        posService.create(pos);
        return Results.success(pos);
    }

    @ApiOperation(value = "修改服务点信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<Pos> update(@RequestBody final Pos pos) {
        SecurityTokenHelper.validToken(pos, true, true);
        this.validObject(pos);
        posService.update(pos);
        //触发网店关联服务点更新
        onlineShopRelPosService.resetIsInvCalculated(null, pos.getPosCode());
        return Results.success(pos);
    }

    @ApiOperation(value = "通过posCode服务点信息明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping("/by/{posCode}")
    public ResponseEntity<Pos> detailByPosCode(@PathVariable final String posCode) {
        final Pos pos = posRepository.getPosByCode(posCode);
        return Results.success(pos);
    }

}
