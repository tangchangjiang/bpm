package org.o2.metadata.console.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.o2.metadata.console.infra.entity.SystemParameter;

import java.util.List;

/**
 * 系统参数Mapper
 *
 * @author hongyun.wang01@hand-china.com 2020-02-13
 */
public interface SystemParameterMapper extends BaseMapper<SystemParameter> {

    List<SystemParameter> fuzzyQuery(@Param("systemParameter") SystemParameter systemParameter,
                                     @Param("tenantId") Long tenantId);

}
