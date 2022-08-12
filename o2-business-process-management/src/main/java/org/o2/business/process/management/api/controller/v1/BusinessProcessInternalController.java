package org.o2.business.process.management.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.util.Results;
import org.o2.business.process.management.app.service.BusinessProcessRedisService;
import org.o2.business.process.management.domain.BusinessProcessContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/8/10 15:26
 */
@RestController("BusinessProcessInternalController.v1")
@RequestMapping("v1/{organizationId}/internal/business-process")
public class BusinessProcessInternalController {

    private final BusinessProcessRedisService businessProcessRedisService;

    public BusinessProcessInternalController(BusinessProcessRedisService businessProcessRedisService) {
        this.businessProcessRedisService = businessProcessRedisService;
    }

    @ApiOperation(value = "获取流程信息")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{processCode}")
    public ResponseEntity<BusinessProcessContext> getBusinessProcessConfig(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                    @PathVariable(value = "processCode") @ApiParam(value = "业务流程编码", required = true) String processCode) {
        return Results.success(businessProcessRedisService.getBusinessProcessConfig(processCode, organizationId));
    }
}
