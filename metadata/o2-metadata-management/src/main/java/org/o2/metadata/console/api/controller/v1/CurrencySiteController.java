package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.o2.metadata.console.api.dto.CurrencyDTO;
import org.o2.metadata.console.api.vo.CurrencyVO;
import org.o2.metadata.console.app.service.CurrencyService;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = MetadataManagementAutoConfiguration.CURRENCY_SITE)
@Controller("currencySiteController.v1")
@RequestMapping("/v1/currency")
public class CurrencySiteController extends BaseController {
    private final CurrencyService currencyService;

    public CurrencySiteController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @ApiOperation("查询币种信息")
    @GetMapping("/page")
    @Permission(level = ResourceLevel.SITE, permissionLogin = true)
    public ResponseEntity<Page<CurrencyVO>> page(CurrencyDTO currencyDTO,
                                                 PageRequest pageRequest) {
        currencyDTO.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
        return Results.success(currencyService.page(currencyDTO, pageRequest));
    }
}
