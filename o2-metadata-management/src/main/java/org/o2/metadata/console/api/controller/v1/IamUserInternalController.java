package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.util.Results;
import org.o2.metadata.console.api.co.IamUserCO;
import org.o2.metadata.console.api.dto.IamUserQueryInnerDTO;
import org.o2.metadata.console.app.service.IamUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * 用户信息内部接口
 *
 * @author yipeng.zhu@hand-china.com 2021-11-01
 **/
@RestController("iamUserInternalController.v1")
@RequestMapping("/v1/{organizationId}/iam-user-internal")
public class IamUserInternalController {
    private final IamUserService iamUserService;

    public IamUserInternalController(IamUserService iamUserService) {
        this.iamUserService = iamUserService;
    }

    @ApiOperation(value = "用户信息")
    @Permission(permissionWithin = true)
    @GetMapping("/info")
    public ResponseEntity<List<IamUserCO>> listIamUser(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody IamUserQueryInnerDTO queryInner) {
        queryInner.setTenantId(organizationId);
        return Results.success(iamUserService.listIamUser(queryInner));
    }

}
