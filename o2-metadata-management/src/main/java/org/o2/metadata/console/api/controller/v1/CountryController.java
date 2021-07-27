package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.CustomPageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.console.app.service.CountryService;
import org.o2.metadata.console.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.console.infra.entity.Country;
import org.o2.metadata.console.infra.entity.Region;
import org.o2.metadata.console.infra.repository.CountryRepository;
import org.o2.metadata.console.infra.repository.RegionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;


/**
 * 国家API
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */

@RestController("countryController.v1")
@RequestMapping("/v1/{organizationId}/countries")
@Api(tags = MetadataManagementAutoConfiguration.COUNTRY)
public class CountryController extends BaseController {

    private final CountryService countryService;
    private final RegionRepository regionRepository;
    private final CountryRepository countryRepository;

    public CountryController(final CountryService countryService,
                             final RegionRepository regionRepository,
                             final CountryRepository countryRepository) {
        this.countryService = countryService;
        this.regionRepository = regionRepository;
        this.countryRepository = countryRepository;
    }

    @ApiOperation("分页查询国家")
    @GetMapping
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    @CustomPageRequest
    public ResponseEntity<?> pageListCountries(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,final Country country,
                                               @ApiIgnore @SortDefault(value = Country.FIELD_COUNTRY_ID) final PageRequest pageRequest) {
        country.setTenantId(organizationId);
        return Results.success(PageHelper.doPageAndSort(pageRequest, () -> countryRepository.select(country)));
    }

    @ApiOperation("获取所有国家")
    @GetMapping("/all")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    @CustomPageRequest
    public ResponseEntity<?> listAllCountries(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,final Country country) {
        country.setTenantId(organizationId);
        return ResponseEntity.ok(countryRepository.select(country));
    }

    @ApiOperation("获取所有有效国家")
    @GetMapping("/valid")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    @CustomPageRequest
    public ResponseEntity<?> listValidCountries(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,final Country country) {
        country.setTenantId(organizationId);
        country.setEnabledFlag(1);
        return ResponseEntity.ok(countryRepository.select(country));
    }

    @ApiOperation("根据ID查询指定国家")
    @GetMapping("/query-by-id")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<?> getCountryById(@RequestParam final Long countryId) {
        return Results.success(countryRepository.selectByPrimaryKey(countryId));
    }

    @ApiOperation("根据CODE查询指定国家")
    @GetMapping("/query-by-code")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    @CustomPageRequest
    public ResponseEntity<?> getCountryByCode(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,@RequestParam final String countryCode) {
        return Results.success(countryRepository.selectOne(Country.builder().countryCode(countryCode).tenantId(organizationId).build()));
    }

    @ApiOperation("根据regionId查询国家")
    @GetMapping("/query-by-region-id")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<?> getCountryByRegionId(@RequestParam final Long regionId) {
        final List<Region> regionList = regionRepository.select(Region.FIELD_REGION_ID, regionId);
        if (regionList.isEmpty()) {
            return Results.error();
        }
        final List<Country> countryList = countryRepository.select(Country.FIELD_COUNTRY_ID, regionList.get(0).getCountryId());
        if (countryList.isEmpty()) {
            return Results.error();
        }
        return Results.success(countryList.get(0));
    }

    @ApiOperation("新增国家定义")
    @PostMapping
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> createCountry(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,@RequestBody final Country country) {
        country.setTenantId(organizationId);
        this.validObject(country);
        return ResponseEntity.status(HttpStatus.CREATED).body(countryService.createCountry(country));
    }

    @ApiOperation("更新国家定义")
    @PutMapping
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> updateCountry(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,@RequestBody final Country country) {
        SecurityTokenHelper.validToken(country);
        country.setTenantId(organizationId);
        this.validObject(country);
        return ResponseEntity.ok(countryService.updateCountry(country));
    }

    @ApiOperation("批量禁用国家定义")
    @PatchMapping("/batch-disable")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity batchDisableCountry(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,@RequestBody final List<Country> countryList) {
        SecurityTokenHelper.validToken(countryList);
        if (!CollectionUtils.isEmpty(countryList)) {
            return ResponseEntity.ok(countryService.batchDisableCountry(organizationId,countryList));
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

