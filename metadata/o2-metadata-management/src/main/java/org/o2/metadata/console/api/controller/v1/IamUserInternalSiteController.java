package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.util.Results;
import org.o2.metadata.console.api.co.IamUserCO;
import org.o2.metadata.console.api.dto.IamUserQueryInnerDTO;
import org.o2.metadata.console.app.service.IamUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户信息查询（平台层）
 *
 * @author chao.yang05@hand-china.com 2023-05-16
 */
@RestController("iamUserInternalSiteController.v1")
@RequestMapping("/v1/iam-user-internal")
public class IamUserInternalSiteController {

    private final IamUserService iamUserService;

    public IamUserInternalSiteController(IamUserService iamUserService) {
        this.iamUserService = iamUserService;
    }

    @ApiOperation(value = "查询用户信息（平台层）")
    @Permission(permissionWithin = true, level = ResourceLevel.SITE)
    @GetMapping("/info-list")
    public ResponseEntity<List<IamUserCO>> listIamUserInfos(@RequestBody IamUserQueryInnerDTO queryInner) {
        queryInner.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
        return Results.success(iamUserService.listIamUserInfo(queryInner));
    }
}
