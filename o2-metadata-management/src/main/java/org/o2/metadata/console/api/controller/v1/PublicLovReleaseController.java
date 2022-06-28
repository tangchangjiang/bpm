package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.o2.metadata.console.api.vo.PublicLovVO;
import org.o2.metadata.console.app.service.O2PublicLovService;
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

    @ApiOperation(value = "")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/release")
    public ResponseEntity<Void> release(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                        @RequestParam String lovCode,
                                        @RequestParam(required = false) String resourceOwner){
        PublicLovVO publicLovVO = new PublicLovVO();
        publicLovVO.setTenantId(organizationId);
        publicLovVO.setLovCode(lovCode);
        publicLovService.createPublicLovFile(publicLovVO,resourceOwner);
        return Results.success();
    }
}
