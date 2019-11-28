package org.o2.metadata.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.app.bo.SysParameterBO;
import org.o2.metadata.app.service.SysParameterCacheService;
import org.o2.metadata.config.EnableMetadata;
import org.o2.metadata.domain.entity.SysParameter;
import org.o2.metadata.domain.repository.SysParameterSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统参数设置 管理 API
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */

@RestController("sysParameterSettingController.v1")
@RequestMapping("/v1/sys-parameter-settings")
@Api(tags = EnableMetadata.SYS_PARAMETER_SETTING)
public class SysParameterSettingController extends BaseController {

    @Autowired
    private SysParameterSettingRepository sysParameterSettingRepository;

    @Autowired
    private SysParameterCacheService sysParameterCacheService;

    @ApiOperation(value = "系统参数设置列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parameterCode", value = "参数编码", paramType = "query"),
            @ApiImplicitParam(name = "parameterDesc", value = "参数描述", paramType = "query")
    })
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<?> list(final String parameterCode, final String parameterDesc, final PageRequest pageRequest) {
        final Page<SysParameter> list = sysParameterSettingRepository.listSysParameterSetting(pageRequest.getPage(),
                pageRequest.getSize(), parameterCode, parameterDesc);
        return Results.success(list);
    }

    @ApiOperation(value = "系统参数设置明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{parameterId}")
    public ResponseEntity<?> detail(@PathVariable final Long parameterSettingId) {
        final SysParameter sysParameter = sysParameterSettingRepository.selectByPrimaryKey(parameterSettingId);
        return Results.success(sysParameter);
    }

    @ApiOperation(value = "根据code查询系统参数value,仅查有效数据")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/value")
    public ResponseEntity<?> getValue(@RequestParam final String parameterCode) {
        final SysParameter sysParameter = new SysParameter();
        sysParameter.setParameterCode(parameterCode);
        sysParameter.setActiveFlag(1);
        final List<SysParameter> list = sysParameterSettingRepository.select(sysParameter);
        if (list.size() > 0) {
            return Results.success(list.get(0).getParameterValue());
        } else {
            return Results.success();
        }
    }

    @ApiOperation(value = "修改系统参数设置")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<?> update(@RequestBody final SysParameter sysParameter) {
        SecurityTokenHelper.validToken(sysParameter);
        if (sysParameter.getParameterId() != null) {
            sysParameterSettingRepository.updateByPrimaryKeySelective(sysParameter);
            sysParameterCacheService.saveSysParameter(convert(sysParameter));
        }
        return Results.success(sysParameter);
    }

    @ApiOperation(value = "新增系统参数设置")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<?> create(@RequestBody final SysParameter sysParameter) {
        //校验唯一
        sysParameter.validateParameterCode(sysParameterSettingRepository);
        sysParameter.setParameterId(null);
        sysParameterSettingRepository.insertSelective(sysParameter);
        sysParameterCacheService.saveSysParameter(convert(sysParameter));

        return Results.success(sysParameter);
    }

    @ApiOperation(value = "删除系统参数设置")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody final SysParameter sysParameter) {
        SecurityTokenHelper.validToken(sysParameter);
        sysParameterSettingRepository.deleteByPrimaryKey(sysParameter);
        sysParameterCacheService.deleteSysParameter(sysParameter.getParameterCode());
        return Results.success();
    }

    private SysParameterBO convert(final SysParameter sysParameter) {
        final SysParameterBO sysParameterBo = new SysParameterBO();
        sysParameterBo.setParameterCode(sysParameter.getParameterCode());
        sysParameterBo.setParameterValue(sysParameter.getParameterValue());
        sysParameterBo.setIsActive(sysParameter.getActiveFlag());
        return sysParameterBo;
    }
}
