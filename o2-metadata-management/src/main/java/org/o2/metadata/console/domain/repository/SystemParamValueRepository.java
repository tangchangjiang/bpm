package org.o2.metadata.console.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.infra.entity.SystemParamValue;

import java.util.List;
import java.util.Set;

/**
 * 系统参数值资源库
 *
 * @author hongyun.wang01@hand-china.com 2020-02-13
 */
public interface SystemParamValueRepository extends BaseRepository<SystemParamValue> {
    /**
     * 获取参数类型为String的参数值
     *
     * @param paramCode 参数code
     * @param tenantId  租户id
     * @return StringList<String>
     */
    String getSysValueByParam(String paramCode, Long tenantId);

    /**
     * 获取参数类型为list的参数值
     *
     * @param paramCode 参数code
     * @param tenantId  租户id
     * @return List<String>
     */
    List<String> getSysListByParam(String paramCode, Long tenantId);

    /**
     * 获取参数类型为set的参数值
     *
     * @param paramCode 参数code
     * @param tenantId  租户id
     * @return Set<String>
     */
    Set<String> getSysSetByParam(String paramCode, Long tenantId);
}
