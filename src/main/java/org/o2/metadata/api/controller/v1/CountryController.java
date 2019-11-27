package org.o2.metadata.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.CustomPageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.ext.metadata.app.service.CountryService;
import org.o2.ext.metadata.config.EnableMetadataClientConsole;
import org.o2.ext.metadata.domain.entity.Country;
import org.o2.ext.metadata.domain.entity.Region;
import org.o2.ext.metadata.domain.repository.CountryRepository;
import org.o2.ext.metadata.domain.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/v1/countries")
@Api(tags = EnableMetadataClientConsole.COUNTRY)
public class CountryController extends BaseController {

    private final CountryService countryService;
    private final RegionRepository regionRepository;
    private final CountryRepository countryRepository;

    @Autowired
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
    public ResponseEntity<?> pageListCountries(final Country country,
                                               @ApiIgnore @SortDefault(value = Country.FIELD_COUNTRY_ID) final PageRequest pageRequest) {
        return Results.success(PageHelper.doPageAndSort(pageRequest, () -> countryRepository.select(country)));
    }

    @ApiOperation("获取所有国家")
    @GetMapping("/all")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    @CustomPageRequest
    public ResponseEntity<?> listAllCountries(final Country country) {
        return ResponseEntity.ok(countryRepository.select(country));
    }

    @ApiOperation("获取所有有效国家")
    @GetMapping("/valid")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    @CustomPageRequest
    public ResponseEntity<?> listValidCountries(final Country country) {
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
    public ResponseEntity<?> getCountryByCode(@RequestParam final String countryCode) {
        final Country country = new Country();
        country.setCountryCode(countryCode);
        return Results.success(countryRepository.selectOne(country));
    }

    @ApiOperation("根据regionId查询国家")
    @GetMapping("/query-by-region-id")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<?> getCountryByRegionId(@RequestParam final Long regionId) {
        final List<Region> regionList = regionRepository.select(Region.FIELD_REGION_ID, regionId);
        if (regionList.size() == 0) {
            return Results.error();
        }
        final List<Country> countryList = countryRepository.select(Country.FIELD_COUNTRY_ID, regionList.get(0).getCountryId());
        if (countryList.size() == 0) {
            return Results.error();
        }
        return Results.success(countryList.get(0));
    }

    @ApiOperation("新增国家定义")
    @PostMapping
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> createCountry(@RequestBody final Country country) {
        this.validObject(country);
        return ResponseEntity.status(HttpStatus.CREATED).body(countryService.createCountry(country));
    }

    @ApiOperation("更新国家定义")
    @PutMapping
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> updateCountry(@RequestBody final Country country) {
        SecurityTokenHelper.validToken(country);
        this.validObject(country);
        return ResponseEntity.ok(countryService.updateCountry(country));
    }

    @ApiOperation("批量禁用国家定义")
    @PatchMapping("/batch-disable")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity batchDisableCountry(@RequestBody final List<Country> countryList) {
        SecurityTokenHelper.validToken(countryList);
        if (!CollectionUtils.isEmpty(countryList)) {
            return ResponseEntity.ok(countryService.batchDisableCountry(countryList));
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

