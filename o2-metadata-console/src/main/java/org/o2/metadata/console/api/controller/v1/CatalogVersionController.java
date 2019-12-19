package org.o2.metadata.console.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import org.o2.metadata.core.domain.entity.CatalogVersion;
import org.o2.metadata.core.domain.repository.CatalogVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hzero.mybatis.helper.SecurityTokenHelper;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 目录版本 管理 API
 *
 * @author jiu.yang@hand-china.com 2019-12-19 16:32:58
 */
@RestController("catalogVersionController.v1")
@RequestMapping("/v1/catalog-versions")
public class CatalogVersionController extends BaseController {

    @Autowired
    private CatalogVersionRepository catalogVersionRepository;

    @ApiOperation(value = "目录版本列表")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping
    public ResponseEntity<?> list(CatalogVersion catalogVersion, @ApiIgnore @SortDefault(value = CatalogVersion.FIELD_CATALOG_VERSION_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<CatalogVersion> list = catalogVersionRepository.pageAndSort(pageRequest, catalogVersion);
        return Results.success(list);
    }

    @ApiOperation(value = "目录版本明细")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/{catalogVersionId}")
    public ResponseEntity<?> detail(@PathVariable Long catalogVersionId) {
        CatalogVersion catalogVersion = catalogVersionRepository.selectByPrimaryKey(catalogVersionId);
        return Results.success(catalogVersion);
    }

    @ApiOperation(value = "创建目录版本")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CatalogVersion catalogVersion) {
        catalogVersionRepository.insertSelective(catalogVersion);
        return Results.success(catalogVersion);
    }

    @ApiOperation(value = "修改目录版本")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping
    public ResponseEntity<?> update(@RequestBody CatalogVersion catalogVersion) {
        SecurityTokenHelper.validToken(catalogVersion);
        catalogVersionRepository.updateByPrimaryKeySelective(catalogVersion);
        return Results.success(catalogVersion);
    }

    @ApiOperation(value = "删除目录版本")
    @Permission(level = ResourceLevel.SITE)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody CatalogVersion catalogVersion) {
        SecurityTokenHelper.validToken(catalogVersion);
        catalogVersionRepository.deleteByPrimaryKey(catalogVersion);
        return Results.success();
    }

}
