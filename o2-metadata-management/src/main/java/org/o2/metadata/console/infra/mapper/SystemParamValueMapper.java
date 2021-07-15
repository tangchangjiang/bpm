package org.o2.metadata.console.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.o2.metadata.console.infra.entity.SystemParamValue;
import org.o2.metadata.console.api.vo.SystemParamValueVO;

import java.util.List;
import java.util.Set;

/**
 * 系统参数值Mapper
 *
 * @author hongyun.wang01@hand-china.com 2020-02-13
 */
public interface SystemParamValueMapper extends BaseMapper<SystemParamValue> {
    /**
     * 获取参数类型为String的参数值
     *
     * @param paramCode 参数code
     * @param tenantId  租户id
     * @return StringList<String>
     */
    String getSysValueByParam(@Param("paramCode") String paramCode, @Param("tenantId") Long tenantId);

    /**
     * 获取参数类型为list的参数值
     *
     * @param paramCode 参数code
     * @param tenantId  租户id
     * @return List<String>
     */
    List<String> getSysListByParam(@Param("paramCode") String paramCode, @Param("tenantId") Long tenantId);

    /**
     * 获取参数类型为set的参数值
     *
     * @param paramCode 参数code
     * @param tenantId  租户id
     * @return Set<String>
     */
    Set<String> getSysSetByParam(@Param("paramCode") String paramCode, @Param("tenantId") Long tenantId);

    /**
     * 获取参数类型为set的参数值以及扩展字段
     *
     * @param paramCode 参数code
     * @param tenantId  租户id
     * @return SystemParamValueVO
     */
    List<SystemParamValueVO> getSysSetWithParams(@Param("paramCode") String paramCode, @Param("tenantId") Long tenantId);

}
