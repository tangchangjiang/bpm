package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.*;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.context.metadata.api.ISysParameterContext;
import org.o2.context.metadata.vo.SysParameterVO;
import org.o2.metadata.console.config.EnableMetadataConsole;
import org.o2.metadata.core.domain.entity.SysParameter;
import org.o2.metadata.core.domain.repository.SysParameterRepository;
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
@RequestMapping("/v1/{organizationId}/sys-parameter-settings")
@Api(tags = EnableMetadataConsole.SYS_PARAMETER_SETTING)
public class SysParameterController extends BaseController {

    @Autowired
    private SysParameterRepository sysParameterRepository;

    @Autowired
    private ISysParameterContext sysParameterContext;

    @ApiOperation(value = "系统参数设置列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parameterCode", value = "参数编码", paramType = "query"),
            @ApiImplicitParam(name = "parameterDesc", value = "参数描述", paramType = "query")
    })
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<?> list(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestParam(value = "parameterCode",required = false) String parameterCode, @RequestParam(value = "parameterDesc" ,required = false) String parameterDesc, final PageRequest pageRequest) {
        final Page<SysParameter> list = sysParameterRepository.listSysParameterSetting(pageRequest.getPage(),
                pageRequest.getSize(), parameterCode, parameterDesc, organizationId);
        return Results.success(list);
    }

    @ApiOperation(value = "系统参数设置明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{parameterId}")
    public ResponseEntity<?> detail(@PathVariable @ApiParam(value = "租户ID", required = true) Long parameterId) {
        final SysParameter sysParameter = sysParameterRepository.selectByPrimaryKey(parameterId);
        return Results.success(sysParameter);
    }

    @ApiOperation(value = "根据code查询系统参数value,仅查有效数据")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/value")
    public ResponseEntity<?> getValue(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId ,@RequestParam(value = "parameterCode") final String parameterCode) {
        final SysParameter sysParameter = new SysParameter();
        sysParameter.setParameterCode(parameterCode);
        sysParameter.setActiveFlag(1);
        sysParameter.setTenantId(organizationId);
        final List<SysParameter> list = sysParameterRepository.select(sysParameter);
        if (list.size() > 0) {
            return Results.success(list.get(0).getParameterValue());
        } else {
            return Results.success();
        }
    }

    @ApiOperation(value = "修改系统参数设置")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<?> update(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,@RequestBody final SysParameter sysParameter) {
        SecurityTokenHelper.validToken(sysParameter);
        sysParameter.setTenantId(organizationId);
        if (sysParameter.getParameterId() != null) {
            sysParameterRepository.updateByPrimaryKeySelective(sysParameter);
            sysParameterContext.saveSysParameter(convert(sysParameter));
        }
        return Results.success(sysParameter);
    }

    @ApiOperation(value = "新增系统参数设置")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<?> create(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,@RequestBody final SysParameter sysParameter) {
        //校验唯一
        sysParameter.setTenantId(organizationId);
        sysParameter.validateParameterCode(sysParameterRepository);
        sysParameter.setParameterId(null);
        sysParameterRepository.insertSelective(sysParameter);
        sysParameterContext.saveSysParameter(convert(sysParameter));

        return Results.success(sysParameter);
    }

    @ApiOperation(value = "删除系统参数设置")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> delete(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,@RequestBody final SysParameter sysParameter) {
        SecurityTokenHelper.validToken(sysParameter);
        sysParameter.setTenantId(organizationId);
        sysParameterRepository.deleteByPrimaryKey(sysParameter);
        sysParameterContext.deleteSysParameter(sysParameter.getParameterCode(),sysParameter.getTenantId());
        return Results.success();
    }

    private SysParameterVO convert(final SysParameter sysParameter) {
        final SysParameterVO sysParameterVO = new SysParameterVO();
        sysParameterVO.setParameterCode(sysParameter.getParameterCode());
        sysParameterVO.setParameterValue(sysParameter.getParameterValue());
        sysParameterVO.setActiveFlag(sysParameter.getActiveFlag());
        sysParameterVO.setTenantId(sysParameter.getTenantId());
        return sysParameterVO;
    }
}
