package org.o2.metadata.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.o2.ext.metadata.domain.entity.SysParameterSetting;

import java.util.List;

/**
 * 系统参数设置Mapper
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface SysParameterSettingMapper extends BaseMapper<SysParameterSetting> {

    /**
     * 根据条件查询系统参数设置
     *
     * @param parameterCode 参数编码
     * @param parameterDesc 参数描述
     * @return 结果集
     */
    List<SysParameterSetting> listSysParameterSetting(@Param("parameterCode") String parameterCode, @Param("parameterDesc") String parameterDesc);

}
