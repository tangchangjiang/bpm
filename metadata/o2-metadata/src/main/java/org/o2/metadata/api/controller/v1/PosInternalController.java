package org.o2.metadata.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.util.Results;
import org.o2.metadata.api.co.PosStoreInfoCO;
import org.o2.metadata.api.dto.StoreQueryDTO;
import org.o2.metadata.app.service.PosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 服务店
 *
 * @author chao.yang05@hand-china.com 2022/4/14
 */
@RestController("posInternalController.v1")
@RequestMapping({"v1/{organizationId}/pos-internal"})
public class PosInternalController {

    private final PosService posService;

    public PosInternalController(PosService posService) {
        this.posService = posService;
    }

    /**
     * 获取自提点信息
     *
     * @param posCode 服务点编码
     * @param organizationId 租户Id
     * @return 自提点信息
     */
    @ApiOperation(value = "获取自提点信息")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @GetMapping("/pickup-info")
    public ResponseEntity<PosStoreInfoCO> getPosPickUpInfo(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                            @RequestParam(value = "posCode") String posCode) {
        PosStoreInfoCO posStoreInfoCO = posService.getPosPickUpInfo(posCode, organizationId);
        return Results.success(posStoreInfoCO);
    }

    /**
     * 条件批量查询门店信息
     *
     * @param storeQueryDTO 查询条件
     * @param organizationId 租户Id
     * @return 门店信息
     */
    @ApiOperation(value = "批量查询门店信息")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @GetMapping("/store-list")
    public ResponseEntity<List<PosStoreInfoCO>> getStoreInfoList(@RequestBody StoreQueryDTO storeQueryDTO,
                                                                 @PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId) {
        List<PosStoreInfoCO> storeInfoList = posService.getStoreInfoList(storeQueryDTO, organizationId);
        return Results.success(storeInfoList);
    }

}
