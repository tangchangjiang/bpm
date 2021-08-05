package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.o2.metadata.console.api.dto.CountryDTO;
import org.o2.metadata.console.api.dto.CountryQueryLovDTO;
import org.o2.metadata.console.api.vo.CountryVO;
import org.o2.metadata.console.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.console.infra.convertor.CountryConverter;
import org.o2.metadata.console.infra.repository.CountryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final CountryRepository countryRepository;

    public CountryController(final CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }


    @ApiOperation("获取所有有效国家")
    @GetMapping("/valid")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<List<CountryVO>> listValidCountries(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, final CountryDTO country) {
        country.setTenantId(organizationId);
        country.setEnabledFlag(1);
        CountryQueryLovDTO queryLovDTO = new CountryQueryLovDTO();
        queryLovDTO.setTenantId(organizationId);
        return ResponseEntity.ok(CountryConverter.poToVoListObjects(countryRepository.listCountryLov(queryLovDTO,organizationId)));
    }

    @ApiOperation("根据CODE查询指定国家")
    @GetMapping("/query-by-code")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<CountryVO> getCountryByCode(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,@RequestParam final String countryCode) {
        CountryQueryLovDTO queryLovDTO = new CountryQueryLovDTO();
        queryLovDTO.setTenantId(organizationId);
        queryLovDTO.setCountryCode(countryCode);
        return Results.success(CountryConverter.poToVoObject(countryRepository.listCountryLov(queryLovDTO,organizationId).get(0)));
    }

}

