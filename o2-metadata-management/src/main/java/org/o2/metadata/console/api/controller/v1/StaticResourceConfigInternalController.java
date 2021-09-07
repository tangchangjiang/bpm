package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.util.Results;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.console.api.co.StaticResourceConfigCO;
import org.o2.metadata.console.infra.entity.StaticResourceConfig;
import org.o2.metadata.console.infra.repository.StaticResourceConfigRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * description 静态资源配置
 *
 * @author zhilin.ren@hand-china.com 2021/09/07 15:08
 */
@RestController("staticResourceConfigInternalController.v1")
@RequestMapping("/v1/{organizationId}/static-resource-configs-internal")
public class StaticResourceConfigInternalController {

    private final StaticResourceConfigRepository staticResourceConfigRepository;

    public StaticResourceConfigInternalController(StaticResourceConfigRepository staticResourceConfigRepository) {
        this.staticResourceConfigRepository = staticResourceConfigRepository;
    }


    @ApiOperation("根据资源编码获取配置对象")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{resourceCode}")
    public ResponseEntity<StaticResourceConfigCO> getStaticResourceConfig(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                          @PathVariable(value = "resourceCode") @ApiParam(value = "参数code", required = true) String resourceCode){

        List<StaticResourceConfig> resourceConfigs = staticResourceConfigRepository.selectByCondition(Condition.builder(StaticResourceConfig.class)
                .andWhere(Sqls.custom().andEqualTo(StaticResourceConfig.FIELD_RESOURCE_CODE, resourceCode)
                        .andEqualTo(StaticResourceConfig.FIELD_TENANT_ID, organizationId))
                .build());
        if (!CollectionUtils.isEmpty(resourceConfigs)) {
            StaticResourceConfig resourceConfig = resourceConfigs.get(0);
            StaticResourceConfigCO configCO = new StaticResourceConfigCO(resourceConfig);
            return Results.success(configCO);
        }
        return Results.success();

    }



}
