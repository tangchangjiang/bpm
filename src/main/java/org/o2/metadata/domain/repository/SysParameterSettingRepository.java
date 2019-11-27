package org.o2.metadata.domain.repository;

import io.choerodon.core.domain.Page;
import org.hzero.mybatis.base.BaseRepository;
import org.o2.ext.metadata.domain.entity.SysParameterSetting;

/**
 * 系统参数设置资源库
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface SysParameterSettingRepository extends BaseRepository<SysParameterSetting> {

    /**
     * 根据条件查询系统参数设置
     *
     * @param page          页码
     * @param size          页大小
     * @param parameterCode 参数编码
     * @param parameterDesc 参数描述
     * @return 结果集
     */
    Page<SysParameterSetting> listSysParameterSetting(int page, int size, String parameterCode, String parameterDesc);

    /**
     * 详细信息
     *
     * @param parameterSettingId
     * @return
     */
    SysParameterSetting detail(Long parameterSettingId);
}
