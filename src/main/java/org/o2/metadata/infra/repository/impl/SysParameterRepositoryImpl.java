package org.o2.metadata.infra.repository.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.domain.entity.SysParameter;
import org.o2.metadata.domain.repository.SysParameterRepository;
import org.o2.metadata.infra.mapper.SysParameterMapper;
import org.springframework.stereotype.Component;

/**
 * 系统参数设置 资源库实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Component
public class SysParameterRepositoryImpl extends BaseRepositoryImpl<SysParameter> implements SysParameterRepository {
    private final SysParameterMapper sysParameterMapper;

    public SysParameterRepositoryImpl(final SysParameterMapper sysParameterMapper) {
        this.sysParameterMapper = sysParameterMapper;
    }

    @Override
    public Page<SysParameter> listSysParameterSetting(final int page, final int size, final String parameterCode, final String parameterDesc, Long organizationId) {
        return PageHelper.doPage(page, size, () -> sysParameterMapper.listSysParameterSetting(parameterCode, parameterDesc,organizationId));
    }

    @Override
    public SysParameter detail(final Long parameterSettingId) {
        return null;
    }
}
