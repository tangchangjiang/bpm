package org.o2.metadata.app.service;


import org.o2.metadata.domain.vo.SysParameterVO;

/**
 * 系统参数缓存
 *
 * @author mark.bao@hand-china.com 2019-05-30
 */
public interface SysParameterService {

    /**
     * 保存系统参数缓存
     *
     * @param sysParameterVO 系统参数业务实体
     */
    void saveSysParameter(SysParameterVO sysParameterVO);

    /**
     * 删除系统参数缓存
     *
     * @param tenantId 租户ID
     * @param code     系统参数编码
     */
    void deleteSysParameter(String code, Long tenantId);

    /**
     * 根据系统参数编码获取实体
     *
     * @param tenantId 租户ID
     * @param code     系统参数编码
     * @return 系统参数业务实体
     */
    SysParameterVO getSysParameter(String code, Long tenantId);

    /**
     * 根据系统参数编码获取参数值
     *
     * @param tenantId 租户ID
     * @param code     系统参数编码
     * @return 系统参数值
     */
    String getSysParameterValue(String code, Long tenantId);

    /**
     * 根据系统参数编码判断参数是否有效
     *
     * @param tenantId 租户ID
     * @param code     系统参数编码
     * @return true 有效 false 无效
     */
    boolean isSysParameterActive(String code, Long tenantId);
}
