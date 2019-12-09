package org.o2.metadata.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.app.service.OnlineShopRelPosService;
import org.o2.metadata.config.MetadataSwagger;
import org.o2.metadata.domain.entity.OnlineShopRelPos;
import org.o2.metadata.domain.entity.Pos;
import org.o2.metadata.domain.repository.OnlineShopRelPosRepository;
import org.o2.metadata.domain.repository.PosRepository;
import org.o2.metadata.domain.vo.OnlineShopRelPosVO;
import org.o2.metadata.infra.constants.BasicDataConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 网店关联服务点 管理 API
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */

@RestController("onlineShopRelPosController.v1")
@RequestMapping("/v1/{organizationId}")
@Api(tags = MetadataSwagger.ONLINE_SHOP_POS_REL)
public class OnlineShopRelPosController extends BaseController {

    private final OnlineShopRelPosService onlineShopRelPosService;
    private final PosRepository posRepository;
    private final OnlineShopRelPosRepository relationshipRepository;

    public OnlineShopRelPosController(final OnlineShopRelPosService onlineShopRelPosService,
                                      final PosRepository posRepository,
                                      final OnlineShopRelPosRepository relationshipRepository) {
        this.onlineShopRelPosService = onlineShopRelPosService;
        this.posRepository = posRepository;
        this.relationshipRepository = relationshipRepository;
    }

    @ApiOperation(value = "批量创建网店关联服务点关系")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/online-shop-rel-pos/batch-create")
    public ResponseEntity<List<OnlineShopRelPos>> create(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody final List<OnlineShopRelPos> onlineShopRelPosList) {
        this.validList(onlineShopRelPosList);
        final List<OnlineShopRelPos> relationShips = onlineShopRelPosService.batchInsertSelective(organizationId,onlineShopRelPosList);
        return Results.success(relationShips);
    }

    @ApiOperation("批量更新网点关联服务点状态")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/online-shop-rel-pos/batch-update")
    public ResponseEntity<List<OnlineShopRelPos>> batchUpdateActived(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,@RequestBody final List<OnlineShopRelPos> onlineShopRelPoses) {
        this.validList(onlineShopRelPoses);
        SecurityTokenHelper.validToken(onlineShopRelPoses);
        final List<OnlineShopRelPos> relationships = onlineShopRelPosService.batchUpdateByPrimaryKey(organizationId,onlineShopRelPoses);
        return Results.success(relationships);
    }

    @ApiOperation(("查询未与指定网点绑定的服务点"))
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping("/online-shops/{onlineShopId}/unbind-poses")
    public ResponseEntity queryUnbindPoses(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                           @PathVariable("onlineShopId") final Long onlineShopId,
                                           @RequestParam(required = false) final String posCode,
                                           @RequestParam(required = false) final String posName,
                                           @ApiIgnore final PageRequest pageRequest) {
        final Page<Pos> posList = PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(),
                () -> posRepository.listUnbindPosList(onlineShopId, posCode, posName,organizationId));
        return Results.success(posList);
    }

    @ApiOperation(value = "查询与指定网店关联的服务点列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping("/online-shops/{onlineShopId}/shop-pos-relationships")
    public ResponseEntity list(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                               @PathVariable("onlineShopId") final Long onlineShopId,
                               final OnlineShopRelPosVO pos,
                               @ApiIgnore final PageRequest pageRequest) {
        pos.setTenantId(organizationId);
        final Page<OnlineShopRelPosVO> list = PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(),
                () -> relationshipRepository.listShopPosRelsByOption(onlineShopId, pos));
        return Results.success(list);
    }

    @ApiOperation(value = "重新设置'是否计算库存'字段")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @PostMapping("/online-shops/reset-is-inv-calculated")
    public ResponseEntity<?> resetIsInvCalculated(
            @PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
            @RequestParam("onlineShopCode") final String onlineShopCode,
            @RequestParam("posCode") final String posCode) {
        if (StringUtils.isEmpty(onlineShopCode) && StringUtils.isEmpty(posCode)) {
            return new ResponseEntity<>(getExceptionResponse(BasicDataConstants.ErrorCode.BASIC_DATA_ONLINE_AND_POS_CODE_IS_NULL), HttpStatus.OK);
        }

        if (StringUtils.isNotEmpty(onlineShopCode) && StringUtils.isNotEmpty(posCode)) {
            return new ResponseEntity<>(getExceptionResponse(BasicDataConstants.ErrorCode.BASIC_DATA_ONLINE_AND_POS_CODE_IS_NULL), HttpStatus.OK);
        }
        return Results.success(onlineShopRelPosService.resetIsInvCalculated(onlineShopCode, posCode,organizationId));
    }
}
