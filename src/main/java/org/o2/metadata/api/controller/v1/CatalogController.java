package org.o2.metadata.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
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
import org.o2.metadata.config.MetadataSwagger;
import org.o2.metadata.domain.entity.Catalog;
import org.o2.metadata.domain.repository.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 版本 管理 API
 *
 * @author jiu.yang@hand-china.com 2019-12-02 15:33:52
 */
@Api(tags = MetadataSwagger.CATALOG)
@RestController("catalogController.v1")
@RequestMapping("/v1/{organizationId}/catalogs")
public class CatalogController extends BaseController {

    @Autowired
    private CatalogRepository catalogRepository;

    @ApiOperation(value = "版本列表")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping
    public ResponseEntity<?> list(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, Catalog catalog, @ApiIgnore @SortDefault(value = Catalog.FIELD_CATALOG_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        catalog.setTenantId(organizationId);
        Page<Catalog> list = catalogRepository.pageAndSort(pageRequest, catalog);
        return Results.success(list);
    }

    @ApiOperation(value = "版本明细")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/{catalogId}")
    public ResponseEntity<?> detail(@PathVariable Long catalogId) {
        Catalog catalog = catalogRepository.selectByPrimaryKey(catalogId);
        return Results.success(catalog);
    }

    @ApiOperation(value = "创建版本")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping
    public ResponseEntity<?> create(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody Catalog catalog) {
        catalog.setTenantId(organizationId);
        catalogRepository.insertSelective(catalog);
        return Results.success(catalog);
    }

    @ApiOperation(value = "修改版本")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping
    public ResponseEntity<?> update(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody Catalog catalog) {
        SecurityTokenHelper.validToken(catalog);
        catalog.setTenantId(organizationId);
        catalogRepository.updateByPrimaryKeySelective(catalog);
        return Results.success(catalog);
    }

    @ApiOperation(value = "删除版本")
    @Permission(level = ResourceLevel.SITE)
    @DeleteMapping
    public ResponseEntity<?> remove(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody Catalog catalog) {
        catalog.setTenantId(organizationId);
        SecurityTokenHelper.validToken(catalog);
        catalogRepository.deleteByPrimaryKey(catalog);
        return Results.success();
    }


}
