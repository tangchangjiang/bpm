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
import org.o2.boot.metadata.app.SysParameterCacheService;
import org.o2.boot.metadata.app.bo.SysParameterBO;
import org.o2.ext.metadata.config.EnableMetadataClientConsole;
import org.o2.ext.metadata.domain.entity.SysParameterSetting;
import org.o2.ext.metadata.domain.repository.SysParameterSettingRepository;
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
@Api(tags = EnableMetadataClientConsole.SYS_PARAMETER_SETTING)
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
        final Page<SysParameterSetting> list = sysParameterSettingRepository.listSysParameterSetting(pageRequest.getPage(),
                pageRequest.getSize(), parameterCode, parameterDesc);
        return Results.success(list);
    }

    @ApiOperation(value = "系统参数设置明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{parameterSettingId}")
    public ResponseEntity<?> detail(@PathVariable final Long parameterSettingId) {
        final SysParameterSetting sysParameterSetting = sysParameterSettingRepository.selectByPrimaryKey(parameterSettingId);
        return Results.success(sysParameterSetting);
    }

    @ApiOperation(value = "根据code查询系统参数value,仅查有效数据")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/value")
    public ResponseEntity<?> getValue(@RequestParam final String parameterCode) {
        final SysParameterSetting sysParameterSetting = new SysParameterSetting();
        sysParameterSetting.setParameterCode(parameterCode);
        sysParameterSetting.setIsActive(1);
        final List<SysParameterSetting> list = sysParameterSettingRepository.select(sysParameterSetting);
        if (list.size() > 0) {
            return Results.success(list.get(0).getParameterValue());
        } else {
            return Results.success();
        }
    }

    @ApiOperation(value = "修改系统参数设置")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<?> update(@RequestBody final SysParameterSetting sysParameterSetting) {
        SecurityTokenHelper.validToken(sysParameterSetting);
        if (sysParameterSetting.getParameterSettingId() != null) {
            sysParameterSettingRepository.updateByPrimaryKeySelective(sysParameterSetting);
            sysParameterCacheService.saveSysParameter(convert(sysParameterSetting));
        }
        return Results.success(sysParameterSetting);
    }

    @ApiOperation(value = "新增系统参数设置")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<?> create(@RequestBody final SysParameterSetting sysParameterSetting) {
        //校验唯一
        sysParameterSetting.validateParameterCode(sysParameterSettingRepository);
        sysParameterSetting.setParameterSettingId(null);
        sysParameterSettingRepository.insertSelective(sysParameterSetting);
        sysParameterCacheService.saveSysParameter(convert(sysParameterSetting));

        return Results.success(sysParameterSetting);
    }

    @ApiOperation(value = "删除系统参数设置")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody final SysParameterSetting sysParameterSetting) {
        SecurityTokenHelper.validToken(sysParameterSetting);
        sysParameterSettingRepository.deleteByPrimaryKey(sysParameterSetting);
        sysParameterCacheService.deleteSysParameter(sysParameterSetting.getParameterCode());
        return Results.success();
    }

    private SysParameterBO convert(final SysParameterSetting sysParameterSetting) {
        final SysParameterBO sysParameterBo = new SysParameterBO();
        sysParameterBo.setParameterCode(sysParameterSetting.getParameterCode());
        sysParameterBo.setParameterValue(sysParameterSetting.getParameterValue());
        sysParameterBo.setIsActive(sysParameterSetting.getIsActive());
        return sysParameterBo;
    }
}
