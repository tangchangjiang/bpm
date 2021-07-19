package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;

import org.o2.metadata.console.api.dto.FreightDTO;
import org.o2.metadata.console.api.vo.FreightInfoVO;
import org.o2.metadata.console.app.service.FreightTemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 * 运费
 *
 * @author yipeng.zhu@hand-china.com 2021-07-14
 **/
@RestController("freightInternalController.v1")
@RequestMapping({"v1/{organizationId}/freight-internal"})
public class FreightInternalController {
    private final FreightTemplateService freightService;

    public FreightInternalController(FreightTemplateService freightService) {
        this.freightService = freightService;
    }

    @ApiOperation(value = "查询模版信息")
    @Permission(permissionPublic = true, level = ResourceLevel.ORGANIZATION)
    @PostMapping("/template")
    public ResponseEntity<FreightInfoVO> getFreightTemplate(@RequestBody FreightDTO freight) {
        return Results.success(freightService.getFreightTemplate(freight));
    }
}
