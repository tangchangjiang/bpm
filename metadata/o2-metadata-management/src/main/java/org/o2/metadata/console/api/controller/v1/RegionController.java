package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.o2.core.response.OperateResponse;
import org.o2.metadata.console.api.dto.RegionQueryDTO;
import org.o2.metadata.console.api.vo.AreaRegionVO;
import org.o2.metadata.console.api.vo.RegionCacheVO;
import org.o2.metadata.console.api.vo.RegionVO;
import org.o2.metadata.console.app.service.O2SiteRegionFileService;
import org.o2.metadata.console.app.service.RegionService;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.console.infra.constant.O2LovConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 地区层级定义管理
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */

@RestController("regionController.v1")
@RequestMapping("/v1/{organizationId}/regions")
@Api(tags = MetadataManagementAutoConfiguration.REGION)
public class RegionController extends BaseController {

    private final RegionService regionService;
    private final O2SiteRegionFileService siteRegionFileService;

    public RegionController(RegionService regionService,
                            O2SiteRegionFileService siteRegionFileService) {
        this.regionService = regionService;
        this.siteRegionFileService = siteRegionFileService;
    }

    @ApiOperation("查询国家下地区定义，使用树状结构返回")
    @GetMapping
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<RegionVO>> treeRegionWithParent(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                               @RequestParam(value = "countryCode") final String countryCode,
                                                               @RequestParam(required = false) final String condition,
                                                               @RequestParam(required = false) final Integer enabledFlag) {
        return Results.success(regionService.treeRegionWithParent(countryCode, condition, enabledFlag, organizationId));
    }

    @ApiOperation("查询国家/地区下的有效地区列表")
    @GetMapping("/valid")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<RegionVO>> listValidRegions(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                           @RequestParam(required = false, value = "countryCode") final String countryCode,
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

        return Results.success(regionService.listChildren(queryDTO, organizationId));
    }

    @ApiOperation("查询大区下的省份")
    @GetMapping("/area")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @Permission(permissionPublic = true, level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<AreaRegionVO>> listAreaRegions(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                              @RequestParam final String countryCode,
                                                              @RequestParam(required = false) final Integer enabledFlag) {
        return Results.success(regionService.listAreaRegion(countryCode, enabledFlag, organizationId));
    }

    @ApiOperation("地区静态资源发布")
    @GetMapping("/release")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<OperateResponse> release(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                   @RequestParam(required = false) String countryCode,
                                                   @RequestParam(required = false) String resourceOwner,
                                                   @RequestParam(required = false) String businessTypeCode) {
        RegionCacheVO regionCacheVO = new RegionCacheVO();
        regionCacheVO.setTenantId(organizationId);
        regionCacheVO.setCountryCode(countryCode != null ? countryCode : O2LovConstants.RegionLov.DEFAULT_COUNTRY_CODE);
        siteRegionFileService.createRegionStaticFile(regionCacheVO, resourceOwner, businessTypeCode);
        return Results.success(OperateResponse.success());
    }

    @ApiOperation("根据地区级别分别获取省市区数据(前后无关联关系)")
    @GetMapping("/query-by-level")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<RegionVO>> queryAreaByLevel(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                           RegionQueryDTO queryDTO, @ApiIgnore final PageRequest pageRequest) {

        List<RegionVO> resultList = regionService.listChildren(queryDTO, organizationId);
        if (StringUtils.isNotBlank(queryDTO.getRegionName())) {
            List<RegionVO> filterList = resultList.stream()
                    .filter(regionVO -> regionVO.getRegionName().contains(queryDTO.getRegionName())).collect(Collectors.toList());
            return Results.success(getPage(filterList, pageRequest));
        } else {
            return Results.success(getPage(resultList, pageRequest));
        }
    }

    protected Page<RegionVO> getPage(List<RegionVO> regionVOList, PageRequest pageRequest) {
        if (pageRequest.getPage() >= 0 && pageRequest.getSize() > 0) {
            PageInfo page = new PageInfo(pageRequest.getPage(), pageRequest.getSize(), true);

            // 数据总数
            int total = regionVOList.size();
            // 开始位置(包含)
            int startIndex = page.getPage() * page.getSize();
            // 结束位置(不包含)
            int endIndex = Math.min(startIndex + page.getSize(), total);
            // 结果数据
            List<RegionVO> resultList = Collections.emptyList();
            if (startIndex < endIndex) {
                // 进行分页
                resultList = regionVOList.subList(startIndex, endIndex);
            }
            return new Page<>(resultList, page, total);
        }
        // 数据总数
        int total = regionVOList.size();
        return new Page<>(regionVOList, new PageInfo(0, total == 0 ? 1 : total), total);
    }

}

