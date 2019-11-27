package org.o2.metadata.infra.repository.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.ext.metadata.domain.entity.SysParameterSetting;
import org.o2.ext.metadata.domain.repository.SysParameterSettingRepository;
import org.o2.ext.metadata.infra.mapper.SysParameterSettingMapper;
import org.springframework.stereotype.Component;

/**
 * 系统参数设置 资源库实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Component
public class SysParameterSettingRepositoryImpl extends BaseRepositoryImpl<SysParameterSetting> implements SysParameterSettingRepository {
    private final SysParameterSettingMapper sysParameterSettingMapper;

    public SysParameterSettingRepositoryImpl(final SysParameterSettingMapper sysParameterSettingMapper) {
        this.sysParameterSettingMapper = sysParameterSettingMapper;
    }

    @Override
    public Page<SysParameterSetting> listSysParameterSetting(final int page, final int size, final String parameterCode, final String parameterDesc) {
        return PageHelper.doPage(page, size, () -> sysParameterSettingMapper.listSysParameterSetting(parameterCode, parameterDesc));
    }

    @Override
    public SysParameterSetting detail(final Long parameterSettingId) {
        return null;
    }
}
