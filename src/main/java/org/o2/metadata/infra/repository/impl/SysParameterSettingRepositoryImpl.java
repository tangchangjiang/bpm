package org.o2.metadata.infra.repository.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.domain.entity.SysParameter;
import org.o2.metadata.domain.repository.SysParameterSettingRepository;
import org.o2.metadata.infra.mapper.SysParameterMapper;
import org.springframework.stereotype.Component;

/**
 * 系统参数设置 资源库实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Component
public class SysParameterSettingRepositoryImpl extends BaseRepositoryImpl<SysParameter> implements SysParameterSettingRepository {
    private final SysParameterMapper sysParameterMapper;

    public SysParameterSettingRepositoryImpl(final SysParameterMapper sysParameterMapper) {
        this.sysParameterMapper = sysParameterMapper;
    }

    @Override
    public Page<SysParameter> listSysParameterSetting(final int page, final int size, final String parameterCode, final String parameterDesc) {
        return PageHelper.doPage(page, size, () -> sysParameterMapper.listSysParameterSetting(parameterCode, parameterDesc));
    }

    @Override
    public SysParameter detail(final Long parameterSettingId) {
        return null;
    }
}
