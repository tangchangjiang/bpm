package org.o2.metadata.core.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.o2.metadata.core.domain.entity.SysParameter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统参数设置Mapper
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface SysParameterMapper extends BaseMapper<SysParameter> {

    /**
     * 根据条件查询系统参数设置
     *
     * @param parameterCode 参数编码
     * @param parameterDesc 参数描述
     * @param tenantId 租户ID
     * @return 结果集
     */
    List<SysParameter> listSysParameterSetting(@Param("parameterCode") String parameterCode, @Param("parameterDesc") String parameterDesc, @Param("tenantId") Long tenantId);

}
