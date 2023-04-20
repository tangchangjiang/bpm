package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.util.Results;
import org.o2.metadata.console.api.dto.RegionQueryDTO;
import org.o2.metadata.console.api.vo.AreaRegionVO;
import org.o2.metadata.console.api.vo.RegionVO;
import org.o2.metadata.console.app.service.RegionService;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * 地区层级定义管理 平台层
 *
 * @author chao.yang05@hand-china.com 2023-04-11
 */
@RestController("regionSiteController.v1")
@RequestMapping("/v1/regions")
@Api(tags = MetadataManagementAutoConfiguration.REGION_SITE)
public class RegionSiteController {

    private final RegionService regionService;

    public RegionSiteController(RegionService regionService) {
        this.regionService = regionService;
    }

    @ApiOperation("平台层查询国家/地区下的有效地区列表")
    @GetMapping("/valid")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @Permission(level = ResourceLevel.SITE)
    public ResponseEntity<List<RegionVO>> listValidRegions(@RequestParam(required = false, value = "countryCode") final String countryCode,
                                                           @RequestParam(required = false, value = "regionCode") final String regionCode) {
        if (countryCode == null && regionCode == null) {
            return Results.success(Collections.emptyList());
        }
        RegionQueryDTO queryDTO = new RegionQueryDTO();
        if (StringUtils.isNotEmpty(countryCode)) {
            queryDTO.setCountryCode(countryCode);
            queryDTO.setLevelNumber(1);
        }
        queryDTO.setParentRegionCode(regionCode);
        queryDTO.setEnabledFlag(1);

        return Results.success(regionService.listChildren(queryDTO, null));
    }

    @ApiOperation("查询大区下的省份")
    @GetMapping("/area")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @Permission(permissionPublic = true, level = ResourceLevel.SITE)
    public ResponseEntity<List<AreaRegionVO>> listAreaRegions(Long tenantId,
                                                              @RequestParam final String countryCode,
                                                              @RequestParam(required = false) final Integer enabledFlag) {
        if (null == tenantId) {
            tenantId = BaseConstants.DEFAULT_TENANT_ID;
        }
        // 这里不需要做兜底查询，因为H0接口中查询值集已经做了兜底
        return Results.success(regionService.listAreaRegion(countryCode, enabledFlag, tenantId));
    }
}
