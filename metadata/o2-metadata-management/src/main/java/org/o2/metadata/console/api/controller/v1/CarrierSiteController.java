package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.console.infra.entity.Carrier;
import org.o2.metadata.console.infra.repository.CarrierRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 站点级 承运商 管理 API
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@RestController("carrierSiteController.v1")
@RequestMapping("/v1/carriers")
@Api(tags = MetadataManagementAutoConfiguration.CARRIER_SITE)
public class CarrierSiteController extends BaseController {
    private final CarrierRepository carrierRepository;

    public CarrierSiteController(CarrierRepository carrierRepository) {
        this.carrierRepository = carrierRepository;
    }

    @ApiOperation(value = "承运商列表 站点级")
    @Permission(level = ResourceLevel.SITE)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping("/page-list")
    public ResponseEntity<Page<Carrier>> list(final Carrier carrier,
                                              final PageRequest pageRequest) {
        final Page<Carrier> list = PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(),
                () -> carrierRepository.listCarrier(carrier, BaseConstants.Flag.YES));
        return Results.success(list);
    }

    @ApiOperation(value = "承运商明细 站点级")
    @Permission(level = ResourceLevel.SITE)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping("/detail")
    public ResponseEntity<Carrier> detail(@RequestParam final Long carrierId) {
        final Carrier carrier = carrierRepository.selectByPrimaryKey(carrierId);
        return Results.success(carrier);
    }
}
