package org.o2.metadata.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.o2.metadata.api.dto.FreightDTO;
import org.o2.metadata.api.vo.FreightInfoVO;
import org.o2.metadata.app.service.FreightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 *
 * 运费
 *
 * @author yipeng.zhu@hand-china.com 2021-07-14
 **/
@RestController("freightInternalController.v1")
@RequestMapping({"v1/{organizationId}/freight-internal"})
public class FreightInternalController {
    private final FreightService freightService;

    public FreightInternalController(FreightService freightService) {
        this.freightService = freightService;
    }

    @ApiOperation(value = "从redis查询仓库")
    @Permission(permissionPublic = true, level = ResourceLevel.ORGANIZATION)
    @PostMapping("/template")
    public ResponseEntity<FreightInfoVO> getFreightTemplate(@RequestBody FreightDTO freight) {
        return Results.success(freightService.getFreightTemplate(freight));
    }
}
