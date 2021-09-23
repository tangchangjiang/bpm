package org.o2.metadata.console.api.controller.v1;

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
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.console.api.vo.CatalogVO;
import org.o2.metadata.console.app.service.CatalogService;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.console.infra.entity.Catalog;
import org.o2.metadata.console.infra.repository.CatalogRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 版本 管理 API
 *
 * @author jiu.yang@hand-china.com 2019-12-02 15:33:52
 */
@Api(tags = MetadataManagementAutoConfiguration.CATALOG)
@RestController("catalogController.v1")
@RequestMapping("/v1/{organizationId}/catalogs")
@Slf4j
public class CatalogController extends BaseController {

    private final CatalogRepository catalogRepository;

    private final CatalogService catalogService;

    public CatalogController(CatalogRepository catalogRepository, CatalogService catalogService) {
        this.catalogRepository = catalogRepository;
        this.catalogService = catalogService;
    }

    @ApiOperation(value = "版本列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<Catalog>> list(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, Catalog catalog, @ApiIgnore @SortDefault(value = Catalog.FIELD_CATALOG_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        catalog.setTenantId(organizationId);
        final Page<Catalog> list = PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(),
                () -> catalogRepository.listCatalog(catalog));
        return Results.success(list);
    }

    @ApiOperation(value = "版本明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{catalogId}")
    public ResponseEntity<Catalog> detail(@PathVariable Long catalogId) {
        Catalog catalog = catalogRepository.selectByPrimaryKey(catalogId);
        return Results.success(catalog);
    }

    @ApiOperation(value = "创建版本")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<Catalog> create(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody Catalog catalog) {
        catalog.setTenantId(organizationId);
        catalogRepository.insertSelective(catalog);
        return Results.success(catalog);
    }

    @ApiOperation(value = "修改版本")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<Catalog> update(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody Catalog catalog) {
        SecurityTokenHelper.validToken(catalog);
        catalog.setTenantId(organizationId);
        catalogService.update(catalog);
        return Results.success(catalog);
    }

    @ApiOperation(value = "删除版本")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<Void> remove(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody Catalog catalog) {
        catalog.setTenantId(organizationId);
        SecurityTokenHelper.validToken(catalog);
        catalogRepository.deleteByPrimaryKey(catalog);
        return Results.success();
    }

    @ApiOperation(value = "版本导出Excel")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/export")
    @ExcelExport(CatalogVO.class)
    public ResponseEntity<List<CatalogVO>> export(ExportParam exportParam, HttpServletResponse response,@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId) {
        List<CatalogVO> export = catalogService.export(exportParam,organizationId);
        log.info("data {}",export);
        return Results.success(export);
    }

}
