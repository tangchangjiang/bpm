package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.base.BaseConstants;
import org.o2.core.helper.UserHelper;
import org.o2.metadata.console.infra.lovadapter.PublicLovQueryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * description:前端-集值查询
 * </p>
 *
 * @author wei.cai@hand-china.com 2019/12/25 12:14
 */
@Api(
        tags = {"Public Lov"}
)
@RestController("publicLovController.v1")
@RequestMapping({"/v1/pub/lov"})
public class PublicLovQueryController {
    private final PublicLovQueryRepository publicLovQueryRepository;

    public PublicLovQueryController(PublicLovQueryRepository publicLovQueryRepository) {
        this.publicLovQueryRepository = publicLovQueryRepository;
    }


    @ApiOperation("集值 - 查询")
    @Permission(permissionPublic = true)
    @GetMapping({"/search-by-code"})
    public ResponseEntity<List<LovValueDTO>> searchLov(@RequestParam @ApiParam(value = "值集编码", required = true) final String lovCode,
                                                       @RequestParam(required = false, defaultValue = "zh_CN") @ApiParam(value = "语言", defaultValue = "zh_CN") final String lang) {
        try {
            DetailsHelper.getAnonymousDetails().setLanguage(lang);
            return publicLovQueryRepository.queryLovInfo(lovCode, UserHelper.getTenantId());
        } finally {
            DetailsHelper.getAnonymousDetails().setLanguage(BaseConstants.DEFAULT_LOCALE_STR);
        }
    }

    @ApiOperation("集值 - 批量查询")
    @Permission(permissionPublic = true)
    @GetMapping({"/batch/search-by-code"})
    public ResponseEntity<Map<String, List<LovValueDTO>>> batchSearchLov(@RequestParam final Map<String, String> queryMap,
                                                                         @RequestParam(required = false, defaultValue = "zh_CN") @ApiParam(value = "语言", defaultValue = "zh_CN") final String lang) {
        try {
            DetailsHelper.getAnonymousDetails().setLanguage(lang);
            return publicLovQueryRepository.batchQueryLovInfo(queryMap, UserHelper.getTenantId());
        } finally {
            DetailsHelper.getAnonymousDetails().setLanguage(BaseConstants.DEFAULT_LOCALE_STR);
        }
    }
}
