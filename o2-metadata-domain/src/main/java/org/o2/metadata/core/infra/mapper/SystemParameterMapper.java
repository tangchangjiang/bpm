package org.o2.metadata.core.infra.mapper;

import org.apache.ibatis.annotations.Param;
import org.o2.metadata.core.domain.entity.SystemParameter;

import java.util.List;

import io.choerodon.mybatis.common.BaseMapper;

/**
 * 系统参数Mapper
 *
 * @author hongyun.wang01@hand-china.com 2020-02-13
 */
public interface SystemParameterMapper extends BaseMapper<SystemParameter> {

    List<SystemParameter> fuzzyQuery(@Param("systemParameter") SystemParameter systemParameter,
                                     @Param("tenantId") Long tenantId);

}
