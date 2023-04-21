package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.CustomPageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.o2.annotation.annotation.ProcessAnnotationValue;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.console.infra.entity.Warehouse;
import org.o2.metadata.console.infra.repository.WarehouseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 仓库 站点级
 *
 * @author chao.yang05@hand-china.com 2023-04-11
 */
@RestController("warehouseSiteController.v1")
@RequestMapping("/v1/warehouse")
@Api(tags = MetadataManagementAutoConfiguration.WAREHOUSE_SITE)
public class WarehouseSiteController extends BaseController {

    private final WarehouseRepository warehouseRepository;

    public WarehouseSiteController(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @ApiOperation(value = "仓库信息列表 站点级")
    @Permission(level = ResourceLevel.SITE)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @ProcessAnnotationValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping
    @CustomPageRequest
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = Warehouse.class, responseContainer = "List"))
    public ResponseEntity<Page<Warehouse>> list(final Warehouse warehouse,
                                                @ApiIgnore final PageRequest pageRequest) {
        warehouse.setSiteFlag(BaseConstants.Flag.YES);
        final Page<Warehouse> posList = PageHelper.doPage(pageRequest,
                () -> warehouseRepository.listWarehouseByCondition(warehouse));
        return Results.success(posList);
    }
}
