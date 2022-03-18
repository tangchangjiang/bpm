package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.exception.CommonException;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.core.response.OperateResponse;
import org.o2.metadata.console.api.dto.AddressMappingQueryDTO;
import org.o2.metadata.console.api.dto.AddressReleaseDTO;
import org.o2.metadata.console.api.dto.CountryQueryLovDTO;
import org.o2.metadata.console.api.vo.AddressMappingVO;
import org.o2.metadata.console.api.vo.RegionTreeChildVO;
import org.o2.metadata.console.app.service.AddressMappingService;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.entity.AddressMapping;
import org.o2.metadata.console.infra.entity.Country;
import org.o2.metadata.console.infra.repository.AddressMappingRepository;
import org.o2.metadata.console.infra.repository.CountryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 地址匹配 管理 API
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@RestController("addressMappingController.v1")
@RequestMapping("/v1/{organizationId}/address-mappings")
@Api(tags = MetadataManagementAutoConfiguration.ADDRESS_MAPPING)
public class AddressMappingController extends BaseController {
    private final AddressMappingRepository addressMappingRepository;
    private final AddressMappingService addressMappingService;
    private final CountryRepository countryRepository;

    public AddressMappingController(AddressMappingRepository addressMappingRepository,
                                    AddressMappingService addressMappingService,
                                    CountryRepository countryRepository) {
        this.addressMappingRepository = addressMappingRepository;
        this.addressMappingService = addressMappingService;
        this.countryRepository = countryRepository;
    }

    @ApiOperation(value = "地址匹配列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/all")
    public ResponseEntity<List<RegionTreeChildVO>> listAllAddressMappings(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                          final AddressMappingQueryDTO addressMappingQueryDTO,
                                                                          final String countryCode) {
        addressMappingQueryDTO.setTenantId(organizationId);
        if (MetadataConstants.Constants.COUNTRY_ALL.equals(countryCode)) {
            final List<RegionTreeChildVO> results = new ArrayList<>();
            // 获得所有国家
            CountryQueryLovDTO dto= new CountryQueryLovDTO();
            dto.setTenantId(organizationId);
            final List<Country> countryList = countryRepository.listCountryLov(dto,organizationId);
            for (final Country country : countryList) {
                final RegionTreeChildVO treeChildVO = new RegionTreeChildVO();
                //负数，防止与regionId重复
                treeChildVO.setRegionCode(country.getCountryCode());
                treeChildVO.setRegionCode(country.getCountryCode());
                treeChildVO.setRegionName(country.getCountryName());
                treeChildVO.setChildren(addressMappingService.findAddressMappingGroupByCondition(addressMappingQueryDTO, country.getCountryCode()));
                if (!treeChildVO.getChildren().isEmpty()) {
                    results.add(treeChildVO);
                }
            }
            return Results.success(results);
        } else {
            return Results.success(addressMappingService.findAddressMappingGroupByCondition(addressMappingQueryDTO, countryCode));
        }

    }

    @ApiOperation(value = "地址匹配详情")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping("/detail")
    public ResponseEntity<AddressMappingVO> detail(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                 final Long addressMappingId,
                                                 final String countryCode) {
        return Results.success(addressMappingService.addressMappingDetail(addressMappingId,countryCode,organizationId));
    }

    @ApiOperation(value = "创建地址匹配")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<AddressMapping> createAddressMapping(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody AddressMapping addressMapping) {
        addressMapping.setTenantId(organizationId);
        addressMappingService.createAddressMapping(addressMapping);
        return Results.success(addressMapping);
    }

    @ApiOperation(value = "修改地址匹配")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<AddressMapping> updateAddressMapping(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                  @RequestBody final AddressMapping addressMapping) {
        addressMapping.setTenantId(organizationId);
        SecurityTokenHelper.validToken(addressMapping);
        if (!addressMapping.exist(addressMappingRepository)) {
            throw new CommonException(BaseConstants.ErrorCode.NOT_FOUND);
        }
        addressMappingRepository.updateByPrimaryKeySelective(addressMapping);
        return Results.success(addressMapping);
    }

    @ApiOperation(value = "删除地址匹配")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<Void> deleteAddressMapping(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                     @RequestBody final AddressMapping addressMapping) {
        addressMapping.setTenantId(organizationId);
        SecurityTokenHelper.validToken(addressMapping);
        addressMappingRepository.delete(addressMapping);
        return Results.success();
    }

    @ApiOperation(value = "发布地区信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/release")
    public ResponseEntity<?> release(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                     @RequestBody AddressReleaseDTO addressRelease){
        addressRelease.setTenantId(organizationId);
        addressMappingService.releaseAddressMapping(addressRelease);
        return Results.success(OperateResponse.success());
    }
}
