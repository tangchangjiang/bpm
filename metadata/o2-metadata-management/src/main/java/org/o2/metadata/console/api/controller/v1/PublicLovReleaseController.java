package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.o2.core.response.OperateResponse;
import org.o2.metadata.console.api.vo.PublicLovVO;
import org.o2.metadata.console.app.service.O2PublicLovService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("publicLovReleaseController.v1")
@RequestMapping("/v1/{organizationId}/public/lov")
public class PublicLovReleaseController extends BaseController {

    private final O2PublicLovService publicLovService;
    public PublicLovReleaseController(O2PublicLovService publicLovService){
        this.publicLovService = publicLovService;
    }

    @ApiOperation(value = "公共值集发布")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/release")
    public ResponseEntity<OperateResponse> release(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                   @RequestParam(required = false) String lovCode,
                                                   @RequestParam(required = false) String resourceOwner,
                                                   @RequestParam(required = false) String businessTypeCode){
        PublicLovVO publicLovVO = new PublicLovVO();
        publicLovVO.setTenantId(organizationId);
        publicLovVO.setLovCode(lovCode!=null?lovCode:MetadataConstants.PublicLov.PUB_LOV_CODE);
        publicLovService.createPublicLovFile(publicLovVO, resourceOwner, businessTypeCode);
        return Results.success(OperateResponse.success());
    }
}
