package org.o2.metadata.api.controller.v1;

import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.base.BaseConstants;
import org.o2.metadata.core.infra.service.CustomLovService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * description:前端-集值
 * </p>
 *
 * @author wei.cai@hand-china.com 2019/12/25 12:14
 */
@Api(
        tags = {"Public Lov"}
)
@RestController("publicLovController.v1")
@RequestMapping({"/v1/{organizationId}"})
public class PublicLovController {
    private final CustomLovService customLovService;

    public PublicLovController(final CustomLovService customLovService) {
        this.customLovService = customLovService;
    }

    @ApiOperation("集值 - 查询")
    @Permission(permissionPublic = true)
    @GetMapping({"/pub/lov/search-by-code"})
    public ResponseEntity<List<LovValueDTO>> searchLov(@RequestParam @ApiParam(value = "值集编码", required = true) final String lovCode,
                                                       @RequestParam(required = false, defaultValue = "zh_CN") @ApiParam(value = "语言", defaultValue = "zh_CN") final String lang,
                                                       @PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId) {
        try {
            DetailsHelper.getAnonymousDetails().setLanguage(lang);
            return customLovService.queryLovInfo(lovCode, organizationId);
        } finally {
            DetailsHelper.getAnonymousDetails().setLanguage(BaseConstants.DEFAULT_LOCALE_STR);
        }
    }

    @ApiOperation("集值 - 批量查询")
    @Permission(permissionPublic = true)
    @GetMapping({"/pub/lov/batch/search-by-code"})
    public ResponseEntity<Map<String, List<LovValueDTO>>> batchSearchLov(@RequestParam final Map<String, String> queryMap,
                                                                         @RequestParam(required = false, defaultValue = "zh_CN") @ApiParam(value = "语言", defaultValue = "zh_CN") final String lang,
                                                                         @PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId) {
        try {
            DetailsHelper.getAnonymousDetails().setLanguage(lang);
            return customLovService.batchQueryLovInfo(queryMap, organizationId);
        } finally {
            DetailsHelper.getAnonymousDetails().setLanguage(BaseConstants.DEFAULT_LOCALE_STR);
        }
    }
}