package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.o2.metadata.console.api.dto.CountryQueryLovDTO;
import org.o2.metadata.console.api.vo.CountryVO;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.console.infra.convertor.CountryConverter;
import org.o2.metadata.console.infra.repository.CountryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 国家API 站点级
 *
 * @author chao.yang05@hand-china.com 2023-04-19
 */
@RestController("countrySiteController.v1")
@RequestMapping("/v1/countries")
@Api(tags = MetadataManagementAutoConfiguration.COUNTRY_SITE)
public class CountrySiteController extends BaseController {

    private final CountryRepository countryRepository;

    public CountrySiteController(final CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @ApiOperation("根据CODE查询指定国家(站点级)")
    @GetMapping("/query-by-code")
    @Permission(level = ResourceLevel.SITE, permissionLogin = true)
    public ResponseEntity<CountryVO> getCountryByCode(@RequestParam Long tenantId, @RequestParam final String countryCode) {
        CountryQueryLovDTO queryLovDTO = new CountryQueryLovDTO();
        queryLovDTO.setTenantId(tenantId);
        queryLovDTO.setCountryCode(countryCode);
        return Results.success(CountryConverter.poToVoObject(countryRepository.listCountryLov(queryLovDTO, tenantId).get(0)));
    }
}
