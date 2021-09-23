package org.o2.metadata.console.api.controller.v1;

import com.google.common.base.Preconditions;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.console.api.dto.CatalogRelVersionQueryDTO;
import org.o2.metadata.console.app.service.CatalogVersionService;
import org.o2.metadata.console.infra.entity.Catalog;
import org.o2.metadata.console.infra.entity.CatalogVersion;
import org.o2.metadata.console.infra.repository.CatalogRepository;
import org.o2.metadata.console.infra.repository.CatalogVersionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 目录版本 管理 API
 *
 * @author jiu.yang@hand-china.com 2019-12-19 16:32:58
 */
@RestController("catalogVersionController.v1")
@RequestMapping("/v1/{organizationId}/catalog-versions")
public class CatalogVersionController extends BaseController {

    private final CatalogVersionRepository catalogVersionRepository;
    private final CatalogRepository catalogRepository;
    private final CatalogVersionService catalogVersionService;

    public CatalogVersionController(CatalogVersionRepository catalogVersionRepository,
                                    CatalogRepository catalogRepository,
                                    CatalogVersionService catalogVersionService) {
        this.catalogVersionRepository = catalogVersionRepository;
        this.catalogRepository = catalogRepository;
        this.catalogVersionService = catalogVersionService;
    }

    @ApiOperation(value = "目录版本列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<CatalogVersion>> list(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, CatalogVersion catalogVersion, @ApiIgnore @SortDefault(value = CatalogVersion.FIELD_CATALOG_VERSION_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        catalogVersion.setTenantId(organizationId);
        if (null != catalogVersion.getCatalogCode()) {
            Catalog catalog = catalogRepository.selectOne(Catalog.builder().catalogCode(catalogVersion.getCatalogCode()).tenantId(organizationId).build());
            Preconditions.checkArgument(null != catalog, "illegal combination catalogCode && organizationId");
            catalogVersion.setCatalogId(catalog.getCatalogId());
        }
        Page<CatalogVersion> list = catalogVersionRepository.pageAndSort(pageRequest, catalogVersion);
        return Results.success(list);
    }

    @ApiOperation(value = "目录版本明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{catalogVersionId}")
    public ResponseEntity<CatalogVersion> detail(@PathVariable Long catalogVersionId) {
        CatalogVersion catalogVersion = catalogVersionRepository.selectByPrimaryKey(catalogVersionId);
        return Results.success(catalogVersion);
    }

    @ApiOperation(value = "创建目录版本")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<CatalogVersion> create(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,@RequestBody CatalogVersion catalogVersion) {
        catalogVersion.setTenantId(organizationId);
        catalogVersionService.insert(catalogVersion);
        return Results.success(catalogVersion);
    }

    @ApiOperation(value = "修改目录版本")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<CatalogVersion> update(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody CatalogVersion catalogVersion) {
        catalogVersion.setTenantId(organizationId);
        catalogVersionService.update(catalogVersion);
        return Results.success(catalogVersion);
    }

    @ApiOperation(value = "删除目录版本")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<Void> remove(@RequestBody CatalogVersion catalogVersion) {
        SecurityTokenHelper.validToken(catalogVersion);
        catalogVersionRepository.deleteByPrimaryKey(catalogVersion);
        return Results.success();
    }

    @ApiOperation(value = "目录&目录版本")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/catalog-version")
    public ResponseEntity<Page<CatalogVersion>> catalogRelVersion(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, CatalogRelVersionQueryDTO queryDTO,
                                                            @ApiIgnore @SortDefault(value = CatalogVersion.FIELD_CATALOG_VERSION_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
          queryDTO.setTenantId(organizationId);
        final Page<CatalogVersion> list = PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(),
                () -> catalogVersionService.catalogRelVersion(queryDTO));
        return Results.success(list);
    }

}
