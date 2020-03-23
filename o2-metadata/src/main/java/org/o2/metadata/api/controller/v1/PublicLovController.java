package org.o2.metadata.api.controller.v1;

import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.*;
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
@RequestMapping({"/v1/{organizationId}/lov"})
public class PublicLovController {

    private CustomLovService customLovService;

    public PublicLovController(CustomLovService customLovService){
        this.customLovService = customLovService;
    }

    @ApiOperation("集值 - 查询")
    @Permission(permissionPublic = true)
    @GetMapping({"/search-by-code"})
    public ResponseEntity<List<LovValueDTO>> searchLov(@RequestParam @ApiParam(value = "值集编码", required = true) String lovCode,
                                                           @RequestParam(required = false, defaultValue = "zh_CN") @ApiParam(value = "语言", defaultValue = "zh_CN")final String lang,
                                                           @PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId) {
        try {
            DetailsHelper.getAnonymousDetails().setLanguage(lang);
            return customLovService.queryLovInfo(lovCode, organizationId);
        } finally {
            DetailsHelper.getAnonymousDetails().setLanguage(BaseConstants.DEFAULT_LOCALE_STR);
        }
    }

    @ApiOperation("集值 - 批量查询")
    @Permission(permissionPublic = true)
    @GetMapping({"/batch/search-by-code"})
    public ResponseEntity<Map<String, List<LovValueDTO>>> batchSearchLov(@RequestParam Map<String, String> queryMap,
                                                                             @RequestParam(required = false, defaultValue = "zh_CN") @ApiParam(value = "语言", defaultValue = "zh_CN")final String lang,
                                                                             @PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId) {
        try {
            DetailsHelper.getAnonymousDetails().setLanguage(lang);
            return customLovService.batchQueryLovInfo(queryMap, organizationId);
        } finally {
            DetailsHelper.getAnonymousDetails().setLanguage(BaseConstants.DEFAULT_LOCALE_STR);
        }
    }
}