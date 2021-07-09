package org.o2.metadata.console.api.controller.v1;

import com.google.common.base.Preconditions;
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
import org.o2.metadata.console.api.vo.RegionTreeChildVO;
import org.o2.metadata.console.app.service.AddressMappingService;
import org.o2.metadata.console.app.service.RegionService;
import org.o2.metadata.console.config.EnableMetadataConsole;
import org.o2.metadata.console.infra.entity.AddressMapping;
import org.o2.metadata.console.infra.entity.Catalog;
import org.o2.metadata.console.infra.entity.Country;
import org.o2.metadata.console.infra.entity.Region;
import org.o2.metadata.console.domain.repository.AddressMappingRepository;
import org.o2.metadata.console.domain.repository.CatalogRepository;
import org.o2.metadata.console.domain.repository.CountryRepository;
import org.o2.metadata.console.domain.repository.RegionRepository;
import org.o2.metadata.console.infra.constant.BasicDataConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
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
@Api(tags = EnableMetadataConsole.ADDRESS_MAPPING)
public class AddressMappingController extends BaseController {
    @Autowired
    private AddressMappingRepository addressMappingRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private AddressMappingService addressMappingService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CatalogRepository catalogRepository;

    @ApiOperation(value = "地址匹配列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/all")
    public ResponseEntity<?> listAllAddressMappings(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, final AddressMapping condition, final String countryCode) {
        condition.setTenantId(organizationId);
        if (BasicDataConstants.Constants.COUNTRY_ALL.equals(countryCode)) {
            final List<RegionTreeChildVO> results = new ArrayList<>();
            // 获得所有国家
            final List<Country> countryList = countryRepository.select(Country.builder().enabledFlag(1).tenantId(condition.getTenantId()).build());
            for (final Country country : countryList) {
                final RegionTreeChildVO treeChildVO = new RegionTreeChildVO();
                //负数，防止与regionId重复
                treeChildVO.setRegionId(0 - country.getCountryId());
                treeChildVO.setRegionCode(country.getCountryCode());
                treeChildVO.setRegionName(country.getCountryName());
                treeChildVO.setChildren(addressMappingService.findAddressMappingGroupByCondition(condition, country.getCountryCode()));
                if (treeChildVO.getChildren().size() > 0) {
                    results.add(treeChildVO);
                }
            }
            return Results.success(results);
        } else {
            return Results.success(addressMappingService.findAddressMappingGroupByCondition(condition, countryCode));
        }

    }

    @ApiOperation(value = "地址匹配详情")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping("/detail")
    public ResponseEntity<?> detail(final Long addressMappingId, final String countryCode) {
        final AddressMapping addressMapping = addressMappingRepository.selectByPrimaryKey(addressMappingId);
        final Region region = regionRepository.selectByPrimaryKey(addressMapping.getRegionId());
        final Catalog catalog = catalogRepository.selectOne(Catalog.builder().catalogCode(addressMapping.getCatalogCode()).tenantId(addressMapping.getTenantId()).build());
        addressMapping.setCatalogCode(catalog.getCatalogCode());
        addressMapping.setCatalogName(catalog.getCatalogName());
        if (region != null) {
            addressMapping.setRegionName(region.getRegionName());
            //通过路径path，获取
            if (BasicDataConstants.Constants.COUNTRY_ALL.equals(countryCode)) {
                final Country country = countryRepository.selectByPrimaryKey(region.getCountryId());
                addressMapping.getRegionPathIds().add(country == null ? -100L : country.getCountryId());
                addressMapping.getRegionPathCodes().add(country == null ? "NULL" : country.getCountryCode());
                addressMapping.getRegionPathNames().add(country == null ? "NULL" : country.getCountryName());
            }
            final String[] regionPaths = region.getLevelPath().split(BasicDataConstants.Constants.ADDRESS_SPLIT_REGEX);
            for (final String code : regionPaths) {
                final Region region1 = regionService.getRegionByCode(code);
                addressMapping.getRegionPathIds().add(region1 == null ? -100L : region1.getRegionId());
                addressMapping.getRegionPathCodes().add(region1 == null ? "NULL" : region1.getRegionCode());
                addressMapping.getRegionPathNames().add(region1 == null ? "NULL" : region1.getRegionName());
            }
        }
        return Results.success(addressMapping);
    }

    @ApiOperation(value = "创建地址匹配")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<?> createAddressMapping(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody AddressMapping addressMapping) {
        addressMapping.setTenantId(organizationId);
        if (addressMapping.exist(addressMappingRepository)) {
            return new ResponseEntity<>(getExceptionResponse(BaseConstants.ErrorCode.DATA_EXISTS), HttpStatus.OK);
        }
        try {
            Preconditions.checkArgument(null != addressMapping.getCatalogCode(), BasicDataConstants.ErrorCode.BASIC_DATA_CATALOG_CODE_IS_NULL);
            Preconditions.checkArgument(null != addressMapping.getTenantId(), BasicDataConstants.ErrorCode.BASIC_DATA_TENANT_ID_IS_NULL);
            Catalog catalog = catalogRepository.selectOne(Catalog.builder().catalogCode(addressMapping.getCatalogCode()).tenantId(addressMapping.getTenantId()).build());
            Preconditions.checkArgument(null != catalog, "unrecognized catalogCode:" + addressMapping.getCatalogCode() + "or tenantId:" + addressMapping.getTenantId());
            addressMapping.setCatalogId(catalog.getCatalogId());
            addressMappingRepository.insertSelective(addressMapping);
            return Results.success(addressMapping);
        } catch (final DuplicateKeyException e) {
            throw new CommonException(BasicDataConstants.ErrorCode.BASIC_DATA_DUPLICATE_CODE, e, "AddressMapping(" + addressMapping.getAddressMappingId() + ")");
        }
    }

    @ApiOperation(value = "修改地址匹配")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<?> updateAddressMapping(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody final AddressMapping addressMapping) {
        addressMapping.setTenantId(organizationId);
        SecurityTokenHelper.validToken(addressMapping);
        if (!addressMapping.exist(addressMappingRepository)) {
            return new ResponseEntity<>(getExceptionResponse(BaseConstants.ErrorCode.NOT_FOUND), HttpStatus.OK);
        }
        addressMappingRepository.updateByPrimaryKeySelective(addressMapping);
        return Results.success(addressMapping);
    }

    @ApiOperation(value = "删除地址匹配")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> deleteAddressMapping(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody final AddressMapping addressMapping) {
        addressMapping.setTenantId(organizationId);
        SecurityTokenHelper.validToken(addressMapping);
        addressMappingRepository.delete(addressMapping);
        return Results.success();
    }
}
